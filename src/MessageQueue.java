import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
    private final int maxSize;
    private Queue<QueueMessage> queue;

    public MessageQueue(int maxSize) {
        this.maxSize = maxSize;
        this.queue = new LinkedList<>();
    }

    public synchronized void addMessage(QueueMessage message) throws InterruptedException {
        while (queue.size() == maxSize) {
            wait();  // Wait until there is space in the queue
        }
        queue.add(message);
        notifyAll();  // Notify any waiting threads that a new message has been added
    }

    public synchronized QueueMessage getMessageForRecipient(String recipient) {
        QueueMessage messageToReturn = null;
        for (QueueMessage message : queue) {
            if (message.getRecipient().equals(recipient)) {
                messageToReturn = message;
                break;
            }
        }
        if (messageToReturn != null) {
            System.out.println("Message received from recipient "+recipient);
            queue.remove(messageToReturn);
            notifyAll();  // Notify any waiting threads that space is now available in the queue
        }
        return messageToReturn;
    }
}
