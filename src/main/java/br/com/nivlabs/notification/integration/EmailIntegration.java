package br.com.nivlabs.notification.integration;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.nivlabs.notification.domain.SMTPSettings;
import br.com.nivlabs.notification.exception.HttpException;
import br.com.nivlabs.notification.util.EncryptUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailIntegration {

    private static Logger logger = LoggerFactory.getLogger(EmailIntegration.class);

    private static Map<String, JavaMailSender> senders = new HashMap<>();

    /**
     * Create or update SMTP Client instance
     * 
     * @param settings New settings
     * @return Updated Settings
     */
    public static JavaMailSender createOrUpdateMailSender(SMTPSettings settings) {
        senders.remove(settings.getChannelUuid());
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(settings.getHost());
        javaMailSender.setPort(settings.getPort());

        javaMailSender.setUsername(settings.getUserName());
        try {
            javaMailSender.setPassword(EncryptUtils.getInstance().decrypt(settings.getPassword()));
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
                 | IllegalBlockSizeException | BadPaddingException e) {
            logger.error("Failed to remove email password encryption", e);
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to start SMTP client", e);
        }

        Properties props = javaMailSender.getJavaMailProperties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        senders.put(settings.getChannelUuid(), javaMailSender);
        return getMailSender(settings);
    }

    /**
     * Search SMTP client instance
     * 
     * @param settings SMTP Client Settings
     * @return SMTP client instance
     */
    public static JavaMailSender getMailSender(SMTPSettings settings) {
        if (senders.containsKey(settings.getChannelUuid())) {
            return senders.get(settings.getChannelUuid());
        } else {
            return createOrUpdateMailSender(settings);
        }
    }

    /**
     * Remove data from cache system drive
     * 
     * @param senderUuid Sender UUID
     */
    public static void removeMailSender(String senderUuid) {
        senders.remove(senderUuid);
    }

    /**
     * Send a simple message via email
     * 
     * @param javaMailSender SMTP Client
     * @param subject Message subject
     * @param sender Sender's email
     * @param to Email that will receive the message
     * @param text Text that will be sent by email
     */
    public void sendSimpleMessage(JavaMailSender javaMailSender, String subject, String sender, String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }

    /**
     * Prepares the HTML message for sending
     * 
     * @param javaMailSender SMTP Client
     * @param subject Email message subject
     * @param sender Sender's email
     * @param template HTML template for submission
     * @param variables Variables for processing in the template
     * @param to Email that will receive the message
     * @return Processed message ready for sending
     * @throws MessagingException
     */
    public void sendHtmlMessage(JavaMailSender javaMailSender, TemplateEngine templateEngine, String subject, String sender,
                                String template,
                                Map<String, String> variables,
                                String to)
            throws MessagingException {
        MimeMessage mm = javaMailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mm, true);

        Context context = new Context();
        if (variables != null) {
            for (var entry : variables.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        mmh.setTo(to);
        mmh.setFrom(sender);
        mmh.setSubject(subject);
        mmh.setSentDate(new Date(System.currentTimeMillis()));
        mmh.setText(templateEngine.process(template, context), true);

        javaMailSender.send(mmh.getMimeMessage());
    }
}
