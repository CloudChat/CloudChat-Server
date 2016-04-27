package fr.cloudchat.data;

import java.util.HashMap;

import fr.cloudchat.network.ChatRoom;

public class RoomStorage {
	private static RoomStorage instance;
	public static RoomStorage getInstance() {
		if(instance == null) instance = new RoomStorage();
		return instance;
	}
	
	private HashMap<String, ChatRoom> rooms;
	
	public HashMap<String, ChatRoom> getRooms() {
		return rooms;
	}

	public void setRooms(HashMap<String, ChatRoom> rooms) {
		this.rooms = rooms;
	}

	public RoomStorage() {
		rooms = new HashMap<>();
	}
	
	public void registerRoom(ChatRoom room) {
		this.rooms.put(room.getName(), room);
	}
	
	public boolean hasRoomWithName(String name) {
		return this.rooms.containsKey(name);
	}
	
	public ChatRoom getRoomByName(String name) {
		return this.rooms.get(name);
	}
}
