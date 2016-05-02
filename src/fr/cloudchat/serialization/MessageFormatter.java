package fr.cloudchat.serialization;

import java.util.logging.Logger;

import fr.cloudchat.network.handlers.ChatHandler;

public class MessageFormatter {

	private static Logger logger = Logger.getLogger(ChatHandler.class.getName());
	
	public static String reformateMessageForDisplayMedia(String raw) {
		String message = "";
		boolean parsed = false;
		for(String word : raw.split(" ")) {
			if(word.startsWith("http") && !parsed) {
				if(word.endsWith(".png") || word.endsWith(".jpg") || word.endsWith(".jpeg")) {
					message = "<a target='_blank' href='" + word + "'>"
							+ "<img style='max-width: 100%;' src=\"" + word + "\" />"
									+ "</a><br />" + message;
					parsed = true;
					continue;
				}
			}
			message += word + " ";
		}
		return message;
	}
}
