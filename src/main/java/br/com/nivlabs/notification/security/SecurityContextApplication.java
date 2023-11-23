package br.com.nivlabs.notification.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.nivlabs.notification.exception.HttpException;

public class SecurityContextApplication {
    public static final String PREFIX_KEY = "&*($$)%#";

    public static UserDetailsCustom getAuthenticatedUser() {
        try {
            return (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, "Unauthenticated user, please login.");
        }
    }

    public static String getChannelUuid() {
        return getAuthenticatedUser().getUuid();
    }

}
