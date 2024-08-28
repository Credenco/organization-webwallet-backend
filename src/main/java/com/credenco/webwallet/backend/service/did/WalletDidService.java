package com.credenco.webwallet.backend.service.did;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.Did;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.domain.WalletKey;
import com.credenco.webwallet.backend.repository.DidRepository;
import com.credenco.webwallet.backend.repository.WalletKeyRepository;
import com.credenco.webwallet.backend.rest.webmanage.form.DidForm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.walt.crypto.keys.Key;
import id.walt.crypto.keys.KeyManager;
import id.walt.crypto.keys.KeySerialization;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.jwk.JWKKey;
import id.walt.did.dids.DidService;
import id.walt.did.dids.registrar.DidResult;
import id.walt.did.dids.registrar.dids.DidCreateOptions;
import id.walt.did.dids.registrar.dids.DidEbsiCreateOptions;
import id.walt.did.dids.registrar.dids.DidJwkCreateOptions;
import id.walt.ebsi.EbsiEnvironment;
import id.walt.ebsi.did.DidEbsiService;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.serialization.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletDidService {
    final WalletKeyRepository walletKeyRepository;
    final DidRepository didRepository;
    final EbsiDidService ebsiDidService;
    final ObjectMapper objectMapper;

    @SneakyThrows
    public Did createDid(final LocalPrincipal principal,
                         final Optional<WalletKey> mainWalletKey,
                         final Optional<WalletKey> signingWalletKey,
                         final DidForm didForm) {
        final WalletKey mainKey = getMainKey(principal, mainWalletKey, didForm);
        final WalletKey vcSigningKey = getSigningKey(principal, signingWalletKey, didForm);
        final DidResult didResult = registerDid(principal, didForm, mainKey, vcSigningKey);


        return didRepository.save(Did.builder()
                                          .wallet(principal.getWallet())
                                          .did(didResult.getDid())
                                          .displayName(didForm.getDisplayName())
                                          .walletKey(mainKey)
                                          .document(didResult.getDidDocument().toString())
                                          .environment(didForm.getEbsiEnvironment())
                                          .createdAt(LocalDateTime.now())
                                          .createdBy(principal.getName())
                                          .lastModifiedAt(LocalDateTime.now())
                                          .lastModifiedBy(principal.getName())
                                          .build());
    }

    @SneakyThrows
    public Did resolveDid(final LocalPrincipal principal, final String didId) {
        final Did did = didRepository.findByDidAndWallet(didId, principal.getWallet()).orElseThrow();

        if (didId.startsWith("did:ebsi:")) {
            did.setDocument(ebsiDidService.resolveDid(didId, did.getEnvironment()));
            didRepository.save(did);
        }
        return did;
    }

    @SneakyThrows
    public Did addService(final LocalPrincipal principal,
                          final String didId,
                          final String serviceId,
                          final String serviceType,
                          final String serviceEndpoint) {

        final Did did = didRepository.findByDidAndWallet(didId, principal.getWallet()).orElseThrow();
        did.setDocument(ebsiDidService.addService(didId, did.getEnvironment(), did.getWalletKey(), serviceId, serviceType, serviceEndpoint));
        didRepository.save(did);
        return did;
    }

    @SneakyThrows
    public String registerTrustedIssuer(final LocalPrincipal principal,
                                        final String didId,
                                        final String schemaId,
                                        final String taoWalletAddress) {

        final Did did = didRepository.findByDidAndWallet(didId, principal.getWallet()).orElseThrow();
        if (did.isEbsi()) {
            ebsiDidService.registerIssuer(principal,
                                          did.getWalletKey(),
                                          getSigningKey(did),
                                          EbsiEnvironment.valueOf(did.getEnvironment()),
                                          didId,
                                          schemaId,
                                          taoWalletAddress);
            return "";
        }
        throw new IllegalArgumentException("The DID is not part of the EBSI framework");
    }

    @SneakyThrows
    private WalletKey getMainKey(final LocalPrincipal principal,
                                 final Optional<WalletKey> mainWalletKey,
                                 final DidForm didForm) {
        if (didForm.getType().equals("EBSI")) {
            return getOrCreateDidKey(principal, mainWalletKey, KeyType.secp256k1, didForm.getDisplayName() + " Main Key");
        } else {
            return getOrCreateDidKey(principal, mainWalletKey, KeyType.secp256r1, didForm.getDisplayName() + " Key");
        }
    }

    @SneakyThrows
    private WalletKey getSigningKey(final LocalPrincipal principal,
                                    final Optional<WalletKey> signingWalletKey,
                                    DidForm didForm) {
        if (didForm.getType().equals("EBSI")) {
            return getOrCreateDidKey(principal, signingWalletKey, KeyType.secp256r1, didForm.getDisplayName() + " Signing Key");
        }
        return null;
    }

    @SneakyThrows
    private WalletKey getSigningKey(final Did did) {
        final JsonNode didDocument = objectMapper.readTree(did.getDocument());
        final String assertionMethod = didDocument.get("assertionMethod").get(0).asText();
        return walletKeyRepository.findByKid(assertionMethod.substring(assertionMethod.lastIndexOf("#") + 1)).orElseThrow();
    }

    private Key deserializeKey(WalletKey walletKey) {
        return KeyManager.INSTANCE.resolveSerializedKey(walletKey.getDocument());
    }

    private WalletKey getOrCreateDidKey(final LocalPrincipal principal, Optional<WalletKey> walletKey, KeyType keyType, String displayName) throws InterruptedException {
        if (walletKey.isEmpty()) {
            Key newKey = BuildersKt.runBlocking(
                    EmptyCoroutineContext.INSTANCE,
                    (scope, continuation) -> JWKKey.Companion.generate(keyType, null, continuation));

            String keyId = BuildersKt.runBlocking(
                    EmptyCoroutineContext.INSTANCE,
                    (scope, continuation) -> newKey.getKeyId(continuation));

            return walletKeyRepository.save(WalletKey.builder()
                                             .wallet(principal.getWallet())
                                             .kid(keyId)
                                             .displayName(displayName)
                                             .document(KeySerialization.INSTANCE.serializeKey(newKey))
                                             .createdAt(LocalDateTime.now())
                                             .createdBy(principal.getName())
                                             .lastModifiedAt(LocalDateTime.now())
                                             .lastModifiedBy(principal.getName())
                                             .build());
        } else {
            return walletKey.get();
        }
    }

    private DidResult registerDid(final LocalPrincipal principal, DidForm didForm, WalletKey mainKey, WalletKey vcSigningKey) throws InterruptedException {
        if (didForm.getType().equals("EBSI")) {
            final var ebsiEnvironment = didForm.getEbsiEnvironment();

            return ebsiDidService.registerDid(principal, mainKey, vcSigningKey, EbsiEnvironment.valueOf(ebsiEnvironment), didForm.getTaoWalletAddress());
        } else {
            return BuildersKt.runBlocking(
                    EmptyCoroutineContext.INSTANCE,
                    (scope, continuation) -> DidService.INSTANCE.registerByKey(didForm.getType().toLowerCase(),
                                                                               deserializeKey(mainKey),
                                                                               new DidJwkCreateOptions(KeyType.secp256r1),
                                                                               deserializeKey(vcSigningKey != null ? vcSigningKey : mainKey),
                                                                               continuation));
        }
    }
}
