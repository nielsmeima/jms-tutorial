import javax.jms.*;
import javax.naming.InitialContext;

public class App
{

    public static void main(String[] args)
    {
        Thread t1 = new Thread(new Receiver("Thread #1"));
        Thread t2 = new Thread(new Receiver("Thread #2"));

        t1.start();
        t2.start();

        while (true) {}
    }
}


class Receiver implements Runnable
{
    String topic;

    public Receiver(String topic)
    {
        this.topic = topic;
    }


    public void run()
    {
        receiveMessage();
    }

    private void receiveMessage()
    {
        try
        {
            InitialContext namingContext = new InitialContext();

            ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("ConnectionFactory");

//          Topic destination = (Topic) namingContext.lookup("topics/TutorialTopic");
            Queue destination = (Queue) namingContext.lookup("queues/TutorialQueue");

            try (JMSContext context = connectionFactory.createContext("admin", "admin", JMSContext.AUTO_ACKNOWLEDGE))
            {
                JMSConsumer consumer = context.createConsumer(destination);
//                JMSConsumer consumer = context.createConsumer(destination, String.format("topic = '%s'", this.topic));

                consumeAsync(consumer);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void consumeSync(JMSConsumer consumer)
    {
        String body = consumer.receiveBody(String.class);
        System.out.println(String.format("[%s] %s", this.topic, body));
    }

    private void consumeAsync(JMSConsumer consumer)
    {
        MessageListener messageListener = new TutorialMessageListener(this.topic);
        consumer.setMessageListener(messageListener);

        while(true) {}
    }
}


class TutorialMessageListener implements MessageListener
{
    String topic;

    public TutorialMessageListener(String topic)
    {
        this.topic = topic;
    }

    public void onMessage(Message message)
    {
        TextMessage textMessage = (TextMessage) message;
        try
        {
            String text = textMessage.getText();
            System.out.println(String.format("[%s] %s", this.topic, text));
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
}