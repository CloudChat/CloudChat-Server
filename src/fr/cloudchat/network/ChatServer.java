package fr.cloudchat.network;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ChatServer extends WebSocketServer {

	private static Logger logger = Logger.getLogger(ChatServer.class.getName());
	
	private HashMap<WebSocket, ChatClient> clients;
	
	public ChatServer(InetSocketAddress address) {
		super(address);
		this.clients = new HashMap<>();
	}

	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		synchronized (this.clients) {
			logger.info("Close socket data (reason: " + arg1 + ")");
			ChatClient client = this.clients.get(arg0);
			if(client != null) {
				client.destroy();
				this.clients.remove(arg0);
			}
		}
	}

	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		
	}

	@Override
	public void onMessage(WebSocket arg0, String arg1) {
		synchronized (this.clients) {
			logger.info("Incoming socket data (len: " + arg1.length() + ")");
			ChatClient client = this.clients.get(arg0);
			if(client != null) {
				client.deserializeRawData(arg1);
			}
		}
	}

	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		logger.info("Incoming socket client <" + arg0.getRemoteSocketAddress().getAddress() + ">");
		ChatClient client = new ChatClient(arg0);
		synchronized (this.clients) {
			clients.put(arg0, client);
		}
	}
}
