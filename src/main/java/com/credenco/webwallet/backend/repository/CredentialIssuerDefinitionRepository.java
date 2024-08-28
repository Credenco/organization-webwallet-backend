package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerDefinition;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialIssuerDefinitionRepository extends JpaRepository<CredentialIssuerDefinition, Long>, JpaSpecificationExecutor<Credential> {

    Optional<CredentialIssuerDefinition> findByWalletExternalKeyAndExternalKey(String walletExternalKey, String issuerExternalKey);

    Optional<CredentialIssuerDefinition> findByWalletAndId(Wallet wallet, Long id);

    Page<CredentialIssuerDefinition> findByWallet(Wallet wallet, Pageable pageable);

    Page<CredentialIssuerDefinition> findByWalletAndNameLike(Wallet wallet, String name, Pageable pageable);
}



