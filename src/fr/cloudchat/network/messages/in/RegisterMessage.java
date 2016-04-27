package fr.cloudchat.network.messages.in;

import fr.cloudchat.network.messages.AbstractMessage;

public class RegisterMessage extends AbstractMessage {
	private String token;
	private String provider;
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
}
