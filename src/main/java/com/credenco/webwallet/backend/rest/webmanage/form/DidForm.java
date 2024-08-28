package com.credenco.webwallet.backend.rest.webmanage.form;

import lombok.Getter;

import java.util.Map;

@Getter
public class DidForm {
    private String type;
    private String displayName;
    private Long mainKeyId;
    private Long signingKeyId;
    private Map<String, String> options;

    public String getEbsiEnvironment() {
        if (options != null && options.containsKey("ebsiEnvironment")) {
            return getOptions().get("ebsiEnvironment");
        }
        return null;
    }

    public String getTaoWalletAddress() {
        if (options != null && options.containsKey("taoWalletAddress")) {
            return getOptions().get("taoWalletAddress");
        }
        return null;
    }
}
