import java.util.ArrayList;
import java.util.List;

public class MessagingServerTest {
    public static void main(String[] args) {
        MessageServer server = new MessageServer(10);
        List<Client> clients = new ArrayList<>(); // Create a list to store clients

        for (int i = 0; i < 50; ++i) {
            Client client = new Client(server.getTopic());
            MessageQueue clientQueue = new MessageQueue(10); // Specify the capacity for each client queue
            client.addPersonalQueue(clientQueue);
            clients.add(client); // Add the client to the list

            final int currentI = i; // Creating a final copy of i

            Thread clientThread = new Thread(() -> {
                QueueMessage message = new QueueMessage("Message from client " + currentI, "Recipient");
                client.sendMessageToPersonalQueue(message, clientQueue);
            });
            clientThread.start();
        }

        // Thread to consume messages from the client queues after 5 seconds
        Thread consumeMessages = new Thread(() -> {
            try {
                Thread.sleep(5000);

                for (Client client : clients) {
                    MessageQueue clientQueue = client.getPersonalQueues().get(0); // Assuming only one queue per client for simplicity
                    Message message = client.receiveMessageFromPersonalQueue("Recipient", clientQueue);
                    if (message != null) {
                        System.out.println("Message consumed: " + message.getContent());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumeMessages.start();
    }
}
