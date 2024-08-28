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
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReceivedPresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @ToString.Exclude
    private Wallet wallet;

    @NotNull
    public String document;

    private String notes;

    private LocalDateTime receiveDate;

    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private String createdBy;
    @NotNull
    private LocalDateTime lastModifiedAt;
    @NotNull
    private String lastModifiedBy;
}
