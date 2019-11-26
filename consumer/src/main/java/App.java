import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        receiveMessage();
    }

    public static void receiveMessage() throws NamingException {
        InitialContext namingContext = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("ConnectionFactory");

        Queue testQueue = (Queue) namingContext.lookup("queues/TutorialQueue");

        try (JMSContext context=connectionFactory.createContext("admin", "admin")) {
            JMSConsumer consumer = (JMSConsumer) context.createConsumer(testQueue);

            consumeAsync(consumer);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private static void consumeSync(JMSConsumer consumer) {
        String body = consumer.receiveBody(String.class);
        System.out.println(body);
    }

    private static void consumeAsync(JMSConsumer consumer) {
        MessageListener messageListener = new TutorialMessageListener();
        consumer.setMessageListener(messageListener);

        while(true) {}
    }

}


class TutorialMessageListener implements MessageListener {

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            System.out.println(text);
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }
}