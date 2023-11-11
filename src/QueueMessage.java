public class QueueMessage extends Message {
    private String recipient;

    public QueueMessage(String content, String recipient) {
        super(content, recipient);
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
