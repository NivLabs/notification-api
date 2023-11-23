package br.com.nivlabs.notification.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.nivlabs.notification.repository.ChannelRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private ChannelRepository channelRepository;

    private final String[] postAllowedList = {};

    private final String[] getAllowedList = {
                                             "/api/status"
    };

    private final String[] permitAll = {
                                        "/swagger-ui/**",
                                        "/api-docs/**"
    };

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, postAllowedList)
                .permitAll()
                .requestMatchers(HttpMethod.GET, getAllowedList)
                .permitAll()
                .requestMatchers(permitAll)
                .permitAll()
                .anyRequest().authenticated());

        http.addFilterBefore(
                             new NotificationAuthorizationFilter(
                                     authenticationManager(authenticationConfiguration),
                                     channelRepository),
                             UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}