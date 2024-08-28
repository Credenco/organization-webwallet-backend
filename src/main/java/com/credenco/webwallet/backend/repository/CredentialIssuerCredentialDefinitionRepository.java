package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialDefinition;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerDefinition;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialIssuerCredentialDefinitionRepository extends JpaRepository<CredentialIssuerDefinition, Long>, JpaSpecificationExecutor<Credential> {

    @Query("""
            SELECT c FROM CredentialIssuerCredentialDefinition c
            JOIN c.types t 
            WHERE c.credentialIssuerDefinition.wallet.externalKey = :walletExternalKey 
            AND c.credentialIssuerDefinition.externalKey = :credentialIssuerDefinitionExternalKey 
            AND t.credentialType = :type
            """)
    List<CredentialIssuerCredentialDefinition> findByWalletExternalKeyAndExternalKey(String walletExternalKey, String credentialIssuerDefinitionExternalKey, String type);


}



