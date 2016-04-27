package fr.cloudchat.social;

import fr.cloudchat.misc.ScopeLevelEnum;

public class SocialIdentity {
	
	private String username;
	private String picture;
	private int scope;
	
	public SocialIdentity(String username, String picture, int scope) {
		this.username = username;
		this.picture = picture;
		this.scope = scope;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String avatar) {
		this.picture = avatar;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public boolean hasAdminRights(){
		return this.scope > 0;
	}
	
	public TokenizedSocialIdentity tokenize(String token) {
		return new TokenizedSocialIdentity(this.username, this.picture,
				this.scope, token);
	}
}
