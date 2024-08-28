package com.credenco.webwallet.backend.configuration.security;

import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final WalletRepository walletRepository;


    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        var walletExternalKey = source.getClaimAsString("wallet_external_key");
        var name = source.getClaimAsString("preferred_username");
        var firstName = source.getClaimAsString("given_name");
        var lastName = source.getClaimAsString("family_name");
        var fullName = firstName + " " + lastName;
        final Wallet wallet = walletRepository.findByExternalKey(walletExternalKey)
                .orElseThrow(() -> new IllegalArgumentException("Invalid wallet external key"));
        return new LocalPrincipal(wallet, name, fullName.trim(), firstName, lastName, AuthorityUtils.createAuthorityList(LocalPrincipal.ROLE_END_USER));
    }
}
