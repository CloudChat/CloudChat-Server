package fr.cloudchat.data;

import java.util.HashMap;
import java.util.logging.Logger;

import fr.cloudchat.network.ChatServer;
import fr.cloudchat.social.SocialIdentity;
import fr.cloudchat.social.TokenizedSocialIdentity;

public class IdentityStorage {
	private static Logger logger = Logger.getLogger(IdentityStorage.class.getName());
	
	private HashMap<String, TokenizedSocialIdentity> identities;
	
	public IdentityStorage() {
		this.identities = new HashMap<>();
	}
	
	public void addIdentity(TokenizedSocialIdentity identity) {
		this.identities.put(identity.getToken(), identity);
		
		logger.info("Add a new identity to the storage"
				+ " (username: " + identity.getUsername() + ", "
				+ "storageSize: " + this.identities.size() + ")");
	}
	
	public boolean hasIdentityByToken(String token){
		return this.identities.containsKey(token);
	}
	
	public boolean hasIdentityByUsername(String username){
		for(TokenizedSocialIdentity identity : this.identities.values()) {
			if(identity.getUsername().equals(username)){
				return true;
			}
		}
		return false;
	}
	
	public SocialIdentity getIdentity(String token) {
		TokenizedSocialIdentity identity = this.identities.get(token);
		return new SocialIdentity(identity.getUsername(), 
				identity.getPicture(), identity.getScope());
	}
}
