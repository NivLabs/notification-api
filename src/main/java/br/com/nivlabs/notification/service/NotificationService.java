package br.com.nivlabs.notification.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.nivlabs.notification.domain.dto.SendNotificationDTO;
import br.com.nivlabs.notification.domain.dto.SendNotificationResponseDTO;
import br.com.nivlabs.notification.exception.HttpException;
import br.com.nivlabs.notification.handler.SendEmailHandler;
import jakarta.validation.Valid;

@Service
public class NotificationService {

    @Autowired
    private SendEmailHandler sendEmailHandler;

    @Autowired
    private Logger logger;

    public SendNotificationResponseDTO send(String channel, @Valid SendNotificationDTO request) {
        logger.info("Request to send notification received :: {}", channel);
        switch (channel) {
            case "email":
                return sendEmail(request);
            default:
                throw new HttpException(HttpStatus.NOT_IMPLEMENTED, "Notification sending via " + channel + " not implemented");
        }
    }

    private SendNotificationResponseDTO sendEmail(@Valid SendNotificationDTO request) {
        return sendEmailHandler.execute(request);
    }
}
