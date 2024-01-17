import java.time.Duration;

public class MessagingServerTest {

    public static void main(String[] args) {
        testTopic(7,"Romania_Topic","Countries");// Test the Topic and TopicMessage functionality
        testTopic(17,"UPT_Topic","Faculties");
        testTopic(27,"CEBP_Topic","Courses");
        System.out.println("\n\n");
        testMessageQueue(3,"Romania_Broadcast"); // Test the MessageQueue functionality
        testMessageQueue(5,"UPT_Broadcast");
        testMessageQueue(10,"CEBP_Broadcast");
    }

    private static void testTopic(Integer NoOfHashtag, String Hashtag, String TopicType) {
        MessageServer server = new MessageServer(10);
        System.out.println("TOPIC FUNCTIONALITY TEST");

        for (int i = 0; i < NoOfHashtag; i++) {
            final int messageIndex = i;
            Thread messageThread = new Thread(() -> {
                // Reducing TTL to 5 seconds for quicker expiration
                TopicMessage message = new TopicMessage("Topic message " + (messageIndex + 1) + " with hashtag <#" + Hashtag + ">.", "Header", TopicType, Duration.ofSeconds(5));
                server.getTopic().addMessage(message);
                System.out.println("Added message to topic: " + message.getContent());
            });
            messageThread.start();

            try {
                Thread.sleep(2000); // 2-second delay between message sends
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Simulate removing expired messages from the topic
        Thread expirationThread = new Thread(() -> {
            try {
                Thread.sleep(10000); // Wait 10 seconds before removing expired messages
                server.getTopic().removeExpiredMessages();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        expirationThread.start();

        // Sleep added to give time for messages to be processed and printed
        try {
            Thread.sleep(20000); // Wait for all operations to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testMessageQueue(Integer NoOfHashtag,String Hashtag) {
        MessageServer server = new MessageServer(10);

        System.out.println("PRIVATE MESSAGE FUNCTIONALITY TEST");


        for (int i = 0; i < NoOfHashtag; i++) {
            Client client = new Client(server.getMessageQueue(), server.getTopic());
            int finalI = i;
            Thread clientThread = new Thread(() -> {
                QueueMessage message = new QueueMessage("Message with hashtag <#"+Hashtag+">sent from client " + finalI, "Recipient");
                client.sendMessageToQueue(message);
            });

            clientThread.start();
        }


        new Thread(() -> {
            try {
                for (int i = 0; i < NoOfHashtag ; i++) {
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
