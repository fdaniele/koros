package it.koros.middleware.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "log_messages")
public class LogMessage {
    //Definizione del documento di log all'interno del database MongoDB
    @Id
    private String id;
    private final String originalMessage;
    private final boolean valid;
    private final LocalDateTime timestamp;

    public LogMessage(String originalMessage, boolean valid) {
        this.originalMessage = originalMessage;
        this.valid = valid;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getOriginalMessage() { return originalMessage; }
    public boolean isValid() { return valid; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
