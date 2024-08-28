package com.credenco.webwallet.backend.service;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.domain.History;
import com.credenco.webwallet.backend.domain.HistoryAction;
import com.credenco.webwallet.backend.domain.HistoryEvent;
import com.credenco.webwallet.backend.domain.WalletUser;
import com.credenco.webwallet.backend.repository.HistoryRepository;
import com.credenco.webwallet.backend.repository.WalletUserRepository;
import id.walt.credentials.utils.W3CVcUtils;
import id.walt.credentials.vc.vcs.W3CVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {
    final HistoryRepository historyRepository;
    final WalletUserRepository walletUserRepository;

    public void addHistory(final LocalPrincipal principal,
                           final HistoryEvent event,
                           final HistoryAction action,
                           final String party,
                           final String evidence,
                           final List<Credential> credentials) {
        final WalletUser walletUser = walletUserRepository.findByWalletAndUserName(principal.getWallet(), principal.getUserName()).orElseThrow();
        historyRepository.save(History.builder()
                                       .wallet(walletUser.getWallet())
                                       .user(walletUser)
                                       .eventDate(LocalDateTime.now())
                                       .event(event)
                                       .action(action)
                                       .party(party)
                                       .evidence(evidence)
                                       .credentialUuid(getCredentialUuids(credentials))
                                       .build());
    }

    private static String getCredentialUuids(List<Credential> credentials) {
        if (credentials == null) {
            return null;
        }
        return credentials.stream().map(Credential::getCredentialUuid).collect(Collectors.joining(", "));
    }
}
