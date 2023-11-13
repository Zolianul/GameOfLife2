public class MessageServer {
    private final MessageQueue messageQueue;
    private final Topic topic;

    public MessageServer(int queueLimit) {
        this.messageQueue = new MessageQueue(queueLimit);
        this.topic = new Topic();
    }
    public MessageQueue getMessageQueue() {
        return messageQueue;
    }

    public Topic getTopic() {
        return topic;
    }

}