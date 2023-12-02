import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class MessageServer {
    private final MessageQueue messageQueue;
    private final Topic topic;
    private Connection connection;
    private Channel channel;


    public MessageServer(int queueLimit) {
        // Initialize RabbitMQ connection
        initializeRabbitMQ();

        // Initialize MessageQueue and Topic with the RabbitMQ channel
        this.messageQueue = new MessageQueue(queueLimit, channel);
        this.topic = new Topic(channel);
    }

    private void initializeRabbitMQ() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Set to your RabbitMQ server address
            // Optionally set username and password
            factory.setUsername("guest");
            factory.setPassword("guest");

            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            // Here you can declare exchanges and queues as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }

    public Topic getTopic() {
        return topic;
    }

    // Ensure to close RabbitMQ connection when the server is stopped
    public void stop() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
