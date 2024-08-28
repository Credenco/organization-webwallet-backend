package com.credenco.webwallet.backend.rest.webmanage.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TermsOfUseDto {
    private String id;
    private String type;
}
