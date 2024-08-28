package com.credenco.webwallet.backend.domain;

import id.walt.crypto.utils.JwsUtils;
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

import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonObject;
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
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @ToString.Exclude
    private Wallet wallet;

    @NotNull
    public String document;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CredentialFormat credentialFormat;

    private String disclosures;

    private String metadata;

    /**
     * The data that was used to search for this credential. Filled with relevant data from credential.
     */
    private String searchData;

    /**
     * The id of in the credential_configurations_supported section a stored in credentialConfiguration.
     */
    private String credentialConfigurationId;
    /**
     * The credential configuration as returned by the issuer /.well-known/openid-credential-issuer endpoint for this credential type.
     */
    private String credentialConfiguration;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "issuer_id")
    @ToString.Exclude
    private Issuer issuer;

    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private LocalDateTime issuanceDate;

    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private String createdBy;
    @NotNull
    private LocalDateTime lastModifiedAt;
    @NotNull
    private String lastModifiedBy;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CredentialStatus status;

    public JsonObject asJson() {
        if (document.startsWith("{")) {
            return (JsonObject) Json.Default.parseToJsonElement(document);
        } else if (document.startsWith("ey")) {
            final var payload = JwsUtils.INSTANCE.decodeJws(document, false,  false).getPayload();
            if (payload.containsKey("vc")) {
                return (JsonObject) payload.get("vc");
            } else {
                return payload;
            }
        }
        return null;
    }

    public String getCredentialUuid() {
        return asJson().get("id") != null ? asJson().get("id").toString(): null;
    }
}
