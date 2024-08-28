package com.credenco.webwallet.backend.rest.webmanage.dto;

import com.credenco.webwallet.backend.domain.History;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class HistoryDto {
    private Long id;
    private String username;
    private String evidence;
    private LocalDateTime eventDate;
    private String event;
    private String action;
    private String party;

    public static HistoryDto from(History history) {
        return HistoryDto.builder()
                .id(history.getId())
                .event(history.getEvent().name())
                .action(history.getAction().name())
                .party(history.getParty())
                .evidence(history.getEvidence())
                .eventDate(history.getEventDate())
                .username(history.getUser() != null ? history.getUser().getUserName(): null)
                .build();
    }
}
