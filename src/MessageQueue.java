import com.rabbitmq.client.Channel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageQueue {
    private final int maxSize;
    private Queue<QueueMessage> queue;
    private final ReentrantLock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    private final Channel channel;
    private final String exchangeName = "broadcast_exchange"; // Set your exchange name


    public MessageQueue(int maxSize, Channel channel) {
        this.maxSize = maxSize;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notFull = this.lock.newCondition();
        this.notEmpty = this.lock.newCondition();
        this.channel = channel; // RabbitMQ channel

    }

    public void addMessage(QueueMessage message) throws InterruptedException {
        lock.lock();
        try {
            while (this.queue.size() == this.maxSize) {
                notFull.await(); // Block when the queue is full
            }
            this.queue.add(message);
            publishToRabbitMQ(message); // Publish message to RabbitMQ
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    private void publishToRabbitMQ(QueueMessage message) {
        try {
            String messageContent = message.getContent(); // Assuming your message has a getContent() method
            channel.basicPublish(exchangeName, "", null, messageContent.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public QueueMessage getMessageForRecipient(String recipient) throws InterruptedException {
        lock.lock();
        try {
            while (this.queue.isEmpty()) {
                notEmpty.await();
            }

            QueueMessage messageToReturn = null;
            Iterator<QueueMessage> iterator = this.queue.iterator();
            while (iterator.hasNext()) {
                QueueMessage message = iterator.next();
                if (message.getRecipient().equals(recipient)) {
                    messageToReturn = message;
                    iterator.remove();
                    notFull.signal();
                    System.out.println("Message received from recipient " + recipient + ": " + messageToReturn.getContent());
                    break;
                }
            }
            return messageToReturn;
        } finally {
            lock.unlock();
        }
    }
}