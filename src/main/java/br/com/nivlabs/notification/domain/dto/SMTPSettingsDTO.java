package br.com.nivlabs.notification.domain.dto;

public record SMTPSettingsDTO(
        String host,
        Integer port,
        String userName,
        String password) {

}
