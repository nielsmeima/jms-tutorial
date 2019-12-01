import javax.jms.*;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {

    public static void main(String[] args) throws Exception
    {
        Random rand = new Random();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        ArrayList<String> topics = new ArrayList<>();
        topics.add("Channels");
        topics.add("Routing");
        topics.add("JMS concepts");
        topics.add("Complex Event Processing");

        while (true)
        {
            Date date = Calendar.getInstance().getTime();
            String dateString = dateFormat.format(date);

            int randomIndex = rand.nextInt(topics.size());
            String topic = topics.get(randomIndex);

            System.out.printf("[%s] %s\n", topic, dateString);

            sendMessage(topic, dateString);
            Thread.sleep(1000);
        }

    }

    public static void sendMessage(String topic, String body) throws NamingException
    {
        InitialContext namingContext = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("ConnectionFactory");

//        Topic destination = (Topic) namingContext.lookup("topics/TutorialTopic");
        Queue destination = (Queue) namingContext.lookup("queues/TutorialQueue");

        try (JMSContext context = connectionFactory.createContext("admin", "admin", JMSContext.AUTO_ACKNOWLEDGE))
        {

            context
                .createProducer()
                .setDeliveryMode(DeliveryMode.PERSISTENT)
                .setProperty("topic", topic)
                .send(destination, body);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}