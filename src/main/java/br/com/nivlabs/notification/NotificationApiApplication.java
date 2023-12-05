package br.com.nivlabs.notification;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableEurekaClient
public class NotificationApiApplication {
    private static Logger logger = LoggerFactory.getLogger(NotificationApiApplication.class);

    @Value(value = "${nivlabs.application.zoneId}")
    public String zoneId;

    @PostConstruct
    public void init() {
        logger.info("Setting Spring Boot SetTimeZone to :: {}", zoneId);
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
    }

    public static void main(String[] args) {
        SpringApplication.run(NotificationApiApplication.class, args);
    }
}
