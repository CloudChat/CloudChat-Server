package fr.cloudchat.misc;

public enum MessageTypeEnum {
	ANONYMOUS(1),
	IDENTIFIED(2);
	
	private int type = 1;
	MessageTypeEnum(int type) {
		this.type = type;
	}
	
	public int get(){
		return this.type;
	}
}
