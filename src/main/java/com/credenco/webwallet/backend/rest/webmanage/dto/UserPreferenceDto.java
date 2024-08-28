package com.credenco.webwallet.backend.rest.webmanage.dto;

import com.credenco.webwallet.backend.domain.WalletUserPreference;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserPreferenceDto {

    private String preferenceKey;
    private String preferenceValue;

    public static List<UserPreferenceDto> from(List<WalletUserPreference> walletUserPreferences) {
        if (walletUserPreferences == null || walletUserPreferences.isEmpty()) {
            return List.of();
        }
        return walletUserPreferences.stream()
                .map(UserPreferenceDto::from)
                .toList();
    }

    public static UserPreferenceDto from(WalletUserPreference walletUserPreference) {
        return UserPreferenceDto.builder()
                .preferenceKey(walletUserPreference.getPreferenceKey())
                .preferenceValue(walletUserPreference.getPreferenceValue())
                .build();
    }
}
