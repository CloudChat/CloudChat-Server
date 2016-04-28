package fr.cloudchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.eclipse.jetty.util.PathWatcher.Config;

import fr.cloudchat.data.ConfigFile;
import fr.cloudchat.data.DataStorage;
import fr.cloudchat.data.DatabaseManager;
import fr.cloudchat.http.ApiServer;
import fr.cloudchat.network.ChatServer;

public class Program {
	
	private static Logger logger = Logger.getLogger(Program.class.getName());
	
	private static ChatServer chatServer = null;
	private static ApiServer apiServer = null;
	private static ConfigFile config = null;
	
	public static ChatServer getChatServer() {
		return chatServer;
	}

	public static void main(String[] args) {
		logger.info("Starting CloudChat server ..");
		
		readConfig();
		//DataStorage.loadEverything();
		DatabaseManager.init();
		
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
	
	public static void readConfig() {
		config = ConfigFile.getConfig();
	}

	public static ConfigFile getConfig() {
		return config;
	}
}
