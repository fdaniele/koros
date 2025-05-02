package it.koros.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
public class Message {

    @Id
    private String id;
    private String content;
    private LocalDateTime receivedAt;

    public Message(String content) {
        this.content = content;
        this.receivedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }
}

