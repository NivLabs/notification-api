package br.com.nivlabs.notification.integration;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailIntegration {

    @Value("${nivlabs.notification.smtp.host}")
    private String host;
    @Value("${nivlabs.notification.smtp.port}")
    private Integer port;
    @Value("${nivlabs.notification.smtp.username}")
    private String userName;
    @Value("${nivlabs.notification.smtp.password}")
    private String password;

    /**
     * Create SMTP Client instance
     * 
     * @return Mail Sender
     */
    private JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSenderInstance = new JavaMailSenderImpl();
        mailSenderInstance.setHost(host);
        mailSenderInstance.setPort(port);

        mailSenderInstance.setUsername(userName);
        mailSenderInstance.setPassword(password);

        Properties props = mailSenderInstance.getJavaMailProperties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSenderInstance;
    }

    /**
     * Send a simple message via email
     * 
     * @param subject Message subject
     * @param sender Sender's email
     * @param to Email that will receive the message
     * @param text Text that will be sent by email
     */
    public void sendSimpleMessage(String subject, String sender, String to, String text) {
        final JavaMailSender javaMailSender = getMailSender();
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
     * @param subject Email message subject
     * @param sender Sender's email
     * @param template HTML template for submission
     * @param variables Variables for processing in the template
     * @param to Email that will receive the message
     * @return Processed message ready for sending
     * @throws MessagingException
     */
    public void sendHtmlMessage(TemplateEngine templateEngine, String subject, String sender,
                                String template,
                                Map<String, String> variables,
                                String to)
            throws MessagingException {
        final JavaMailSender javaMailSender = getMailSender();
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
