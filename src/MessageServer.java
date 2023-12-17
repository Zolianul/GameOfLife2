import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class MessageServer {
    private final MessageQueue messageQueue;
    private final Topic topic;
    private Connection connection;
    private Channel channel;


    public MessageServer(int queueLimit) {

        initializeRabbitMQ(); // Initialize RabbitMQ connection

        this.messageQueue = new MessageQueue(queueLimit, channel);// Initialize MessageQueue and Topic with the RabbitMQ channel
        this.topic = new Topic(channel);
    }

    private void initializeRabbitMQ() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Set RabbitMQ server address
            factory.setUsername("guest");// Set username and password
            factory.setPassword("guest");

            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
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


    public void stop() {       // close RabbitMQ connection when the server is stopped
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
