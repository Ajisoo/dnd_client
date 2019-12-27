import java.io.Serializable;

public class Quest  implements Serializable{
	private String name;
	private String description;
	private String rewards;
	
	public Quest(String name, String description, String rewards){
		this.name = name;
		this.description = description;
		this.rewards = rewards;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getRewards(){
		return rewards;
	}
	
	public Quest(){
		name = "Quest: " + (int)(Math.random() * 10) + 1;
		description = "Description: " + (int)(Math.random() * 10) + 1;
		rewards = "Rewards: " + (int)(Math.random() * 10) + 1;
	}
}
