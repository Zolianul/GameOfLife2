import java.time.Instant;
import java.time.Duration;

public class TopicMessage extends Message {
    private final String type;
    private final Instant timestamp;
    private final Duration ttl; // Time to live for the message

    public TopicMessage(String content,String header, String type, Duration ttl) {
        super(content,header); // Assuming the superclass Message handles content
        this.type = type;
        this.timestamp = Instant.now();
        this.ttl = ttl;
    }

    // Checks if the message is expired based on its TTL
    public boolean isExpired() {
        return Instant.now().isAfter(timestamp.plus(ttl));
    }

    // Getters
    public String getType() {
        return type;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Duration getTtl() {
        return ttl;
    }
}
