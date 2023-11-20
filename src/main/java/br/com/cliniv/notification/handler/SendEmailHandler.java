package br.com.cliniv.notification.handler;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cliniv.notification.domain.dto.SendNotificationDTO;
import jakarta.validation.Valid;

@Component
public class SendEmailHandler {

    @Autowired
    private Logger logger;

    public SendNotificationDTO execute(@Valid SendNotificationDTO request) {
        logger.info("Starting the email sending process :: {} : {}", request.from(), request.to());
        return null;
    }

}
