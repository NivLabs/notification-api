package br.com.nivlabs.notification.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import br.com.nivlabs.notification.domain.dto.ContentDTO;
import br.com.nivlabs.notification.domain.dto.NotificationResultDTO;
import br.com.nivlabs.notification.domain.dto.SendNotificationDTO;
import br.com.nivlabs.notification.domain.dto.SendNotificationResponseDTO;
import br.com.nivlabs.notification.enums.ContentType;
import br.com.nivlabs.notification.enums.NotificationStatusOperation;
import br.com.nivlabs.notification.exception.HttpException;
import br.com.nivlabs.notification.integration.EmailIntegration;

@Component
public class SendEmailHandler {

    @Autowired
    private Logger logger;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private EmailIntegration emailIntegration;

    public SendNotificationResponseDTO execute(SendNotificationDTO request) {
        validateRequest(request);
        logger.info("Starting the email sending process :: {} | {}", request.sender(), request.receiver());

        final String uuidNotification = UUID.randomUUID().toString();
        logger.info("Notification operation UUID :: {}", uuidNotification);
        SendNotificationResponseDTO response = new SendNotificationResponseDTO(uuidNotification, new ArrayList<>());
        request.contents().forEach(content -> {
            if (content.type() == ContentType.htmlTemplate) {
                response.results().add(sendHtmlMessage(uuidNotification, request.subject(), request.sender(),
                                                       request.receiver(), content));
            } else if (content.type() == ContentType.textplain) {
                response.results().add(sendTextMessate(uuidNotification, request.subject(), request.sender(),
                                                       request.receiver(), content));
            } else {
                final String reason = "Email sending only allows text or html content";
                response.results()
                        .add(new NotificationResultDTO(content.sequence(), NotificationStatusOperation.PROCESSED_WITH_ERRORS, reason));
                logger.warn(reason);
            }
        });

        return response;
    }

    private void validateRequest(SendNotificationDTO request) {
        Set<Integer> sequenceSetList = new HashSet<Integer>();
        if (request.contents() != null && request.contents().size() > 0) {
            request.contents().forEach(content -> {
                if (sequenceSetList.contains(content.sequence())) {
                    throw new HttpException(HttpStatus.CONFLICT,
                            "Content sequences cannot be repeated. Repeated sequential " + content.sequence());
                }
            });
        }
    }

    private NotificationResultDTO sendHtmlMessage(String notificationUuid, String subject, String sender,
                                                  String receiver,
                                                  ContentDTO content) {
        try {
            logger.info("Starting html template parse process...");
            emailIntegration.sendHtmlMessage(templateEngine,
                                             subject != null ? subject : "",
                                             sender,
                                             content.value(),
                                             content.templateAttributes() != null ? content.templateAttributes()
                                                                                  : Collections.emptyMap(),
                                             receiver);
            return new NotificationResultDTO(content.sequence(), NotificationStatusOperation.PROCESSED, null);
        } catch (Exception e) {
            logger.error("Failed to send email.", e);
            return new NotificationResultDTO(content.sequence(), NotificationStatusOperation.PROCESSED_WITH_ERRORS, e.getMessage());
        }
    }

    private NotificationResultDTO sendTextMessate(String notificationUuid, String subject, String sender,
                                                  String receiver,
                                                  ContentDTO content) {
        try {
            logger.info("Starting text plain parse process...");
            emailIntegration.sendSimpleMessage(subject, sender, receiver, content.value());
            return new NotificationResultDTO(content.sequence(), NotificationStatusOperation.PROCESSED, null);
        } catch (Exception e) {
            logger.error("Failed to send email.", e);
            return new NotificationResultDTO(content.sequence(), NotificationStatusOperation.PROCESSED_WITH_ERRORS, e.getMessage());
        }
    }

}
