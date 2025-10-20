package com.test.test_1.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "messages",
        indexes = {
                @Index(name = "idx_messages_uuid", columnList = "uuid"),
                @Index(name = "idx_messages_created_at", columnList = "createdAt DESC")
        }
)
public class MessageEntity extends BaseEntity {

    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "text", nullable = false, length = 256)
    private String text;

    public MessageEntity() {}

    public MessageEntity(UUID uuid, String text) {
        this.uuid = uuid;
        this.text = text;
    }

    private Object getBusinessKey() {
        return uuid != null ? uuid.toString() + ":" + text : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MessageEntity that = (MessageEntity) o;

        if (isTransient() && that.isTransient()) {
            return Objects.equals(getBusinessKey(), that.getBusinessKey());
        }

        return true;
    }

    @Override
    public int hashCode() {
        if (isTransient()) {
            return Objects.hash(getBusinessKey());
        }
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + getId() +
                ", uuid=" + uuid +
                ", text='" + text + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }

    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}