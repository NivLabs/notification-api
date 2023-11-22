package br.com.nivlabs.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationApiApplication {

    public static final String AMERICA_SAO_PAULO = "America/Sao_Paulo";

    public static void main(String[] args) {
        SpringApplication.run(NotificationApiApplication.class, args);
    }
}
