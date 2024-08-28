package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.LinkedPresentation;
import com.credenco.webwallet.backend.domain.ReceivedPresentation;
import com.credenco.webwallet.backend.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkedPresentationRepository extends JpaRepository<LinkedPresentation, Long> {

    Optional<LinkedPresentation> findByWalletExternalKeyAndExternalKey(String walletExternalKey, String externalKey);
}



