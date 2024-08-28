package com.credenco.webwallet.backend.domain.issuer;

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
public class CredentialIssuerCredentialDisplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credential_definition_id")
    @ToString.Exclude
    private CredentialIssuerCredentialDefinition credentialDefinition;

    private String displayName;
    private String backgroundColor;
    private String textColor;
    private String backgroundImageUrl;
    private String backgroundAltText;
    private String locale;
}
