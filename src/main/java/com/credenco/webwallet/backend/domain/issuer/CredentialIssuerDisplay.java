package com.credenco.webwallet.backend.domain.issuer;

import com.credenco.webwallet.backend.domain.PresentationDefinitionCredentialType;
import com.credenco.webwallet.backend.domain.PresentationDefinitionPolicy;
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
public class CredentialIssuerDisplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credential_issuer_id")
    @ToString.Exclude
    private CredentialIssuerDefinition credentialIssuerDefinition;

    private String displayName;
    private String logoUrl;
    private String logoAltText;
    private String locale;
}
