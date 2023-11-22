package br.com.nivlabs.notification.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONFIG_SMTP")
public class SMTPSettings extends EntityWithCreatedAndUpdatedColumns {

    @Id
    @Column(name = "CHANNEL_UUID")
    private String channelUuid;
    private String host;
    private int port;
    @Column(name = "USERNAME")
    private String userName;
    private String password;

    public SMTPSettings() {
        super();
    }

    public SMTPSettings(String channelUuid, String host, int port, String userName, String password, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        super();
        this.channelUuid = channelUuid;
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public String getChannelUuid() {
        return channelUuid;
    }

    public void setChannelUuid(String channelUuid) {
        this.channelUuid = channelUuid;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelUuid, host, password, port, userName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SMTPSettings other = (SMTPSettings) obj;
        return Objects.equals(channelUuid, other.channelUuid) && Objects.equals(host, other.host)
                && Objects.equals(password, other.password) && port == other.port && Objects.equals(userName, other.userName);
    }

}
