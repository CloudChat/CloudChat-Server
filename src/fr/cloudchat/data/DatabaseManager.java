package fr.cloudchat.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import fr.cloudchat.Program;
import fr.cloudchat.network.ChatRoom;
import fr.cloudchat.network.messages.out.ChatTextOutMessage;
import fr.cloudchat.social.TokenizedSocialIdentity;

public class DatabaseManager {
	
	private static Logger logger = Logger.getLogger(DatabaseManager.class.getName());
	private static Connection connection = null;
	
	public static void init() {
		try {
			Class.forName( "com.mysql.jdbc.Driver" );
			ConfigFile.DatabaseConfig mysqlConf = Program.getConfig().getMysql();
			connection = DriverManager.getConnection
					( "jdbc:mysql://" + mysqlConf.getUrl()  +
							"/" + mysqlConf.getDatabase(),
							mysqlConf.getUsername(), mysqlConf.getPassword() );
			logger.info("Connected to database");
			
			loadRooms();
		} catch ( SQLException e ) {
			logger.severe(e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public static void saveRoom(ChatRoom room) {
		
		try {
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO chat_room (room_name, room_password)" + 
					" VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, room.getName());
			stmt.setString(2, "");
			int id = stmt.executeUpdate();
			room.setRoomId(id);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public static void loadRooms(){
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery( "SELECT id, room_name, room_password FROM chat_room" );
			while(result.next()) {
				ChatRoom room = new ChatRoom(result.getString("room_name"));
				room.setRoomId(result.getInt("id"));
				RoomStorage.getInstance().registerRoom(room);
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveMessage(ChatRoom room, ChatTextOutMessage message) {
		try {
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO chat_message "
					+ "(message_uid, content, color, from_id, write_date)" + 
					" VALUES (?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, message.getMessage_uid());
			stmt.setString(2, message.getMessage());
			stmt.setString(3, message.getColor());
			stmt.setInt(4, -1);
			stmt.setString(5, message.getDate());
			
			int id = stmt.executeUpdate();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public static void loadMessages(ChatRoom room) {
		//TODO: Load messages for the room
	}
	
	public static void saveIdentity(ChatRoom room, TokenizedSocialIdentity identity) {
		try {
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO chat_identity "
					+ "(username, picture, scope, token, room_id)" + 
					" VALUES (?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, identity.getUsername());
			stmt.setString(2, identity.getPicture());
			stmt.setInt(3, identity.getScope());
			stmt.setString(4, identity.getToken());
			stmt.setInt(5, room.getRoomId());
			int id = stmt.executeUpdate();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public static void loadIdentities(ChatRoom room) {
		
		
	}
}
