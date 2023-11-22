package br.com.nivlabs.notification.domain.dto;

import java.util.List;

public record SendNotificationResponseDTO(
        String uuid,
        List<NotificationResultDTO> results) {

}
