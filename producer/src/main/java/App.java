import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        sendMessageNew("Hello!");
    }

    public static void sendMessageNew(String body) throws NamingException {
        InitialContext namingContext = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("ConnectionFactory");

        Queue testQueue = (Queue) namingContext.lookup("queues/TutorialQueue");

        try (JMSContext context=connectionFactory.createContext("admin", "admin")) {

            context.createProducer().send(testQueue, body);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}