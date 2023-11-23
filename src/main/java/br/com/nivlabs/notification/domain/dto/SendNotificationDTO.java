package br.com.nivlabs.notification.domain.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendNotificationDTO(
        String subject,
        @NotNull(message = "sender cannot be null")
        @NotBlank(message = "sender cannot be blank")
        String sender,
        @NotNull(message = "receiver cannot be null")
        @NotBlank(message = "receiver cannot be blank")
        String receiver,
        @NotNull(message = "from cannot be null")
        List<ContentDTO> contents) {

}
