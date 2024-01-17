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

}