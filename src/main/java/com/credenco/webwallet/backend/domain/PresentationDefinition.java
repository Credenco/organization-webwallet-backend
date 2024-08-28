package com.credenco.webwallet.backend.domain;

import com.credenco.webwallet.backend.service.verify.apiclient.PresentationDefinitionApiDto;
import com.credenco.webwallet.backend.service.verify.apiclient.VerifierApiClientImpl;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
public class PresentationDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalKey;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @ToString.Exclude
    private Wallet wallet;

    private String purpose;
    private String notes;

    @Builder.Default
    @OneToMany(mappedBy = "presentationDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PresentationDefinitionPolicy> policies = new ArrayList<>();

//    private List<FieldConstraint> fieldConstraints;

    @Builder.Default
    @OneToMany(mappedBy = "presentationDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PresentationDefinitionCredentialType> credentialTypes = new ArrayList<>();

    private String clientUrl;
    private String successRedirectUrl;
    private String errorRedirectUrl;

    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private String createdBy;
    @NotNull
    private LocalDateTime lastModifiedAt;
    @NotNull
    private String lastModifiedBy;


    public List<PresentationDefinitionPolicy> getVpPolicies() {
        return policies.stream()
                .filter(p -> p.getPresentationDefinitionPolicyType() == PresentationDefinitionPolicyType.VP)
                .toList();
    }
    public List<PresentationDefinitionPolicy> getVcPolicies() {
        return policies.stream()
                .filter(p -> p.getPresentationDefinitionPolicyType() == PresentationDefinitionPolicyType.VP)
                .toList();
    }

    public void addPolicy(final PresentationDefinitionPolicy policy) {
        if (policies == null) {
            policies = new ArrayList<>();
        }
        policies.add(policy);
        policy.setPresentationDefinition(this);
    }

    public Map<String, Object> generatePresentationDefinition() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", UUID.randomUUID().toString());
        result.put("purpose", purpose);
        result.put("input_descriptors", generateInputDescriptors());

        return result;
    }

    private List<Map<String, Object>> generateInputDescriptors() {
        return credentialTypes.stream()
                .map(PresentationDefinition::generateInputDescriptor)
                .toList();
    }

    private static Map<String, Object> generateInputDescriptor(PresentationDefinitionCredentialType type) {
        return Map.of("id", type.getVcType(),
                      "format",
                      Map.of("jwt_vc_json",
                             Map.of("alg", List.of("EdDSA", "ES256"))),
                      "constraints",
                      Map.of("fields", List.of(
                              Map.of("path", List.of("$.type"),
                                     "filter",
                                     Map.of("type", "string",
                                            "pattern", type.getVcType())))));
    }
}

//public class FieldConstraint {
//    private FieldConstraintType type;
//    private List<String> paths;
//    private String value;
//}
