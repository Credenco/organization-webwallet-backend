package com.credenco.webwallet.backend.rest.webmanage.form.issuer;

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
public class CredentialIssuerDisplayForm {

    private Long id;
    private String displayName;
    private String logoUrl;
    private String logoAltText;
    private String locale;

}
