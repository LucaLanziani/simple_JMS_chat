package jmschat;

import java.util.Observable;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

/**
 * JMSChatSubscriber.java
 *
 * Il publisher si aggancia al topic del provider JMS e si preoccupa
 * di ricevere i messaggi, tramite il TextListener, 
 * nel particolare caso la ricezione è asincrona.
 *
 *
 * @author Luca Lanziani
 * @author Federico Spini
 */
public class JMSChatSubscriber extends Observable {

	private Destination destination;
	private ConnectionFactory connectionFactory;
        private boolean receiving=false;
	/* connessione jms */
	private Connection connection = null;
	/* sessione jms */
	private Session session = null;


        /**
         * Costruttore della classe JMSChatSubscriber
         * @param topic topic a cui ci si vuole agganciare
         * @param connectionFactory fornisce la connessione
         */
        protected JMSChatSubscriber(Topic topic, ConnectionFactory connectionFactory) {
		this.destination = topic;
		this.connectionFactory = connectionFactory;
	}
        
	/** Apre la connessione alla destinazione JMS. */
	protected void connect() {
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, // non transazionale
					Session.AUTO_ACKNOWLEDGE);
		} catch (Exception e) {
			System.out.println("Connection problem: " + e.toString());
			disconnect();
		}
	}

	/** Si disconnette dalla destinazione JMS. */
	protected void disconnect() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (JMSException e) {
				System.out.println("Disconnection problem: " + e.toString());
			}
		}
	}

	/** Riceve messaggi dalla destinazione finquando ricevi vale true. */
	protected void startReceiveMessages() {
		/* riceve finchè ricevi vale true */
		if (!this.receiving) {
                    try {
                            MessageConsumer messageConsumer = session.createConsumer(destination);
                            TextListener textListener = new TextListener(this);
                            messageConsumer.setMessageListener(textListener);
                            /* avvia la ricezione di messaggi per la connessione */
                            connection.start();
                            this.receiving=true;

                    } catch (JMSException e) {
                            System.out.println("JMSChatSubscriber: " + e.toString());
                            System.exit(1);
                    }
                }
	}

        /**
         * Interrompe la connessione con la destinazione JMS
         */
        protected void stopReceiveMessages() {
		if (this.receiving) {
                    try {
                            connection.stop();
                            this.receiving=false;
                    } catch (JMSException e) {
                            System.out.println("JMSChatSubscriber: " + e.toString());
                            System.exit(1);
                    }
                }
	}
	
        /** Elabora un messaggio ricevuto.
         * @param msg messaggio ricevuto
         */
        protected synchronized void messageReceived(String msg) {
                setChanged();
                notifyObservers(msg);
                System.out.println(msg);
        }

}
