package br.com.nivlabs.notification.domain.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendNotificationDTO(
        String subject,
        @NotNull(message = "from cannot be null")
        @NotBlank(message = "from cannot be blank")
        String from,
        @NotNull(message = "to cannot be null")
        @NotBlank(message = "to cannot be blank")
        String to,
        @NotNull(message = "from cannot be null")
        List<ContentDTO> contents) {

}
