package fr.cloudchat.network.messages.out;

import java.util.ArrayList;

import com.google.gson.Gson;

import fr.cloudchat.network.messages.AbstractMessage;
import fr.cloudchat.serialization.JsonSerializable;
import fr.cloudchat.social.SocialIdentity;

public class UsersListMessage extends AbstractMessage implements JsonSerializable {

	public ArrayList<SocialIdentity> connected;
	
	public UsersListMessage(ArrayList<SocialIdentity> connected) {
		this.setMessage_id("USER_LIST");
		this.connected = connected;
	}
	
	@Override
	public String serialize(Gson gson) {
		return gson.toJson(this);
	}

}
