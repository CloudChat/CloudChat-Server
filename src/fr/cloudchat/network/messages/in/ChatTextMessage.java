package fr.cloudchat.network.messages.in;

import fr.cloudchat.network.messages.AbstractMessage;

public class ChatTextMessage extends AbstractMessage {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
