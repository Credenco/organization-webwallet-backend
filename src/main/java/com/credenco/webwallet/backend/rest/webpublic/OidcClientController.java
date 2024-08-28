package com.credenco.webwallet.backend.rest.webpublic;

import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.domain.WalletKey;
import com.credenco.webwallet.backend.repository.WalletKeyRepository;
import com.credenco.webwallet.backend.repository.WalletRepository;
import com.credenco.webwallet.backend.rest.webmanage.dto.KeysDto;
import com.credenco.webwallet.backend.service.did.WalletDidService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.walt.crypto.keys.Key;
import id.walt.crypto.keys.KeyManager;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController()
@RequestMapping("/api/oidc")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class OidcClientController {
    final WalletRepository walletRepository;
    final WalletKeyRepository walletKeyRepository;
    final WalletDidService didService;
    final ObjectMapper objectMapper;

    @GetMapping(path="/{walletExternalKey}")
    public void isAlive(@PathVariable String walletExternalKey) {
        log.info("Client URL called " + walletExternalKey);
    }

    @GetMapping(path="/code-cb/{walletExternalKey}")
    public ResponseEntity<Object> callback(@PathVariable String walletExternalKey, @RequestParam MultiValueMap<String, String> params) {
        log.info("Client URL code-cb called " + walletExternalKey + ":" + params );
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "Goodbye");
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("Farewell, client!");
    }

    @SneakyThrows
    @GetMapping(path="/jwks/{walletExternalKey}")
    public KeysDto getJwks(@PathVariable String walletExternalKey) {
        final Optional<Wallet> wallet = walletRepository.findByExternalKey(walletExternalKey);
        if (wallet.isEmpty()) {
            throw new IllegalArgumentException("Invalid wallet key");
        }

        KeysDto keysDto = KeysDto.builder()
                .keys(walletKeyRepository.findAllByWallet(wallet.get()).stream()
                    .map(this::deserializeKey)
                    .map(this::getPublicKey)
                    .map(this::serializeKey)
                    .toList())
                .build();
        log.info(objectMapper.writeValueAsString(keysDto));

        return keysDto;
    }

    private Key deserializeKey(WalletKey walletKey) {
        return KeyManager.INSTANCE.resolveSerializedKey(walletKey.getDocument());
    }

    @SneakyThrows
    private Key getPublicKey(Key key) {
        return BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) -> key.getPublicKey(continuation));
    }

    @SneakyThrows
    private JsonNode serializeKey(Key key) {
        return objectMapper.readTree(BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) -> key.exportJWKObject(continuation)).toString());
    }
}
