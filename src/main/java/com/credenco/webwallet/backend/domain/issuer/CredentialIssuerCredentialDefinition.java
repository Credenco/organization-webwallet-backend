package com.credenco.webwallet.backend.domain.issuer;

import com.credenco.webwallet.backend.domain.CredentialIssueTemplate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CredentialIssuerCredentialDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credential_issuer_id")
    @ToString.Exclude
    private CredentialIssuerDefinition credentialIssuerDefinition;

    private String credentialConfigurationId;

    private String format;

    @Builder.Default
    @OneToMany(mappedBy = "credentialDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CredentialIssuerCredentialDisplay> displays = new ArrayList<>();

    @OrderBy("typeOrder ASC")
    @Builder.Default
    @OneToMany(mappedBy = "credentialDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CredentialIssuerCredentialType> types = new ArrayList<>();

    @OrderBy("attributeOrder ASC")
    @Builder.Default
    @OneToMany(mappedBy = "credentialDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CredentialIssuerCredentialAttribute> attributes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "issue_template_id")
    @ToString.Exclude
    private CredentialIssueTemplate credentialIssueTemplate;
}
