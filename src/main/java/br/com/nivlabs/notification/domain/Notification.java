package br.com.nivlabs.notification.domain;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Notification extends EntityWithCreatedAndUpdatedColumns {

    @Id
    private String uuid;
    @Column(name = "ACCOUNT_UUID")
    private String accountUuid;
    private String subject;
    private String sender;
    private String receiver;

    public Notification() {
        super();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccountUuid() {
        return accountUuid;
    }

    public void setAccountUuid(String accountUuid) {
        this.accountUuid = accountUuid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountUuid, receiver, sender, subject, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Notification other = (Notification) obj;
        return Objects.equals(accountUuid, other.accountUuid) && Objects.equals(receiver, other.receiver)
                && Objects.equals(sender, other.sender) && Objects.equals(subject, other.subject) && Objects.equals(uuid, other.uuid);
    }

}
