package fr.cloudchat.social;

import fr.cloudchat.misc.ScopeLevelEnum;

public class TokenizedSocialIdentity extends SocialIdentity {

	private String token;
	
	public TokenizedSocialIdentity(String username, String avatar, int scope,
			String token, int id) {
		super(username, avatar, scope, id);

		this.setToken(token);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
