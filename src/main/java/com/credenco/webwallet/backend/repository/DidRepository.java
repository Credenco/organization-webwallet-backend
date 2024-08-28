package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.Did;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.domain.WalletKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DidRepository extends JpaRepository<Did, Long> {
    List<Did> findAllByWallet(final Wallet wallet);

    Optional<Did> findByIdAndWallet(final Long id, final Wallet wallet);

    Optional<Did> findByDidAndWallet(final String did, final Wallet wallet);

}



