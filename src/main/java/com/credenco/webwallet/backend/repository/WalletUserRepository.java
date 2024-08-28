package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.domain.WalletUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletUserRepository extends JpaRepository<WalletUser, Long> {

    Optional<WalletUser> findByWalletAndUserName(Wallet wallet, String userName);
}


