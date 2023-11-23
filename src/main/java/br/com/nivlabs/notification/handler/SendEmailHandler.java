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

import br.com.nivlabs.notification.domain.Notification;
import br.com.nivlabs.notification.domain.SMTPSettings;
import br.com.nivlabs.notification.domain.dto.ContentDTO;
import br.com.nivlabs.notification.domain.dto.NotificationResultDTO;
import br.com.nivlabs.notification.domain.dto.SendNotificationDTO;
import br.com.nivlabs.notification.domain.dto.SendNotificationResponseDTO;
import br.com.nivlabs.notification.enums.ContentType;
import br.com.nivlabs.notification.enums.NotificationStatusOperation;
import br.com.nivlabs.notification.exception.HttpException;
import br.com.nivlabs.notification.integration.EmailIntegration;
import br.com.nivlabs.notification.repository.NotificationRepository;
import br.com.nivlabs.notification.security.SecurityContextApplication;
import jakarta.mail.MessagingException;

@Component
public class SendEmailHandler {

    @Autowired
    private Logger logger;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private EmailIntegration emailIntegration;
    @Autowired
    private NotificationRepository notificationRepo;

    public SendNotificationResponseDTO execute(SendNotificationDTO request) {
        validateRequest(request);
        final String channelUuid = SecurityContextApplication.getChannelUuid();
        logger.info("Starting the email sending process :: {} | {} | {}", channelUuid, request.sender(), request.receiver());

        final String uuidNotification = UUID.fromString(channelUuid).toString();
        logger.info("Notification operation UUID :: {}", uuidNotification);

        Notification entity = new Notification(uuidNotification, channelUuid, request.subject(), request.sender(), request.receiver());
        notificationRepo.save(entity);

        SendNotificationResponseDTO response = new SendNotificationResponseDTO(uuidNotification, new ArrayList<>());

        SMTPSettings smtpSettings = new SMTPSettings();
        request.contents().forEach(content -> {
            if (content.type() == ContentType.htmlTemplate) {
                response.results().add(sendHtmlMessage(smtpSettings, request.subject(), request.sender(), request.receiver(), content));
            } else if (content.type() == ContentType.textplain) {
                response.results().add(sendTextMessate(smtpSettings, request.subject(), request.sender(), request.receiver(), content));
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

    private NotificationResultDTO sendHtmlMessage(SMTPSettings settings, String subject, String sender, String receiver,
                                                  ContentDTO content) {
        try {
            logger.info("Starting html template parse process...");
            emailIntegration.sendHtmlMessage(EmailIntegration.getMailSender(settings),
                                             templateEngine,
                                             subject != null ? subject : "",
                                             sender,
                                             content.value(),
                                             content.templateAttributes() != null ? content.templateAttributes()
                                                                                  : Collections.emptyMap(),
                                             receiver);
            return new NotificationResultDTO(content.sequence(), NotificationStatusOperation.PROCESSED, null);
        } catch (MessagingException e) {
            logger.error("Failed to process template email message", e);
            return new NotificationResultDTO(content.sequence(), NotificationStatusOperation.PROCESSED_WITH_ERRORS,
                    "Failed to process template email message");
        } catch (Exception e) {
            logger.error("Failed to send email.", e);
            return new NotificationResultDTO(content.sequence(), NotificationStatusOperation.PROCESSED_WITH_ERRORS, e.getMessage());
        }
    }

    private NotificationResultDTO sendTextMessate(SMTPSettings settings, String subject, String sender, String receiver,
                                                  ContentDTO content) {
        try {
            logger.info("Starting text plain parse process...");
            emailIntegration.sendSimpleMessage(null, subject, sender, receiver, content.value());
            return new NotificationResultDTO(content.sequence(), NotificationStatusOperation.PROCESSED, null);
        } catch (Exception e) {
            logger.error("Failed to send email.", e);
            return new NotificationResultDTO(content.sequence(), NotificationStatusOperation.PROCESSED_WITH_ERRORS, e.getMessage());
        }
    }

}
