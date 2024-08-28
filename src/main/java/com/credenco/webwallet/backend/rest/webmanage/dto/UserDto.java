package com.credenco.webwallet.backend.rest.webmanage.dto;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.WalletUser;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto {

    private String name;
    private String fullName;
    private String firstName;
    private String lastName;
    private String organization;
    private String base64EncodedOrganizationLogo;
    private String locale;
    private List<UserPreferenceDto> userPreferences;

    public static UserDto from(LocalPrincipal principal, WalletUser walletUser) {
        return UserDto.builder()
                .name(principal.getName())
                .fullName(principal.getFullName())
                .firstName(principal.getFirstName())
                .lastName(principal.getLastName())
                .organization(principal.getWallet().getDisplayName())
                .base64EncodedOrganizationLogo(principal.getWallet().getLogo() != null ? new String(principal.getWallet().getLogo()) : null)
                .locale(walletUser.getPreferredLocale())
                .userPreferences(UserPreferenceDto.from(walletUser.getUserPreferences()))
                .build();

    }
}
