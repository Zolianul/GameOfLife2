import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Topic {
    private List<TopicMessage> messages;

    public Topic() {
        this.messages = new ArrayList<>();
    }

    public List<TopicMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<TopicMessage> messages) {
        this.messages = messages;
    }

    // Synchronized method to add a message to the topic
    public synchronized void addMessage(TopicMessage message) {
        this.messages.add(message);
    }

    // Synchronized method to get messages by type
    public synchronized List<TopicMessage> getMessageOfType(String type) {
        return messages.stream()
                .filter(message -> message.getType().equals(type))
                .collect(Collectors.toList());
    }

    // This could be called by a cleanup thread or some expiration policy mechanism
    public synchronized void removeExpiredMessages() {
        messages.removeIf(TopicMessage::isExpired);
    }

    // ... Other methods such as getting all messages or messages by other criteria
}

