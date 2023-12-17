import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.TimeoutException;

public class Viral {
    private Connection connection = null;
    private Channel channel = null;
    private Map<String, Integer> broadcastHashtags = null;
    private Map<String, Integer> allTopicHashtags = null;

    public Viral() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");

            connection = factory.newConnection();
            channel = connection.createChannel();

            broadcastHashtags = new HashMap<>();
            allTopicHashtags = new HashMap<>();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    public void startListening() throws IOException {
        String broadcastExchangeName = "broadcast_exchange";
        String topicExchangeName = "topic_exchange";

        String broadcastQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(broadcastQueueName, broadcastExchangeName, "");

        channel.exchangeDeclare(broadcastExchangeName, BuiltinExchangeType.FANOUT,true);
        String topicQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(topicQueueName, topicExchangeName, "");

        channel.exchangeDeclare(topicExchangeName, BuiltinExchangeType.FANOUT,true);

        Consumer broadcastConsumer = createBroadcastConsumer();
        Consumer topicConsumer = createTopicConsumer();

        channel.basicConsume(broadcastQueueName, true, broadcastConsumer);
        channel.basicConsume(topicQueueName, true, topicConsumer);
    }

    private Consumer createBroadcastConsumer() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                processHashtags(message, broadcastHashtags);
            }
        };
    }

    private Consumer createTopicConsumer() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                processHashtags(message, allTopicHashtags);
            }
        };
    }

    private void processHashtags(String message, Map<String, Integer> hashtagMap) {
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String hashtag = matcher.group(1);
            hashtagMap.put(hashtag, hashtagMap.getOrDefault(hashtag, 0) + 1);
        }
    }

    public void displayTrendingHashtags() {
        System.out.println("Trending Hashtags from Broadcast Messages:");
        displayHashtags(broadcastHashtags);

        System.out.println("Trending Hashtags from All Topic Messages:");
        displayHashtags(allTopicHashtags);
    }

    private void displayHashtags(Map<String, Integer> hashtagMap) {
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(hashtagMap.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (Map.Entry<String, Integer> entry : sortedList) {
            System.out.println("#" + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("-----------");
    }

    public void closeConnection() throws IOException, TimeoutException {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            Viral viral = new Viral();
            viral.startListening();

            // Allow the service to listen for messages for some time
            Thread.sleep(10000);
            for (int i = 0; i < 50; i++){
            viral.displayTrendingHashtags();
                System.out.println("-----------\n\n");
                Thread.sleep(5000);}
            viral.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
