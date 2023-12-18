import com.rabbitmq.client.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Topic {
    private List<TopicMessage> messages = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private Channel channel;
    private final String exchangeName= "topic_exchange"; // RabbitMQ exchange name

    private final String routingKeyBase = "topic.";
    public Topic(Channel channel) {
        this.channel = channel;
    }

    public void addMessage(TopicMessage message) {
        lock.lock();
        try {
            messages.add(message);
            publishToRabbitMQ(message); // Publish message to RabbitMQ
        } finally {
            lock.unlock();
        }
    }

    private void publishToRabbitMQ(TopicMessage message) {
        try {
            if (!message.isExpired()) {
                String messageContent = message.getContent();
                String routingKey = routingKeyBase+message.getType(); // Using message type as routing key
                channel.basicPublish(exchangeName, routingKey, null, messageContent.getBytes());
                System.out.println("Published message to topic: " + routingKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        long expiredCount = 0;
        try {
            // Count the expired messages
            expiredCount = messages.stream().filter(TopicMessage::isExpired).count();
            messages.removeIf(TopicMessage::isExpired);

            // Print the number of messages removed
            System.out.println(expiredCount + " expired messages removed from the topic.");
        } finally {
            lock.unlock();
        }
    }
}