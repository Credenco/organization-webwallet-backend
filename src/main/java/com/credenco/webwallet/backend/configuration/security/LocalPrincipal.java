package com.credenco.webwallet.backend.configuration.security;

import com.credenco.webwallet.backend.domain.Wallet;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class LocalPrincipal extends AbstractAuthenticationToken {

    public static final String ROLE_API_KEY = "ROLE_API_KEY";
    public static final String ROLE_END_USER = "ROLE_END_USER";

    private final Wallet wallet;
    private final String userName;
    private final String fullName;
    private final String firstName;
    private final String lastName;

    public LocalPrincipal(Wallet wallet, String userName, String fullName, final String firstName, final String lastName, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.wallet = wallet;
        this.userName = userName;
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return wallet;
    }

    @Override
    public String getName() {
        return userName;
    }
}
