package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.History;
import com.credenco.webwallet.backend.domain.Issuer;
import com.credenco.webwallet.backend.domain.PresentationDefinition;
import com.credenco.webwallet.backend.domain.Wallet;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    Page<History> findAllByWalletOrderByEventDateDesc(Wallet wallet, Pageable pageable);

}
