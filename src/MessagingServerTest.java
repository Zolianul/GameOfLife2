import java.time.Duration;

public class MessagingServerTest {

    public static void main(String[] args) {// Test the TopicMessage the MessageQueue functionality
        testTopic(7,"Romania","Countries");
        testTopic(17,"UPT","Faculties");
        testTopic(27,"CEBP","Courses");
        testMessageQueue(3,"Romania");
        testMessageQueue(5,"UPT");
        testMessageQueue(10,"CEBP");
    }

    private static void testTopic(Integer NoOfHashtag,String Hashtag, String TopicType) {
        MessageServer server = new MessageServer(10);
        System.out.println("TOPIC FUNCTIONALITY TEST");

        for (int i = 0; i < NoOfHashtag; i++) {
            final int messageIndex = i;
            Thread messageThread = new Thread(() -> {
                TopicMessage message = new TopicMessage("Topic message " + (messageIndex + 1)+"with hashtag <#"+Hashtag+">.", "Header", TopicType, Duration.ofSeconds(20));
                server.getTopic().addMessage(message);
                System.out.println("Added message to topic: " + message.getContent());
            });
            messageThread.start();


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Simulate removing expired messages from the topic
        Thread expirationThread = new Thread(() -> {
            try {
                Thread.sleep(5000);
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
