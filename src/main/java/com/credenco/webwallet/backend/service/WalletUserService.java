package com.credenco.webwallet.backend.service;

import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.domain.WalletUser;
import com.credenco.webwallet.backend.domain.WalletUserPreference;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerDisplay;
import com.credenco.webwallet.backend.repository.WalletUserRepository;
import com.credenco.webwallet.backend.rest.FormToJpaCollectionMerger;
import com.credenco.webwallet.backend.rest.webmanage.dto.UserPreferenceDto;
import com.credenco.webwallet.backend.rest.webmanage.form.issuer.CredentialIssuerDisplayForm;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletUserService {

    private final WalletUserRepository walletUserRepository;
    final FormToJpaCollectionMerger<UserPreferenceDto, WalletUserPreference> userPreferenceMerger = new FormToJpaCollectionMerger<>();

    @Transactional
    public WalletUser getWalletUser(Wallet wallet, String userName) {
        final Optional<WalletUser> dbUser = walletUserRepository.findByWalletAndUserName(wallet, userName);
        return (dbUser.isPresent())
                ? dbUser.get()
                : walletUserRepository.save(WalletUser.builder()
                                                    .wallet(wallet)
                                                    .userName(userName)
                                                    .preferredLocale("nl-NL")
                                                    .createdAt(LocalDateTime.now())
                                                    .lastModifiedAt(LocalDateTime.now())
                                                    .build());
    }

    @Transactional
    public WalletUser saveWalletUser(final Wallet wallet, final String name, final String locale) {
        final WalletUser walletUser = getWalletUser(wallet, name);
        walletUser.setPreferredLocale(locale);
        walletUser.setLastModifiedAt(LocalDateTime.now());
        return walletUserRepository.save(walletUser);
    }
    @Transactional
    public WalletUser saveWalletUserPreferences(final Wallet wallet, final String name, final List<UserPreferenceDto> userPreferenceDtos) {
        final WalletUser walletUser = getWalletUser(wallet, name);
        walletUser.setLastModifiedAt(LocalDateTime.now());

        final List<WalletUserPreference> walletUserPreferences = userPreferenceMerger.buildJpaCollection(
                userPreferenceDtos,
                walletUser.getUserPreferences(),
                (userPreferenceDto, walletUserPreference) -> userPreferenceMerger.equals(userPreferenceDto.getPreferenceKey(), walletUserPreference.getPreferenceKey()),
                (form, entity) -> {
                    entity.setPreferenceValue(form.getPreferenceValue());
                    return entity;
                },
                (userPreferenceDto) -> {
                    var walletUserPreference = new WalletUserPreference();
                    walletUserPreference.setPreferenceKey(userPreferenceDto.getPreferenceKey());
                    walletUserPreference.setPreferenceValue(userPreferenceDto.getPreferenceValue());
                    walletUserPreference.setWalletUser(walletUser);
                    return walletUserPreference;
                });
        walletUser.setUserPreferences(walletUserPreferences);

        return walletUserRepository.save(walletUser);
    }
}
