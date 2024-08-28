package com.credenco.webwallet.backend.rest.webmanage;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.Did;
import com.credenco.webwallet.backend.domain.WalletKey;
import com.credenco.webwallet.backend.repository.DidRepository;
import com.credenco.webwallet.backend.repository.WalletKeyRepository;
import com.credenco.webwallet.backend.rest.webmanage.form.DidForm;
import com.credenco.webwallet.backend.rest.webmanage.dto.DidDto;
import com.credenco.webwallet.backend.rest.webmanage.dto.DidDtoMapper;
import com.credenco.webwallet.backend.rest.webmanage.form.ServiceForm;
import com.credenco.webwallet.backend.rest.webmanage.form.TrustedIssuerForm;
import com.credenco.webwallet.backend.service.did.WalletDidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

@RestController()
@RequestMapping("/api/did")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class DidController {
    final DidRepository didRepository;
    final WalletKeyRepository walletKeyRepository;
    final WalletDidService didService;
    final DidDtoMapper didDtoMapper;

    @GetMapping
    public List<DidDto> getDids(LocalPrincipal principal) {
        final List<Did> dids = didRepository.findAllByWallet(principal.getWallet());
        return didDtoMapper.from(dids);
    }

    @GetMapping("/{didId}")
    public DidDto getDid(LocalPrincipal principal, @PathVariable Long didId) {
        final Optional<Did> did = didRepository.findByIdAndWallet(didId, principal.getWallet());
        if (did.isEmpty()) {
            throw new IllegalArgumentException("Did not found");
        }
        final Did resolvedDid = didService.resolveDid(principal, did.get().getDid());
        return didDtoMapper.from(resolvedDid);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public DidDto createDid(LocalPrincipal principal, @RequestBody DidForm didForm) {
        Optional<WalletKey> mainWalletKey = getWalletKey(principal, didForm.getMainKeyId());
        Optional<WalletKey> signingWalletKey = getWalletKey(principal, didForm.getSigningKeyId());

        return didDtoMapper.from(didService.createDid(principal, mainWalletKey, signingWalletKey, didForm));
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public DidDto updateDid(LocalPrincipal principal, @PathVariable Long id) {
        Did did = didRepository.findByIdAndWallet(id, principal.getWallet()).orElseThrow();
        // TBD
        return didDtoMapper.from(did);
    }

    @PostMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public DidDto addService(LocalPrincipal principal, @PathVariable Long id, @RequestBody ServiceForm serviceForm) {
        Did did = didRepository.findByIdAndWallet(id, principal.getWallet()).orElseThrow();
        return didDtoMapper.from(didService.addService(principal,  did.getDid(), serviceForm.getServiceId(), serviceForm.getServiceType(), serviceForm.getServiceEndpoint()));
    }

    @PostMapping(path = "/{id}/registerIssuer", consumes = APPLICATION_JSON_VALUE)
    public String registerIssuer(LocalPrincipal principal, @PathVariable Long id, @RequestBody TrustedIssuerForm trustedIssuerForm) {
        Did did = didRepository.findByIdAndWallet(id, principal.getWallet()).orElseThrow();
        return didService.registerTrustedIssuer(principal,  did.getDid(), trustedIssuerForm.getSchemaId(), trustedIssuerForm.getTaoWalletAddress());
    }

    private Optional<WalletKey> getWalletKey(LocalPrincipal principal, Long keyId) {
        if (keyId != null) {
            Optional<WalletKey> walletKey = walletKeyRepository.findByIdAndWallet(keyId, principal.getWallet());
            if (walletKey.isEmpty()) {
                throw new IllegalArgumentException("Wallet key not found");
            }
            return walletKey;
        }
        return Optional.empty();
    }
}
