import java.util.List;

public class Client {
    private MessageQueue messageQueue;
    private Topic topic;

    // Assume that messageQueue and topic are passed to the client, simulating the server's resources
    public Client(MessageQueue messageQueue, Topic topic) {
        this.messageQueue = messageQueue;
        this.topic = topic;
    }


    public void sendMessageToQueue(QueueMessage queueMessage) {
        try {
            // Simulate sending a message to the queue
            messageQueue.addMessage(queueMessage);
            System.out.println("Client sent message to queue: " + queueMessage.getContent());
        } catch (InterruptedException e) {
            // Handle the interrupted exception
            System.out.println("Thread was interrupted while sending message to queue");
            // Optionally, you can re-interrupt the thread
            Thread.currentThread().interrupt();
        }
    }


    public void sendMessageToTopic(TopicMessage topicMessage) {
        // Simulate sending a message to the topic
        topic.addMessage(topicMessage);
        System.out.println("Client sent message to topic: " + topicMessage.getContent());
    }

    public Message receiveMessageFromQueue(String recipient) throws InterruptedException {
        // Simulate receiving a message from the queue
        QueueMessage message = messageQueue.getMessageForRecipient(recipient);
        if (message != null) {
            System.out.println("Client received message from queue: " + message.getContent());
        }
        return message;
    }

    public TopicMessage receiveMessageFromTopic(String type) {
        // Simulate receiving messages from the topic
        List<TopicMessage> messages = topic.getMessageOfType(type);
        TopicMessage message = null;

        if (!messages.isEmpty()) {
            // Assuming the strategy is to get the first available message
            message = messages.get(0);
            System.out.println("Client received message from topic: " + message.getContent());
        }

        return message;
    }
}