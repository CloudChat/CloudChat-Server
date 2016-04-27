package fr.cloudchat.network.handlers;

import java.util.logging.Logger;

import fr.cloudchat.data.RoomStorage;
import fr.cloudchat.network.ChatClient;
import fr.cloudchat.network.ChatRoom;
import fr.cloudchat.network.messages.in.RegisterMessage;
import fr.cloudchat.social.SocialIdentity;

public class HandshakeHandler {
	
	private static Logger logger = Logger.getLogger(HandshakeHandler.class.getName());

	public static void handleRegisterMessage(ChatClient client, RegisterMessage message) {
		logger.info("Request register for client in room '" + message.getProvider() + "' "
				+ "(token: " + message.getToken() + ")");
		
		if(RoomStorage.getInstance().hasRoomWithName(message.getProvider())) {
			ChatRoom room = RoomStorage.getInstance().getRoomByName(message.getProvider());
			if(room.getIdentities().hasIdentityByToken(message.getToken())) {
				SocialIdentity identity = room.getIdentities().getIdentity(message.getToken());
				client.setIdentity(identity);
				client.setRoom(room);
				room.addClient(client);
				logger.info("Client registered in room (size: " + room.getRoomSize() + ")");
				
				room.sendHistory(client);
			}
			else {
				logger.info("Can't found the token for the user, force disconnect");
			}
		}
		else {
			logger.info("Can't found the room '" + message.getProvider() + "' for "
					+ "the user, force disconnect");
		}
	}
}
