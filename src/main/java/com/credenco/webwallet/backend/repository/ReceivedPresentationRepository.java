package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.ReceivedPresentation;
import com.credenco.webwallet.backend.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceivedPresentationRepository extends JpaRepository<ReceivedPresentation, Long> {

    List<ReceivedPresentation> findByWallet(Wallet wallet);

    Optional<ReceivedPresentation> findByWalletAndId(Wallet wallet, Long id);
}



