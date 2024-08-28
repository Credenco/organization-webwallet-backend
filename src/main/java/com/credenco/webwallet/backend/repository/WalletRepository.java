package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByApiKey(String apiKey);
    Optional<Wallet> findByExternalKey(String externalKey);
}


