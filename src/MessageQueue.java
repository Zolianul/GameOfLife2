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

    public MessageQueue(int maxSize) {
        this.maxSize = maxSize;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notFull = this.lock.newCondition();
        this.notEmpty = this.lock.newCondition();
    }

    public void addMessage(QueueMessage message) throws InterruptedException {
        lock.lock();
        try {
            while (this.queue.size() == this.maxSize) {
                notFull.await(); // Block when the queue is full
            }
            this.queue.add(message);
            notEmpty.signal(); // Signal that the queue is not empty
        } finally {
            lock.unlock();
        }
    }

    public QueueMessage getMessageForRecipient(String recipient) throws InterruptedException {
        lock.lock();
        try {
            while (this.queue.isEmpty()) {
                notEmpty.await(); // Block when the queue is empty
            }

            QueueMessage messageToReturn = null;
            Iterator<QueueMessage> iterator = this.queue.iterator();
            while (iterator.hasNext()) {
                QueueMessage message = iterator.next();
                if (message.getRecipient().equals(recipient)) {
                    messageToReturn = message;
                    iterator.remove();
                    notFull.signal(); // Signal that the queue is not full
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