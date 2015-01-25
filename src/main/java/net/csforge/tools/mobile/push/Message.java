package net.csforge.tools.mobile.push;

public class Message {
	private String id;
	private String message;
	private int badge;
	private String sound;
	private Object extras;
	
	public Message() {
		super();
	}
	public Message(String message) {
		super();
		this.message = message;
	}
	public Message(String id, String message) {
		super();
		this.id = id;
		this.message = message;
	}
	public Message(String id, String message, Object extras) {
		super();
		this.id = id;
		this.message = message;
		this.extras = extras;
	}
	public Message(String message, Object extras) {
		super();
		this.message = message;
		this.extras = extras;
	}
	public Message(String id, String message, int badge, String sound) {
		super();
		this.id = id;
		this.message = message;
		this.badge = badge;
		this.sound = sound;
	}
	public Message(String message, int badge, String sound, Object extras) {
		super();
		this.message = message;
		this.badge = badge;
		this.sound = sound;
		this.extras = extras;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getBadge() {
		return badge;
	}
	public void setBadge(int badge) {
		this.badge = badge;
	}
	public String getSound() {
		return sound;
	}
	public void setSound(String sound) {
		this.sound = sound;
	}
	public Object getExtras() {
		return extras;
	}
	public void setExtras(Object extras) {
		this.extras = extras;
	}
}
