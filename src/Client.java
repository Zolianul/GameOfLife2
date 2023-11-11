import java.util.ArrayList;
import java.util.List;

public class Client {
    private List<MessageQueue> personalQueues;
    private Topic topic;

    public Client(Topic topic) {
        this.personalQueues = new ArrayList<>();
        this.topic = topic;
    }

    public void addPersonalQueue(MessageQueue messageQueue) {
        this.personalQueues.add(messageQueue);
    }

    public void sendMessageToPersonalQueue(QueueMessage queueMessage, MessageQueue personalQueue) {
        try {
            personalQueue.addMessage(queueMessage);
            System.out.println("Client sent message to personal queue: " + queueMessage.getContent());
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted while sending message to personal queue");
            Thread.currentThread().interrupt();
        }
    }

    public Message receiveMessageFromPersonalQueue(String recipient, MessageQueue personalQueue) {
        QueueMessage message = personalQueue.getMessageForRecipient(recipient);
        if (message != null) {
            System.out.println("Client received message from personal queue: " + message.getContent());
        }
        return message;
    }

    public List<MessageQueue> getPersonalQueues() {
        return personalQueues;
    }
}
