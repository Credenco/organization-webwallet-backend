package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.Issuer;
import com.credenco.webwallet.backend.domain.Wallet;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuerRepository extends JpaRepository<Issuer, Long> {

    List<Issuer> findAllByWallet(Wallet wallet);

    Optional<Issuer> findFirstByWalletAndConfigurationUrl(Wallet wallet, String configurationUrl);

}



