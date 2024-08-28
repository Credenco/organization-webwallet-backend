package com.credenco.webwallet.backend.service.verify.apiclient;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PresentationDefinitionApiDto {
    private String purpose;
    private List<String> vpPolicies;
    private List<String> vcPolicies;
    private List<String> credentialTypes;
    private Map<String, Object> presentationDefinition;
    private String successRedirectUrl;
    private String clientUrl;
}
