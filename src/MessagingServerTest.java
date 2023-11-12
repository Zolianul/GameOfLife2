import java.time.Duration;

public class MessagingServerTest {
    public static void main(String[] args) {
        testTopic(); // Test the Topic and TopicMessage functionality
        testMessageQueue(); // Test the MessageQueue functionality

    }

    private static void testTopic() {
        MessageServer server = new MessageServer(10);
        System.out.println("TOPIC FUNCTIONALITY TEST");

        for (int i = 0; i < 100; i++) {
            final int messageIndex = i;
            Thread messageThread = new Thread(() -> {
                TopicMessage message = new TopicMessage("Topic message " + (messageIndex + 1), "Header", "Type", Duration.ofSeconds(10));
                server.getTopic().addMessage(message);
                System.out.println("Added message to topic: " + message.getContent());
            });
            messageThread.start();
        }

        // Simulate removing expired messages from the topic
        Thread expirationThread = new Thread(() -> {
            try {
                Thread.sleep(5000); // Adding a sleep duration for the test
                server.getTopic().removeExpiredMessages();
                System.out.println("Expired messages removed from the topic.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        expirationThread.start();

        // Sleep added to give time for messages to be processed and printed
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testMessageQueue() {
        MessageServer server = new MessageServer(10);

        System.out.println("PRIVATE MESSAGE FUNCTIONALITY TEST");

        // Create a limited number of clients
        for (int i = 0; i < 100; i++) {
            Client client = new Client(server.getMessageQueue(), server.getTopic());
            int finalI = i;
            Thread clientThread = new Thread(() -> {
                QueueMessage message = new QueueMessage("Message from client " + finalI, "Recipient");
                client.sendMessageToQueue(message);
            });

            clientThread.start();
        }

        // Add a thread to consume messages from the queue
        new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    server.getMessageQueue().getMessageForRecipient("Recipient");
                    Thread.sleep(1000); // Simulate time taken to process each message
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // Sleep added to give time for messages to be processed and printed
        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
