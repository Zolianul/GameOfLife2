import java.time.Instant;
import java.time.Duration;
import java.util.concurrent.locks.ReentrantLock;

public class TopicMessage extends Message {
    private final String type;
    private final Instant timestamp;//current time when sending a message
    private final Duration ttl; // Time to live for the message

    public TopicMessage(String content,String header, String type, Duration ttl) {
        super(content,header);
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
    private final ReentrantLock lock = new ReentrantLock();


    public String getContent() {
        lock.lock();
        try {
            return super.getContent();
        } finally {
            lock.unlock();
        }
    }

    public void setContent(String content) {
        lock.lock();
        try {
            super.setContent(content);
        } finally {
            lock.unlock();
        }
    }
}
