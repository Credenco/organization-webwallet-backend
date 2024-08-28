package com.credenco.webwallet.backend.domain;

import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialDefinition;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
public class IssuedCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @ToString.Exclude
    private Wallet wallet;


    @ManyToOne
    @JoinColumn(name = "credential_issuer_credential_definition_id")
    @ToString.Exclude
    private CredentialIssuerCredentialDefinition credentialIssuerCredentialDefinition;

    @ManyToOne
    @JoinColumn(name = "issuer_id")
    @ToString.Exclude
    private Issuer issuer;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CredentialFormat credentialFormat;

    /**
     * The UUID as stored in de VC json (document)
     */
    @NotNull
    public String credentialUuid;

    @NotNull
    public String document;

    /**
     * The data that was used to search for this credential. Filled with relevant data from credential.
     */
    private String searchData;

    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private LocalDateTime issuanceDate;


}
