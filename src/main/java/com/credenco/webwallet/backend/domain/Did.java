package com.credenco.webwallet.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
public class Did {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @ToString.Exclude
    private Wallet wallet;

    private String displayName;

    @NotNull
    private String did;

    @NotNull
    private String document;

    @ManyToOne
    @JoinColumn(name = "wallet_key_id")
    @ToString.Exclude
    @NotNull
    private WalletKey walletKey;

    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private String createdBy;
    @NotNull
    private LocalDateTime lastModifiedAt;
    @NotNull
    private String lastModifiedBy;

    public String environment;

    public String getType() {
        int startIndex = getDid().indexOf(":") + 1;
        int endIndex = getDid().indexOf(":", startIndex);
        if (startIndex >= 0 && endIndex >= 0) {
            return getDid().substring(startIndex, endIndex);
        }
        return "Unknown";
    }

    public boolean isEbsi() {
        return did.startsWith("did:ebsi");
    }
}
