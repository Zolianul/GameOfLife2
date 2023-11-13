import java.util.List;

public class Client {
    private MessageQueue messageQueue;
    private Topic topic;

    public Client(MessageQueue messageQueue, Topic topic) {
        this.messageQueue = messageQueue;
        this.topic = topic;
    }


    public void sendMessageToQueue(QueueMessage queueMessage) {
        try {
            messageQueue.addMessage(queueMessage);
            System.out.println("Client sent message to queue: " + queueMessage.getContent());
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted while sending message to queue");
            Thread.currentThread().interrupt();
        }
    }


    public void sendMessageToTopic(TopicMessage topicMessage) {
        topic.addMessage(topicMessage);
        System.out.println("Client sent message to topic: " + topicMessage.getContent());
    }

    public Message receiveMessageFromQueue(String recipient) throws InterruptedException {
        QueueMessage message = messageQueue.getMessageForRecipient(recipient);
        if (message != null) {
            System.out.println("Client received message from queue: " + message.getContent());
        }
        return message;
    }

    public TopicMessage receiveMessageFromTopic(String type) {
        List<TopicMessage> messages = topic.getMessageOfType(type);
        TopicMessage message = null;

        if (!messages.isEmpty()) {
            message = messages.get(0);
            System.out.println("Client received message from topic: " + message.getContent());
        }

        return message;
    }
}