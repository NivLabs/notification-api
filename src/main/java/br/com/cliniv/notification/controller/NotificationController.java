package br.com.cliniv.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cliniv.notification.domain.dto.SendNotificationDTO;
import br.com.cliniv.notification.service.NotificationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping("/{channel}")
    public ResponseEntity<SendNotificationDTO> send(@PathVariable("channel") String channel,
                                                    @Valid @RequestBody SendNotificationDTO request) {
        return ResponseEntity.ok(service.send(channel, request));
    }
}
