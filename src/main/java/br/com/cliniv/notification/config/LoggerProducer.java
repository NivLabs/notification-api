package br.com.cliniv.notification.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerProducer {

    @Bean
    Logger getLogger(InjectionPoint p) {
        return LoggerFactory.getLogger(p.getClass().getCanonicalName());
    }
}