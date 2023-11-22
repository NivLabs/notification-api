package br.com.nivlabs.notification.domain.dto;

import java.util.Map;

import br.com.nivlabs.notification.enums.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContentDTO(
        int sequence,
        @NotNull(message = "content ty cannot be null")
        ContentType type,
        @NotNull(message = "content value cannot be null")
        @NotBlank(message = "content value cannot be blank")
        String value,
        Map<String, String> templateAttributes) {
}