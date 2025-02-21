package com.credenco.webwallet.backend.rest.webmanage.form.issuer;

import java.util.ArrayList;
import java.util.List;
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
public class CredentialIssuerDefinitionForm {

    private String externalKey;
    private String name;
    private String description;
    private List<CredentialIssuerDisplayForm> displays = new ArrayList<>();

}
