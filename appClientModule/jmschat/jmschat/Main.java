package jmschat;

import autogui.autoFrame;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Topic;

/**
 *
 * @author Luca Lanziani
 * @author Federico Spini
 */
public class Main {

	@Resource(name = "topic")
	private static Topic topic;
	@Resource(name="connectionFactory")
	private static ConnectionFactory connectionFactory;

	/**
     * @param args the command line arguments:
     */
	public static void main(String[] args) {
		String nickname = null;

		/* accesso ed analisi dei parametri */
		if ((args.length == 0)) {
			/* attenzione, deve essere eseguito con appclient */
			System.out.println("Usage: appclient -client app.jar <nick name>");
			System.exit(1);
		} else{
			nickname = new String(args[0]);
		}
		/* programma principale */
		//jmsChat
		JMSChat jmschat = new JMSChat(nickname, topic, connectionFactory);

                //creazione delle Gui
                autoFrame.init(jmschat);

	}

}
