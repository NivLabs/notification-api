package br.com.nivlabs.notification.domain.dto;

import br.com.nivlabs.notification.enums.NotificationStatusOperation;

public record NotificationResultDTO(
        int sequence,
        NotificationStatusOperation status,
        String reasonForCancellation) {

}
