package jmschat;

import java.util.Observable;
import javax.jms.ConnectionFactory;
import javax.jms.Topic;
/**
 * JMSChat.java
 *
 * Oggetto chat che si occupa di creare publisher e subscribers
 *
 * @author Luca Lanziani
 * @author Federico Spini
 * @version 0.1
 */
public class JMSChat {
	private JMSChatSubscriber jmschatsubscriber;
	private JMSChatPublisher jmschatpublisher;
	private String nickname;

        /**
         * Ritorna un oggetto Osservable relativo ai messaggi in arrivo a cui far agganciare un Observer
         * @return ritorna un Observable
         */
        public Observable getObservableReceivedMessage() {
            return jmschatsubscriber;
        }

        /**
         * Getter per il nickname
         * @return Ritorna il nick name in uso
         */
        public String getNickname() {
            return nickname;
        }

        /**
         * Costruttore per la chat
         * @param nickname il nickname da utilizzare nella chat
         * @param topic il topic a cui ci si vuole agganciare
         * @param connectionFactory la connection factory da utilizzare
         */
	public JMSChat(String nickname, Topic topic, ConnectionFactory connectionFactory){
		/* crea il subscriber */
		this.jmschatsubscriber = new JMSChatSubscriber(topic, connectionFactory);
		
		
		/* crea il publisher*/
		this.jmschatpublisher = new JMSChatPublisher(nickname, topic, connectionFactory);
		
		this.nickname = nickname;
		
	}

        /**
         * si occupa di instaurare la connessione sia del subscriber sia del publisher
         * mandando anche il messaggio di connessione effettuata sulla chat.
         */
	public void connect(){
		jmschatsubscriber.connect();
		jmschatsubscriber.startReceiveMessages();
		jmschatpublisher.connect();
		jmschatpublisher.sendMessage("mi sono appena connesso");
		//MANDA UN MESSAGGIO DEL TIPO: utente: mi sono connesso
	}

        /**
         * Manda il messaggio di disconnessione e si occupa di disconnettere prima il publisher e poi
         * il subscriber
         */
	public void disconnect(){
		//MANDA UN MESSAGGIO DEL TIPO: utente: mi sono disconnesso
		jmschatpublisher.sendMessage("mi disconnetto");
		jmschatpublisher.disconnect();
		jmschatsubscriber.stopReceiveMessages();
		jmschatsubscriber.disconnect();
	}

        /**
         * Metodo di invio messaggio
         * @param msg rappresenta il messaggio da mandare in chat
         */
	public synchronized void sendMessage(String msg) {
		// TODO Auto-generated method stub
		jmschatpublisher.sendMessage(msg);

	}
	
}
