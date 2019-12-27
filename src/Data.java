import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class Data implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<Player> players;
	private Vector<Picture> pictures;
	private Vector<Quest> quests;
	private Die[] dice;
	private Vector<Message> chatHistory;
	
	private int total;
	private int prevTotal;
	private Quest currentQuest;
	private String time;
	
	Picture focus;
	int zoomScale;
	int picX;
	int picY;
	
	public int getZoomScale() {
		return zoomScale;
	}

	public int getPicX() {
		return picX;
	}

	public int getPicY() {
		return picY;
	}

	public Data(String fileLocation){
		
	}
	
	public Quest getCurrentQuest(){
		return currentQuest;
	}
	
	public Vector<Player> getPlayers() {
		return players;
	}

	public Vector<Picture> getPictures() {
		return pictures;
	}

	public Vector<Quest> getQuests() {
		return quests;
	}

	public Die[] getDice() {
		return dice;
	}

	public Vector<Message> getChatHistory() {
		return chatHistory;
	}
	
	public int getTotal(){
		return total;
	}
	
	public int getPrevTotal(){
		return prevTotal;
	}
	
	public String getTime(){
		return time;
	}
	
	public Picture getFocus(){
		return focus;
	}
	
	public void roll(){
		prevTotal = total;
		total = 0;
		for (int i = 0; i < dice.length; i++){
			dice[i].roll();
			if (dice[i].isVisible()){
				total += dice[i].getCurrent();
			}
		}
		
	}
	
	public void update(String s){
		String id = s.substring(0,3);
		if (id.equals("ROL")){
			roll();
		}
		else if (id.equals("LVL")){
			try{
				players.get(Integer.parseInt(s.substring(4,5))).setLevel( Math.min(Math.max(Integer.parseInt(s.substring(6)), 1), 99));
			}catch(Exception e){}
		}
		else if (id.equals("DIE")){
			try{
				int index = Integer.parseInt(s.substring(4,5));
				if (s.charAt(6) == 'x'){
					dice[index].setVisible(false);
				}
				else if (s.charAt(6) == 'o'){
					dice[index].setVisible(true);
				}
				else{
					dice[index].setMax( Math.min(Math.max(Integer.parseInt(s.substring(6)), 2), 99));
				}
				
			}catch(Exception e){
				System.out.println("Error changing Die: " + s);
			}
		}
		else if (id.equals("CHP")){
			try{
				players.get(Integer.parseInt(s.substring(4,5))).setCurrentHp( Math.min(Math.max(Integer.parseInt(s.substring(6)), 1), players.get(Integer.parseInt(s.substring(4,5))).getTotalHp()));
			}catch(Exception e){
				System.out.println("Error changing Current HP: " + s);
			}
		}
		else if (id.equals("MHP")){
			try{
				players.get(Integer.parseInt(s.substring(4,5))).setTotalHp( Math.min(Math.max(Integer.parseInt(s.substring(6)), 1), 99));
				if (players.get(Integer.parseInt(s.substring(4,5))).getCurrentHp() > players.get(Integer.parseInt(s.substring(4,5))).getTotalHp()){
					players.get(Integer.parseInt(s.substring(4,5))).setCurrentHp(players.get(Integer.parseInt(s.substring(4,5))).getTotalHp());
				}
			}catch(Exception e){
				System.out.println("Error changing Max HP: " + s);
			}
		}
		else if (id.equals("TME")){
			try{
				String hours = s.substring(4,6);
				String minutes = s.substring(7,9);
				time = (Math.min(Math.max(Integer.parseInt(hours), 0), 23)) + ":" + Math.min(Math.max(Integer.parseInt(minutes), 0), 59);
			}catch(Exception e){
				System.out.println("Error changing Time: " + s);
			}
		}
		else if (id.equals("ZOO")){
			zoomScale = Math.min(Math.max(zoomScale + Integer.parseInt(s.substring(4)), -4), 4);
		}
		else if (id.equals("XCG")){
			picX = Math.min(Math.max(picX + Integer.parseInt(s.substring(4)), 0), focus.getImage().getWidth(null));
		}
		else if (id.equals("YCG")){
			picY = Math.min(Math.max(picY + Integer.parseInt(s.substring(4)), 0), focus.getImage().getHeight(null));
		}
		else if (id.equals("CHT")){
			// TODO
		}
	}

	public Data(){
		players = new Vector<Player>();
		pictures = new Vector<Picture>();
		quests = new Vector<Quest>();
		dice = new Die[6];
		chatHistory = new Vector<Message>();
		
		for (int i = 0; i < 4; i++){
			players.add(new Player());
		}
		for (int i = 0; i < 7; i++){
			pictures.add(new Picture());
		}
		focus = pictures.get(0);
		for (int i = 0; i < 3; i++){
			quests.add(new Quest());
		}
		int total = 0;
		for (int i = 0; i < 6; i++){
			dice[i] = new Die();
			if (dice[i].isVisible()) total += dice[i].getCurrent();
		}
		for (int i = 0; i < 20; i++){
			//chatHistory.add(new Message(players));
			String s = "";
			for (int j = 0; j < i; j++){
				s += "HELLO:D ";
			}
			chatHistory.add(new Message("Server", s, System.currentTimeMillis()));
		}
		this.total = total;
		time = "" + (int)(Math.random()*24 + 1) + ":" + String.format("%02d",(int)(Math.random()*60));
		focus = pictures.get((int)(Math.random()*pictures.size()));
		
	}
}
