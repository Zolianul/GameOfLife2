public class MessagingServerTest {

    public static void main(String[] args) {
        MessageServer server = new MessageServer(1); // Set queue size to 2

        // Create multiple clients (more than the queue size)
        for (int i = 0; i < 5; i++) {
            Client client = new Client(server.getMessageQueue(), server.getTopic());
            int finalI = i;
            Thread clientThread = new Thread(() -> {
                QueueMessage message = new QueueMessage("Message from client " + finalI, "Recipient");
                client.sendMessageToQueue(message);
            });

            clientThread.start();
        }

        // Optionally, add a thread to simulate consuming messages from the queue
        new Thread(() -> {
            try {
                // Simulate a delay before starting to consume messages
                Thread.sleep(5000);
                for (int i = 0; i < 5; i++) {
                    int finalI2 = i;
                    server.getMessageQueue().getMessageForRecipient("Recipient");
                    Thread.sleep(1000); // Simulate time taken to process each message
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
