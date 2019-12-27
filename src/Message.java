package old;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class Message implements Serializable{
	private String chat;
	private String sender;
	private Date time;
	
	public Message(String sender, String chat, long time){
		this.sender = sender;
		this.chat = chat;
		this.time = new Date();
		this.time.setTime(time);
	}
	
	public Message(Vector<Player> players){
		int random = (int)(Math.random() * (players.size() + 1) - 1);
		if (random == -1){
			sender = "Server";
		}
		else{
			sender = players.get(random).getName();
		}
		chat = "Chat Message " + (int)(Math.random() * 100);
		time = new Date();
		time.setTime(System.currentTimeMillis());
	}
	
	public String getChat(){
		return chat;
	}
	
	public String getSender(){
		return sender;
	}
	
	public Date getTime(){
		return time;
	}
}
