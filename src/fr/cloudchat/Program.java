package fr.cloudchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import fr.cloudchat.data.DataStorage;
import fr.cloudchat.http.ApiServer;
import fr.cloudchat.network.ChatServer;

public class Program {
	
	private static Logger logger = Logger.getLogger(Program.class.getName());
	
	private static ChatServer chatServer = null;
	private static ApiServer apiServer = null;
	
	public static ChatServer getChatServer() {
		return chatServer;
	}

	public static void main(String[] args) {
		logger.info("Starting CloudChat server ..");
		
		DataStorage.loadEverything();
		
		// Starting ApiServer
		apiServer = new ApiServer(3000);
		apiServer.start();
		
		// Starting ChatServer
		chatServer = new ChatServer(new InetSocketAddress( 8080 ));
		chatServer.start();
		
		logger.info("CloudChat started !");
		
		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
		while ( true ) {
			try {
				String in = sysin.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
