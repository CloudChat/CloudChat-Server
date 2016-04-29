package fr.cloudchat.network.handlers;

import fr.cloudchat.network.messages.in.ChatTextMessage;
import fr.cloudchat.network.messages.in.MessageDeleteRequestMessage;
import fr.cloudchat.network.messages.out.ChatTextOutMessage;
import fr.cloudchat.social.SocialIdentity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

import fr.cloudchat.network.ChatClient;

public class ChatHandler {
	
	private static Logger logger = Logger.getLogger(ChatHandler.class.getName());
	
	public static void handleChatTextMessage(ChatClient client, ChatTextMessage message) {
		int messageLenLimit = 400;
		
		if(client.hasIdentity()) {
			// ByPass message length limit
			if(client.getIdentity().hasAdminRights()) {
				messageLenLimit = Integer.MAX_VALUE;
			}
			
			String textMessage = message.getMessage().trim();
			if(textMessage.length() > messageLenLimit || textMessage.equals("")) {
				return;
			}
			
			if(client.getRoom() != null) {
				if(textMessage.startsWith("/")){
					parseChatCommand(client, textMessage.substring(1, textMessage.length()));
					return;
				}
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
				
				ChatTextOutMessage chatTextOutMessage = new ChatTextOutMessage
						(textMessage, "#000000", client.getIdentity(), dateFormat.format(cal.getTime()),
								client.getRoom().getAvailableMessageUID());
				client.getRoom().saveMessage(chatTextOutMessage);
				client.getRoom().broadcast(chatTextOutMessage);
			}
			else {
				client.disconnect();
			}
		}
		else {
			client.disconnect();
		}
	}
	
	public static void handleMessageDeleteRequestMessage
					(ChatClient client, MessageDeleteRequestMessage message) {
		if(client.hasIdentity()){
			if(client.getRoom() != null){
				if(client.getIdentity().hasAdminRights()) {
					ChatTextOutMessage toDelete = client.getRoom()
							.getMessageByUID(message.getMessage_uid());
					client.getRoom().deleteMessage(toDelete);
				}
				else {
					client.disconnect();
				}
			}
			else {
				client.disconnect();
			}
		}
		else {
			client.disconnect();
		}
	}
	
	public static void parseChatCommand(ChatClient client, String command) {
		if(client.getIdentity().getScope() == 0) return;
		String[] parameters = command.split(" ");
		switch (parameters[0]) {
		case "effacer":
			client.getRoom().clearMessages();
			break;
		
		case "annonce":
			chatWithBot(client, "Annonce",
					command.substring(parameters[0].length()).trim(), true);
			break;

		case "aide":
			chatWithBot(client, "Aide",
					"/effacer - <b>Supprime tous les messages du chat</b><br />" + 
					"/annonce [text] - <b>Fait une annonce dans le chat</b>",
					 false);
			break;
			
		default:
			break;
		}
	}
	
	public static void chatWithBot(ChatClient client, String subtile, String content,
			boolean global) {
		SocialIdentity fakeIdentity = new SocialIdentity
				("Robot", "<img src=\"http://media4.popsugar-assets.com/files/2014/07/28/910/n/1922507/e399c9429796a92f_robotMSgrFH.xxxlarge/i/Robot.jpg\" width='32' height='32' />",
						1, -1);
		ChatTextOutMessage chatTextOutMessage = new ChatTextOutMessage
				(content, "#000000", fakeIdentity, subtile,
							client.getRoom().getAvailableMessageUID());
			if(global) {
				client.getRoom().saveMessage(chatTextOutMessage);
				client.getRoom().broadcast(chatTextOutMessage);
			}
			else {
				client.writeData(chatTextOutMessage);
			}
			
	}
}
