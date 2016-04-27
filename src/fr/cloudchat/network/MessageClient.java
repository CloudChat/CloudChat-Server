package fr.cloudchat.network;

import fr.cloudchat.network.messages.AbstractMessage;
import fr.cloudchat.serialization.JsonSerializable;

public interface MessageClient {
	public void writeData(JsonSerializable object);
	public void writeData(String object);
	public void dispatchMessage(AbstractMessage abstractMessage, String raw);
}
