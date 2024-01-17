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

    private void initializeRabbitMQ() {//Method for initializing the RabbitMQ server
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("my_user");
            factory.setPassword("my_password");

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

}
