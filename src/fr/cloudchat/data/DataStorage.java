package fr.cloudchat.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.cloudchat.network.ChatRoom;
import fr.cloudchat.network.handlers.HandshakeHandler;

public class DataStorage {

	private static Logger logger = Logger.getLogger(DataStorage.class.getName());
	
	public static void saveEverything(){
		Gson gson = new Gson();
		String serialized = gson.toJson(RoomStorage.getInstance().getRooms());
		
		PrintWriter writer;
		try {
			writer = new PrintWriter("history.json", "UTF-8");
			writer.print(serialized);
			writer.close();
		} catch (FileNotFoundException e) {
			logger.severe(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public static void loadEverything() {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get("history.json"));
			String content = new String(encoded, StandardCharsets.UTF_8);
			Type type = new TypeToken<HashMap<String, ChatRoom>>(){}.getType();
			Gson gson = new Gson();
			RoomStorage.getInstance().setRooms
				((HashMap<String, ChatRoom>)gson.fromJson(content, type));
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}
}
