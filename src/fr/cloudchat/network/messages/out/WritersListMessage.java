package fr.cloudchat.network.messages.out;

import java.util.ArrayList;

import com.google.gson.Gson;

import fr.cloudchat.network.messages.AbstractMessage;
import fr.cloudchat.serialization.JsonSerializable;
import fr.cloudchat.social.SocialIdentity;

public class WritersListMessage extends AbstractMessage implements JsonSerializable {

	public ArrayList<SocialIdentity> writers;
	
	public WritersListMessage(ArrayList<SocialIdentity> writers) {
		this.setMessage_id("WRITERS_LIST");
		this.writers = writers;
	}
	
	@Override
	public String serialize(Gson gson) {
		return gson.toJson(this);
	}

}
