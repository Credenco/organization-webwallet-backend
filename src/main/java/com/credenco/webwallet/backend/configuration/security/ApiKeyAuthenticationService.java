package com.credenco.webwallet.backend.configuration.security;

import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.repository.WalletRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthenticationService {

    private static final String AUTH_TOKEN_HEADER_NAME = "x-api-key";
    private final WalletRepository walletRepository;


    public boolean hasApiKey(HttpServletRequest request) {
        return request.getHeader(AUTH_TOKEN_HEADER_NAME) != null;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null) {
            throw new BadCredentialsException("Invalid API Key");
        }
        final Wallet wallet = walletRepository.findByApiKey(apiKey).orElseThrow(() -> new BadCredentialsException("Invalid API Key"));
        log.info("API Key {} authentication successful for wallet {}", apiKey, wallet.getExternalKey());
        return new LocalPrincipal(wallet, "apiKey", wallet.getDisplayName(), "", wallet.getDisplayName(), AuthorityUtils.createAuthorityList(LocalPrincipal.ROLE_API_KEY));
    }
}
