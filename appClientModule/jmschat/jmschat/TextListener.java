/*
 * TextListener.java
 *
 *
 * @author cabibbo
 */

package jmschat;

import javax.jms.*;

public class TextListener implements MessageListener {
    
    /* consumer del TextListener */ 
    JMSChatSubscriber jmschatsubscriber; 
    
    /** Crea un nuovo TextListener per il consumer c.
     * @param jmschatsubscriber la classe che rappresenta il subscriber
     */
    protected TextListener(JMSChatSubscriber jmschatsubscriber) {
        this.jmschatsubscriber = jmschatsubscriber; 
    }

    /** Riceve un messaggio.
     * @param m messaggio ricevuto dal TextListener
     */
    @Override
    public void onMessage(Message m) {
    	/* ricevuto un messaggio, ne delega l'elaborazione 
    	 * al suo consumer */
    	if (m instanceof TextMessage) {
    		TextMessage  message = (TextMessage) m;
    		try {
    			jmschatsubscriber.messageReceived(message.getText()); 
    		} catch (JMSException e) {
    			System.out.println("TextListener.onMessage(): " + e.toString());
    		}
    	} 
    }

}
