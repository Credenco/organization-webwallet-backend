package com.credenco.webwallet.backend.rest.webmanage;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.History;
import com.credenco.webwallet.backend.repository.HistoryRepository;
import com.credenco.webwallet.backend.rest.webmanage.dto.HistoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/history")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class HistoryController {
    final HistoryRepository historyRepository;

    @GetMapping
    public PagedModel<HistoryDto> getHistory(LocalPrincipal principal, Pageable pageable) {
        final Page<History> histories = historyRepository.findAllByWalletOrderByEventDateDesc(principal.getWallet(), pageable);
        return new PagedModel<>(histories.map(HistoryDto::from));
    }
}
