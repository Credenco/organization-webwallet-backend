package com.credenco.webwallet.backend.domain.issuer;

import com.credenco.webwallet.backend.domain.Wallet;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CredentialIssuerDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @ToString.Exclude
    private Wallet wallet;

    private String externalKey;
    private String name;
    private String description;

    private String issuerUrl;


    @Builder.Default
    @OneToMany(mappedBy = "credentialIssuerDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CredentialIssuerDisplay> displays = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "credentialIssuerDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CredentialIssuerCredentialDefinition> credentialDefinitions = new ArrayList<>();

    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private String createdBy;
    @NotNull
    private LocalDateTime lastModifiedAt;
    @NotNull
    private String lastModifiedBy;
}
