package com.credenco.webwallet.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @ToString.Exclude
    private Wallet wallet;

    @JoinColumn
    @ToString.Exclude
    private String credentialUuid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private WalletUser user;

    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private HistoryEvent event;

    @NotNull
    @Enumerated(EnumType.STRING)
    private HistoryAction action;

    private String party;

    private String evidence;
}
