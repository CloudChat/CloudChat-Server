package fr.cloudchat.http;

import static spark.Spark.*;

import java.util.logging.Logger;

import javax.xml.ws.handler.MessageContext.Scope;

import fr.cloudchat.Program;
import fr.cloudchat.data.DatabaseManager;
import fr.cloudchat.data.RoomStorage;
import fr.cloudchat.misc.ScopeLevelEnum;
import fr.cloudchat.network.ChatRoom;
import fr.cloudchat.social.TokenizedSocialIdentity;
import spark.Request;
import spark.Response;

public class ApiServer {
	private static Logger logger = Logger.getLogger(ApiServer.class.getName());

	private int port;
	
	public ApiServer(int port) {
		this.port = port;
	}
	
	public void start() {
		port(this.port);
		
		post("/add_client", (req, res) -> handleAddClient(req, res));
	}
	
	private String handleAddClient(Request req, Response res) {
		logger.info("Receive new tokenized identity (token: " + req.queryParams("token") + ")");
		
		String token = req.queryParams("token");
		String username = req.queryParams("username");
		int scope = Integer.parseInt(req.queryParams("scope"));
		String avatar = req.queryParams("picture");
		String provider = req.queryParams("provider");
		
		TokenizedSocialIdentity identity = new TokenizedSocialIdentity
				(username, avatar, scope, token);
		
		ChatRoom room = null;
		if(RoomStorage.getInstance().hasRoomWithName(provider)) {
			logger.info("Get existing '" + provider + "' room instance");
			room = RoomStorage.getInstance().getRoomByName(provider);
			room.init();
		}
		else {
			logger.info("Create '" + provider + "' room instance");
			room = new ChatRoom(provider);
			RoomStorage.getInstance().registerRoom(room);
			room.save();
		}
		
		if(!room.getIdentities().hasIdentityByToken(token)) {
			logger.info("New identity added");
			room.getIdentities().addIdentity(identity);
			DatabaseManager.saveIdentity(room, identity);
		}
		
		return "OK";
	}
}
