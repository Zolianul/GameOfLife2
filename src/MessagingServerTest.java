import java.time.Duration;

public class MessagingServerTest {
    public static void main(String[] args) {
        testTopic();
        testMessageQueue();

    }

    private static void testTopic() {
        MessageServer server = new MessageServer(10);
        System.out.println("TOPIC FUNCTIONALITY TEST");

        for (int i = 0; i < 20; i++) {
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


        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testMessageQueue() {
        MessageServer server = new MessageServer(10);

        System.out.println("PRIVATE MESSAGE FUNCTIONALITY TEST");


        for (int i = 0; i < 20; i++) {
            Client client = new Client(server.getMessageQueue(), server.getTopic());
            int finalI = i;
            Thread clientThread = new Thread(() -> {
                QueueMessage message = new QueueMessage("Message from client " + finalI, "Recipient");
                client.sendMessageToQueue(message);
            });

            clientThread.start();
        }


        new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    server.getMessageQueue().getMessageForRecipient("Recipient");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();


        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
