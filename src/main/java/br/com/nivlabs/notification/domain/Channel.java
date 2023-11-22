package br.com.nivlabs.notification.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Channel extends EntityWithCreatedAndUpdatedColumns {

    @Id
    private String uuid;
    private String name;

    public Channel() {
        super();
    }

    public Channel(String uuid, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super();
        this.uuid = uuid;
        this.name = name;
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Channel other = (Channel) obj;
        return Objects.equals(name, other.name) && Objects.equals(uuid, other.uuid);
    }

}
