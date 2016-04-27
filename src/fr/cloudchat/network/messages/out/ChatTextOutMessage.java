package fr.cloudchat.network.messages.out;

import com.google.gson.Gson;

import fr.cloudchat.network.messages.AbstractMessage;
import fr.cloudchat.serialization.JsonSerializable;
import fr.cloudchat.social.SocialIdentity;

public class ChatTextOutMessage extends AbstractMessage implements JsonSerializable {

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public SocialIdentity getFrom() {
		return from;
	}

	public void setFrom(SocialIdentity from) {
		this.from = from;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getMessage_uid() {
		return message_uid;
	}

	public void setMessage_uid(int message_uid) {
		this.message_uid = message_uid;
	}

	private String message;
	private String color;
	private SocialIdentity from;
	private String date;
	private int message_uid;
	
	public ChatTextOutMessage(String content, String color, SocialIdentity from,
			String date, int messageUid) {
		this.setMessage_id("MESSAGE");
		this.message = content;
		this.color = color;
		this.from = from;
		this.date = date;
		this.message_uid = messageUid;
	}
	
	@Override
	public String serialize(Gson gson) {
		return gson.toJson(this);
	}
}
