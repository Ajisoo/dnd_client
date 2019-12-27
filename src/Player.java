package old;

import java.awt.Graphics;
import java.io.Serializable;

public class Player implements Serializable{
		
	private String name;
	private int totalHp;
	private int currentHp;
	private int level;
	
	public String getName(){
		return name;
	}
	
	public int getTotalHp(){
		return totalHp;
	}
	
	public int getCurrentHp(){
		return currentHp;
	}
	
	
	public int getLevel(){
		return level;
	}
	
	public Player(){
		name = "Player: " + (int)(Math.random()*10);
		totalHp = (int)(Math.random()*20) + 1;
		currentHp = (int)(Math.random()*(totalHp + 1));
		level = (int)(Math.random()*99) + 1;
	}
	
	public Player(String name, int level, int currentHp, int totalHp){
		this.name = name;
		this.level = level;
		this.totalHp = totalHp;
		this.currentHp = currentHp;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTotalHp(int totalHp) {
		this.totalHp = totalHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
