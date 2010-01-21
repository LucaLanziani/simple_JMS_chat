
package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import jmschat.JMSChat;

/**
 * RandomMessage.java
 *
 * Crea un thread che si occupa di mandare sulla chat un numero, specificato parametricamente,
 * di messaggi ad intervalli di tempo random
 *
 * @author Luca Lanziani
 * @author Federico Spini
 */
public class RandomMessage extends Thread {
    
    /** la chat */
    private JMSChat simpleChat;

    /** numero di messaggi da inviare [01 99] */
    private int n; 
    
    /** ritardo tra messaggi */ 
    private int delay; 
    private boolean term=false;
    /** Creates a new instance of ConsumerThread
     * @param chat oggetto chat
     * @param numberOfMessage numero di messaggi randomici da inviare
     * @param maxDelay delay massimo applicabile
     */
    public RandomMessage(JMSChat chat, int numberOfMessage, int maxDelay) {
        super(); 
        this.simpleChat = chat;
        this.n = numberOfMessage;
        this.delay = maxDelay;
    }
    
    @Override
    public void run() {
       
        for (int i = 0; (i < this.n && !term); i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            this.simpleChat.sendMessage(sdf.format(cal.getTime()));
            try {
               Thread.sleep((long) (1000+Math.random()*this.delay));//sleep for 1000 ms
            } catch (Exception e) {
              System.out.print("Thread: "+e);
            }

        }
    	
    }

    public void terminate() {
        this.term=true;
    }


}
