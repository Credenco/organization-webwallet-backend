package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.domain.WalletKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletKeyRepository extends JpaRepository<WalletKey, Long> {
    Optional<WalletKey> findByIdAndWallet(final Long id, final Wallet wallet);

    List<WalletKey> findAllByWallet(final Wallet wallet);
    Optional<WalletKey> findByKid(final String kid);

}



