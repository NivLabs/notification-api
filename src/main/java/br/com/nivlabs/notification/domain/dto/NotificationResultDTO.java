package br.com.nivlabs.notification.domain.dto;

import br.com.nivlabs.notification.enums.NotificationStatus;

public record NotificationResultDTO(
        int sequence,
        NotificationStatus status,
        String reasonForCancellation) {

}
