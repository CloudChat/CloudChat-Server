package fr.cloudchat.serialization;

import com.google.gson.Gson;

public interface JsonSerializable {
	public String serialize(Gson gson);
}
