package fr.cloudchat.network.messages;

public class AbstractMessage {
	protected String message_id;

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
}
