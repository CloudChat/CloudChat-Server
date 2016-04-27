package fr.cloudchat.network.messages.in;

import fr.cloudchat.network.messages.AbstractMessage;

public class MessageDeleteRequestMessage extends AbstractMessage {
	private int message_uid;

	public int getMessage_uid() {
		return message_uid;
	}

	public void setMessage_uid(int message_uid) {
		this.message_uid = message_uid;
	}
}
