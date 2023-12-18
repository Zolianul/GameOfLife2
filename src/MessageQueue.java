import com.rabbitmq.client.Channel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageQueue {
    private final int maxSize; //Maximum nr. of msg allowed in the queue
    private Queue<QueueMessage> queue; // The FIFO queue
    private final ReentrantLock lock;  //For concurrency control
    private final Condition notFull;  //Condition for checking if the queue is not full
    private final Condition notEmpty; //Condition for checking if the queue is not empty
    private final Channel channel;
    private final String exchangeName = "broadcast_exchange"; // RabbitMQ exchange name


    public MessageQueue(int maxSize, Channel channel) {
        this.maxSize = maxSize;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notFull = this.lock.newCondition();
        this.notEmpty = this.lock.newCondition();
        this.channel = channel; // RabbitMQ channel

    }

    public void addMessage(QueueMessage message) throws InterruptedException { // Method for adding a message to queue and publishingg it to RabbitMq
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

    private void publishToRabbitMQ(QueueMessage message) {// Method for publishing the messages to the server. Sending the msg as strings
        try {
            String messageContent = message.getContent();
            channel.basicPublish(exchangeName, "", null, messageContent.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public QueueMessage getMessageForRecipient(String recipient) throws InterruptedException { // Method for receiving msg from a user
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