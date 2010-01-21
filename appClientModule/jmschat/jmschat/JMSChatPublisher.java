package jmschat;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 * JMSChatPublisher.java
 *
 * Il publisher si aggancia al topic del provider JMS e si preoccupa
 * dell'invio dei messaggi
 *
 * @author Luca Lanziani
 * @author Federico Spini
 */

public class JMSChatPublisher{
	
    private Destination destination;
    private ConnectionFactory connectionFactory;
    private MessageProducer messageProducer = null;
    /* connessione jms */
    private Connection connection = null;
    /* sessione jms */
    private Session session = null;

    private String nickname= null;
    /**
     * Costruttore per il publisher, loggetto che si occupa di publicare i messaggi in chat
     * @param nickname nome utente utilizzato nella chat
     * @param topic topic a cui si deve connettere il publisher
     * @param connectionFactory connectionfactory da utilizzare
     */
    protected JMSChatPublisher(String nickname, Topic topic,
			ConnectionFactory connectionFactory) {
            this.destination=topic;
            this.connectionFactory=connectionFactory;
            this.nickname=nickname;
    }
	
    /**
     * Si occupa di instaurare la connessione
     */
    protected void connect() {
        try {
            this.connection = connectionFactory.createConnection();
            this.session = connection.createSession(
                                false, // non transazionale
				Session.AUTO_ACKNOWLEDGE);
            this.messageProducer = session.createProducer(destination);
        } catch (Exception e) {
            System.out.println("Connection problem: " + e.toString());
            disconnect();
        }
    }

    /** Si disconnette dalla destinazione JMS. */
    protected void disconnect() {
        if (connection != null) {
            try {
                this.messageProducer.close();
                this.session.close();
                this.connection.close();
                this.messageProducer =null;
                this.session=null;
                this.connection = null;
            } catch (JMSException e) {
                System.out.println("Disconnection problem: " + e.toString());
            }
        }
    }
    
    /**
     * Invia un messaggio di testo text alla destinazione
     * @param text il messaggio da inviare
     */
    protected void sendMessage(String text) {
        try {
            TextMessage message = session.createTextMessage();
            message.setText(nickname+": "+text);
            System.out.println("Sending message: " + message.getText());
            messageProducer.send(message);
	} catch (JMSException e) {
	    System.out.println("JMSChatPublisher: " + e.toString());
            System.exit(1);
        }
    }
}
