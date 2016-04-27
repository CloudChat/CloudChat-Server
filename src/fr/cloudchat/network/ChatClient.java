package fr.cloudchat.network;

import java.util.logging.Logger;

import org.java_websocket.WebSocket;

import com.google.gson.Gson;

import fr.cloudchat.network.handlers.ChatHandler;
import fr.cloudchat.network.handlers.HandshakeHandler;
import fr.cloudchat.network.messages.AbstractMessage;
import fr.cloudchat.network.messages.in.ChatTextMessage;
import fr.cloudchat.network.messages.in.MessageDeleteRequestMessage;
import fr.cloudchat.network.messages.in.RegisterMessage;
import fr.cloudchat.serialization.JsonSerializable;
import fr.cloudchat.social.SocialIdentity;

public class ChatClient implements MessageClient {
	
	private static Logger logger = Logger.getLogger(ChatClient.class.getName());
	
	private WebSocket socket;
	private SocialIdentity identity;
	private ChatRoom room;

	public ChatClient(WebSocket socket) {
		this.socket = socket;
		this.setIdentity(null);
		
		this.accept();
	}
	
	private void accept() {
		//TODO: Basicly check ip is not banned ..
	}
	
	public void destroy() {
		if(this.room != null) {
			this.room.removeClient(this);
		}
	}
	
	public void disconnect() {
		
	}
	
	public void deserializeRawData(String message) {
		logger.info("Raw data : " + message);
		Gson gson = new Gson();
		AbstractMessage abstractMessage = gson.fromJson(message, AbstractMessage.class);
		logger.info("Dispatch messageId : " + abstractMessage.getMessage_id());
		this.dispatchMessage(abstractMessage, message);
	}

	@Override
	public void writeData(JsonSerializable object) {
		Gson gson = new Gson();
		String raw = object.serialize(gson);
		//logger.info("Send data : " + raw);
		this.socket.send(raw);
	}
	
	@Override
	public void writeData(String object) {
		//logger.info("Send data : " + object);
		this.socket.send(object);
	}

	@Override
	public void dispatchMessage(AbstractMessage abstractMessage, String raw) {
		Gson gson = new Gson();
		
		switch (abstractMessage.getMessage_id()) {
			case "REGISTER":
			HandshakeHandler.handleRegisterMessage(this, 
					gson.fromJson(raw, RegisterMessage.class));
				break;
			
			case "MESSAGE":
				ChatHandler.handleChatTextMessage(this, 
						gson.fromJson(raw, ChatTextMessage.class));
				break;
			
			case "MESSAGE_DELETE":
				ChatHandler.handleMessageDeleteRequestMessage(this, 
						gson.fromJson(raw, MessageDeleteRequestMessage.class));
				break;
		}
	}

	public boolean hasIdentity() {
		return this.getIdentity() != null;
	}
	
	public ChatRoom getRoom() {
		return room;
	}

	public void setRoom(ChatRoom room) {
		this.room = room;
	}

	public SocialIdentity getIdentity() {
		return identity;
	}

	public void setIdentity(SocialIdentity identity) {
		this.identity = identity;
	}
}
