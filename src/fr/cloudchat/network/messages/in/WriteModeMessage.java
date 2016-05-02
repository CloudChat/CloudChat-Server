package fr.cloudchat.network.messages.in;

import fr.cloudchat.network.messages.AbstractMessage;

public class WriteModeMessage extends AbstractMessage {

	 private boolean toogle;

	public boolean isToogle() {
		return toogle;
	}

	public void setToogle(boolean toogle) {
		this.toogle = toogle;
	}
}
