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
import fr.cloudchat.social.SocialIdentity;
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
	
	public static void checkDatabaseConnection() {
		try {
			if(connection.isClosed()) {
				ConfigFile.DatabaseConfig mysqlConf = Program.getConfig().getMysql();
				connection = DriverManager.getConnection
						( "jdbc:mysql://" + mysqlConf.getUrl()  +
								"/" + mysqlConf.getDatabase(),
								mysqlConf.getUsername(), mysqlConf.getPassword() );
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveRoom(ChatRoom room) {
		
		try {
			checkDatabaseConnection();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO chat_room (room_name, room_password)" + 
					" VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, room.getName());
			stmt.setString(2, "");
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	room.setRoomId(generatedKeys.getInt(1));
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
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
				loadIdentities(room);
				loadMessages(room);
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
			checkDatabaseConnection();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO chat_message "
					+ "(message_uid, content, color, from_id, write_date, room_id)" + 
					" VALUES (?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, message.getMessage_uid());
			stmt.setString(2, message.getMessage());
			stmt.setString(3, message.getColor());
			stmt.setInt(4, message.getFrom().getId());
			stmt.setString(5, message.getDate());
			stmt.setInt(6, room.getRoomId());
			stmt.executeUpdate();
			
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                message.setId(generatedKeys.getInt(1));
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public static void loadMessages(ChatRoom room) {
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery( "SELECT id, message_uid, content, color, from_id, write_date, room_id"
					+ " FROM chat_message WHERE room_id=" + room.getRoomId() );
			while(result.next()) {
				int fromId = result.getInt("from_id");
				SocialIdentity identity = null;
				if(fromId == -1) {
					identity = new SocialIdentity
							("Robot", "<img src=\"http://media4.popsugar-assets.com/files/2014/07/28/910/n/1922507/e399c9429796a92f_robotMSgrFH.xxxlarge/i/Robot.jpg\" width='32' height='32' />",
									1, -1);
				}
				else {
					identity = room.getIdentities().getIdentityById(fromId);
				}
				if(identity != null){
					ChatTextOutMessage message = new ChatTextOutMessage
							(result.getString("content"), result.getString("color"), identity, result.getString("write_date"),
									result.getInt("message_uid"));
					message.setId(result.getInt("id"));
					room.getMessages().add(message);
					room.recalculateUID();
				}
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveIdentity(ChatRoom room, TokenizedSocialIdentity identity) {
		try {
			checkDatabaseConnection();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO chat_identity "
					+ "(username, picture, scope, token, room_id)" + 
					" VALUES (?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, identity.getUsername());
			stmt.setString(2, identity.getPicture());
			stmt.setInt(3, identity.getScope());
			stmt.setString(4, identity.getToken());
			stmt.setInt(5, room.getRoomId());
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	identity.setId(generatedKeys.getInt(1));
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public static void loadIdentities(ChatRoom room) {
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery( "SELECT id, username, picture, scope, token, room_id"
					+ " FROM chat_identity WHERE room_id=" + room.getRoomId() );
			while(result.next()) {
				TokenizedSocialIdentity identity = new TokenizedSocialIdentity
						(result.getString("username"), result.getString("picture"),
								result.getInt("scope"), result.getString("token"), result.getInt("id"));
				//logger.info("Loaded identity id: " + identity.getId());
				room.getIdentities().addIdentity(identity);
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteMessage(ChatTextOutMessage message) {
		try {
			checkDatabaseConnection();
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM chat_message WHERE id=" + message.getId());
			int id = stmt.executeUpdate();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
	}
}
