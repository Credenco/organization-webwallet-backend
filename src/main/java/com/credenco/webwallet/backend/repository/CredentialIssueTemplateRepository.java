package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.CredentialIssueTemplate;
import com.credenco.webwallet.backend.domain.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialIssueTemplateRepository extends JpaRepository<CredentialIssueTemplate, Long> {

    Optional<CredentialIssueTemplate> findByWalletAndCredentialConfigurationId(Wallet wallet, String credentialConfigurationId);

}


