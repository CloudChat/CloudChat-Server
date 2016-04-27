package fr.cloudchat.misc;

public enum ScopeLevelEnum {
	BASIC(1),
	ADMIN(2);
	
	private int scope = 1;
	ScopeLevelEnum(int scope) {
		this.scope = scope;
	}
	
	public int get(){
		return this.scope;
	}
	
	@Override
	public String toString() {
		return Integer.toString(this.scope);
	}
}
