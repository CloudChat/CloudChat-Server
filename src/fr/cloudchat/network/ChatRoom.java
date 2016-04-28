package fr.cloudchat.network;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import fr.cloudchat.data.DataStorage;
import fr.cloudchat.data.DatabaseManager;
import fr.cloudchat.data.IdentityStorage;
import fr.cloudchat.network.messages.AbstractMessage;
import fr.cloudchat.network.messages.out.ChatTextOutMessage;
import fr.cloudchat.network.messages.out.HistoryMessage;
import fr.cloudchat.network.messages.out.UsersListMessage;
import fr.cloudchat.serialization.JsonSerializable;
import fr.cloudchat.social.SocialIdentity;

public class ChatRoom {
	
	private int roomId;
	private String name;
	private transient IdentityStorage identities;
	private transient ArrayList<ChatClient> clients;
	private ArrayList<ChatTextOutMessage> messages;
	private int currentMessageId = 0;
	
	public ChatRoom(String name) {
		this.name = name;
		this.identities = new IdentityStorage();
		this.clients = new ArrayList<>();
		this.messages = new ArrayList<>();
	}
	
	public void init() {
		if(this.identities == null) {
			this.identities = new IdentityStorage();
			this.clients = new ArrayList<>();
			
			this.recalculateUID();
		}
	}
	
	public void save() {
		DatabaseManager.saveRoom(this);
	}
	
	public void recalculateUID() {
		int uid = 0;
		for(ChatTextOutMessage message : this.messages) {
			if(message.getMessage_uid() > uid) {
				uid = message.getMessage_uid() + 1;
			}
		}
		this.currentMessageId = uid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IdentityStorage getIdentities() {
		return identities;
	}

	public void setIdentities(IdentityStorage identities) {
		this.identities = identities;
	}
	
	public void addClient(ChatClient client) {
		synchronized (this.clients) {
			this.clients.add(client);
			this.refreshConnectedList();
		}
	}
	
	public void removeClient(ChatClient client) {
		synchronized (this.clients) {
			this.clients.remove(client);
			this.refreshConnectedList();
		}
	}
	
	public int getRoomSize() {
		return this.clients.size();
	}
	
	public void sendHistory(ChatClient client) {
		client.writeData(new HistoryMessage(client.getIdentity().getScope(),
				messages.subList((messages.size() - 250 < 0 ? 0 : messages.size() - 250),
						messages.size())));
	}
	
	public void refreshMessageList() {
		synchronized (this.clients) {
			for(ChatClient client : this.clients) {
				sendHistory(client);
			}
		}
	}
	
	public void refreshConnectedList() {
		synchronized (this.clients) {
			HashMap<String, SocialIdentity> connected = new HashMap<>();
			
			for(ChatClient client : this.clients) {
				if(!connected.containsKey(client.getIdentity().getUsername())){
					connected.put(client.getIdentity().getUsername(),
							client.getIdentity());
				}
			}
			this.broadcast(new UsersListMessage
					(new ArrayList<SocialIdentity>(connected.values())));
		}
	}
	
	public void broadcast(JsonSerializable message) {
		Gson gson = new Gson();
		String raw = message.serialize(gson);
		
		synchronized (this.clients) {
			for(ChatClient client : this.clients) {
				client.writeData(raw);
			}
		}
	}
	
	public int getAvailableMessageUID() {
		this.currentMessageId++;
		return this.currentMessageId;
	}
	
	public void saveMessage(ChatTextOutMessage message) {
		synchronized (this.messages) {
			this.messages.add(message);
		}
		
		DatabaseManager.saveMessage(this, message);
		//DataStorage.saveEverything();
	}
	
	public void clearMessages() {
		synchronized (this.messages) {
			this.messages.clear();
		}
		this.refreshMessageList();
		DataStorage.saveEverything();
	}
	
	public ChatTextOutMessage getMessageByUID(int uid) {
		synchronized (this.messages) {
			for(ChatTextOutMessage message : this.messages) {
				if(message.getMessage_uid() == uid) {
					return message;
				}
			}
			return null;
		}
	}
	
	public void deleteMessage(ChatTextOutMessage message){
		synchronized (this.messages) {
			this.messages.remove(message);
			this.refreshMessageList();
		}
		
		DataStorage.saveEverything();
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
}
