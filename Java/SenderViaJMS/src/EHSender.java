import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EHSender {	
	public static void main(String[] args)throws 
		NamingException, JMSException, IOException, InterruptedException
	{
		// Configure JNDI environment
		Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.amqp_1_0.jms.jndi.PropertiesFileInitialContextFactory");
        env.put(Context.PROVIDER_URL, "D:\\Pilot\\Java\\workspace2\\Java EventHub Demo\\conf\\servicebus.properties");
        Context context = new InitialContext(env);

        ConnectionFactory cf = (ConnectionFactory)context.lookup("SBCF");

        Destination queue = (Destination)context.lookup("EventHub");

        //Create Connection
        Connection connection = cf.createConnection();

        // Create sender-side Session and Message Producer
        Session sendSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer sender = sendSession.createProducer(queue);

        System.out.println("Press Ctrl-C to stop the sender process");
        System.out.println("Press Enter to start now");
        BufferedReader commandLine = new java.io.BufferedReader(
        new InputStreamReader(System.in));
        commandLine.readLine();

        while (true) {
        	sendBytesMessage(sendSession, sender);
        	Thread.sleep(200);
        }
    }

	private static void sendBytesMessage(Session sendSession, MessageProducer sender)throws 
		JMSException, UnsupportedEncodingException
	{
		BytesMessage message = sendSession.createBytesMessage();
		message.writeBytes("Test AMQP message from JMS".getBytes("UTF-8"));
		sender.send(message);
		System.out.println("Sent message");
	}
}
