package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.PresentationDefinition;
import com.credenco.webwallet.backend.domain.Wallet;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationDefinitionRepository extends JpaRepository<PresentationDefinition, Long> {

    Optional<PresentationDefinition> findByWalletAndId(Wallet wallet, Long id);
    Optional<PresentationDefinition> findByWalletAndExternalKey(Wallet wallet, String externalKey);

    Page<PresentationDefinition> findAllByWallet(Wallet wallet, Pageable pageable);

    @Query("""
            SELECT pd 
            FROM PresentationDefinition pd 
            WHERE pd.wallet = :wallet
            and ((pd.externalKey like %:searchText%)
            or (pd.name like %:searchText%)
            or (pd.description like %:searchText%))
            """)
    Page<PresentationDefinition> searchAllByWallet(Wallet wallet, String searchText, Pageable pageable);


}



