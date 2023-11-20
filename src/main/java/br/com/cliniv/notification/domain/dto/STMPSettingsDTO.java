package br.com.cliniv.notification.domain.dto;

public record STMPSettingsDTO(
        String uuid,
        String host,
        int port,
        String userName,
        String password) {

}
