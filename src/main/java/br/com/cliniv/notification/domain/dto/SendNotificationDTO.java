package br.com.cliniv.notification.domain.dto;

import br.com.cliniv.notification.enums.ChannelType;

public record SendNotificationDTO(
        String id,
        String from,
        String to,
        ChannelType channel) {

}
