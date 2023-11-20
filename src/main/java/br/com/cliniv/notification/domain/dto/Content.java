package br.com.cliniv.notification.domain.dto;

import br.com.cliniv.notification.enums.ContentType;

public record Content(
        ContentType type,
        String text) {

}
