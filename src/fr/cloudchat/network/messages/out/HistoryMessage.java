package fr.cloudchat.network.messages.out;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import fr.cloudchat.network.messages.AbstractMessage;
import fr.cloudchat.serialization.JsonSerializable;

public class HistoryMessage extends AbstractMessage implements JsonSerializable {
	
	private int scope;
	private List<ChatTextOutMessage> messages;
	
	public HistoryMessage(int scope, List<ChatTextOutMessage> messages) {
		this.setMessage_id("HISTORY");
		this.scope = scope;
		this.messages = messages;
	}
	
	public String serialize(Gson gson) {
		return gson.toJson(this);
	}
}
