import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Topic {
    private List<TopicMessage> messages = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public void addMessage(TopicMessage message) {
        lock.lock();
        try {
            messages.add(message);
        } finally {
            lock.unlock();
        }
    }

    public List<TopicMessage> getMessageOfType(String type) {
        lock.lock();
        try {
            List<TopicMessage> filteredMessages = new ArrayList<>();
            for (TopicMessage message : messages) {
                if (message.getType().equals(type)) {
                    filteredMessages.add(message);
                }
            }
            return filteredMessages;
        } finally {
            lock.unlock();
        }
    }

    public void removeExpiredMessages() {
        lock.lock();
        try {
            messages.removeIf(TopicMessage::isExpired);
        } finally {
            lock.unlock();
        }
    }
}