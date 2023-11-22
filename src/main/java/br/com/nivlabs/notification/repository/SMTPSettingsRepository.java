package br.com.nivlabs.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.nivlabs.notification.domain.SMTPSettings;

@Repository
public interface SMTPSettingsRepository extends JpaRepository<SMTPSettings, String> {

}
