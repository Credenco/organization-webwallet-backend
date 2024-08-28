package com.credenco.webwallet.backend.domain;

import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerDefinition;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CredentialIssueTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String credentialConfigurationId;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @ToString.Exclude
    private Wallet wallet;

    private String displayName;

    private String template;

    @ManyToOne
    @JoinColumn(name = "issuer_did_id")
    @ToString.Exclude
    private Did issuerDid;

    @ManyToOne
    @JoinColumn(name = "credential_issuer_id")
    @ToString.Exclude
    private CredentialIssuerDefinition credentialIssuerDefinition;

}
