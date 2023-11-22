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

import br.com.nivlabs.notification.domain.SMTPSettings;
import br.com.nivlabs.notification.domain.dto.ContentDTO;
import br.com.nivlabs.notification.domain.dto.NotificationResultDTO;
import br.com.nivlabs.notification.domain.dto.SendNotificationDTO;
import br.com.nivlabs.notification.domain.dto.SendNotificationResponseDTO;
import br.com.nivlabs.notification.enums.ContentType;
import br.com.nivlabs.notification.enums.NotificationStatus;
import br.com.nivlabs.notification.exception.HttpException;
import br.com.nivlabs.notification.integration.EmailIntegration;
import br.com.nivlabs.notification.util.ContextSecurityUtils;
import jakarta.mail.MessagingException;

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
        final String channelUuid = ContextSecurityUtils.getChannelUuid();
        logger.info("Starting the email sending process :: {} | {} | {}", channelUuid, request.from(), request.to());

        final String uuidNotification = UUID.fromString(channelUuid).toString();
        logger.info("Notification operation UUID :: {}", uuidNotification);

        SendNotificationResponseDTO response = new SendNotificationResponseDTO(uuidNotification, new ArrayList<>());

        SMTPSettings smtpSettings = new SMTPSettings();
        request.contents().forEach(content -> {
            if (content.type() == ContentType.htmlTemplate) {
                response.results().add(sendHtmlMessage(smtpSettings, request.subject(), request.from(), request.to(), content));
            } else if (content.type() == ContentType.textplain) {
                response.results().add(sendTextMessate(smtpSettings, request.subject(), request.from(), request.to(), content));
            } else {
                final String reason = "Email sending only allows text or html content";
                response.results().add(new NotificationResultDTO(content.sequence(), NotificationStatus.CANCELED, reason));
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

    private NotificationResultDTO sendHtmlMessage(SMTPSettings settings, String subject, String from, String to, ContentDTO content) {
        try {
            logger.info("Starting html template parse process...");
            emailIntegration.sendHtmlMessage(EmailIntegration.getMailSender(settings),
                                             templateEngine,
                                             subject != null ? subject : "",
                                             from,
                                             content.value(),
                                             content.templateAttributes() != null ? content.templateAttributes()
                                                                                  : Collections.emptyMap(),
                                             to);
            return new NotificationResultDTO(content.sequence(), NotificationStatus.PROCESSED, null);
        } catch (MessagingException e) {
            logger.error("Failed to process template email message", e);
            return new NotificationResultDTO(content.sequence(), NotificationStatus.CANCELED, "Failed to process template email message");
        } catch (Exception e) {
            logger.error("Failed to send email.", e);
            return new NotificationResultDTO(content.sequence(), NotificationStatus.CANCELED, e.getMessage());
        }
    }

    private NotificationResultDTO sendTextMessate(SMTPSettings settings, String subject, String from, String to, ContentDTO content) {
        try {
            logger.info("Starting text plain parse process...");
            emailIntegration.sendSimpleMessage(null, subject, from, to, content.value());
            return new NotificationResultDTO(content.sequence(), NotificationStatus.PROCESSED, null);
        } catch (Exception e) {
            logger.error("Failed to send email.", e);
            return new NotificationResultDTO(content.sequence(), NotificationStatus.CANCELED, e.getMessage());
        }
    }

}
