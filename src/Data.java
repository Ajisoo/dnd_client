package old;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

import javax.imageio.ImageIO;

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
	
	private Color[][] drawing;
	
	Picture focus;
	int zoomScale;
	int picX;
	int picY;
	
	public Color[][] getDrawing(){
		return drawing;
	}
	
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
		players = new Vector<Player>();
		pictures = new Vector<Picture>();
		quests = new Vector<Quest>();
		dice = new Die[6];
		chatHistory = new Vector<Message>();
		focus = null;
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(new File(fileLocation + "players.txt")));
			for (int i = 0; i < 5; i++){
				String s = input.readLine();
				String name = s.substring(0, s.indexOf(' '));
				s = s.substring(s.indexOf(' ') + 1);
				int level = Integer.parseInt(s.substring(0, s.indexOf(' ')));
				s = s.substring(s.indexOf(' ') + 1);
				int current = Integer.parseInt(s.substring(0, s.indexOf(' ')));
				s = s.substring(s.indexOf(' ') + 1);
				int total = Integer.parseInt(s);
				players.add(new Player(name,level,current,total));
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(new File(fileLocation + "dice.txt")));
			for (int i = 0; i < 6; i++){
				String s = input.readLine();
				boolean visible = s.charAt(0) == 'o';
				int max = Integer.parseInt(s.substring(1, s.indexOf(' ')));
				s = s.substring(s.indexOf(' ') + 1);
				int current = Integer.parseInt(s);
				dice[i] = new Die(visible,max,current);
				if (visible){
					total += current;
				}
				
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(new File(fileLocation + "chat.txt")));
			String s = "";
			while ((s = input.readLine()) != null){
				String name = s.substring(0,s.indexOf(' '));
				s = s.substring(s.indexOf(' ') + 1);
				long time = Long.parseLong(s.substring(0,s.indexOf(' ')));
				s = s.substring(s.indexOf(' ') + 1);
				chatHistory.add(new Message(name, s, time));
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(new File(fileLocation + "time.txt")));
			time = input.readLine();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fileLocation += "images\\";
		
		File[] files = new File(fileLocation).listFiles();
		for (int i = 0; i < files.length; i++){
			try {
				pictures.add(new Picture(files[i].getName().substring(0,files[i].getName().length() - 4), ImageIO.read(files[i])));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
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
		String name = s.substring(0, s.indexOf(' '));
		s = s.substring(s.indexOf(' ') + 1);
		String id = s.substring(0,3);
		if (id.equals("DRW")){
			try{
				
				Color[] colors = {Color.black,Color.red,Color.blue,Color.yellow,Color.gray,Color.white,Color.green,new Color(255,125,0),new Color(255,0,255)};
				s = s.substring(4);
				int fx = Integer.parseInt(s.substring(0,s.indexOf(' ')));
				s = s.substring(s.indexOf(' ') + 1);
				int fy = Integer.parseInt(s.substring(0,s.indexOf(' ')));
				s = s.substring(s.indexOf(' ') + 1);
				
				int ix = Integer.parseInt(s.substring(0,s.indexOf(' ')));
				s = s.substring(s.indexOf(' ') + 1);
				int iy = Integer.parseInt(s.substring(0,s.indexOf(' ')));
				s = s.substring(s.indexOf(' ') + 1);
				int colorIndex = Integer.parseInt(s);
				Color c = null;
				if (colorIndex == 9){
					c = null;
				}
				else{
					c = colors[colorIndex];
				}
				try{
					int zoom;
					zoom = 4 - zoomScale;
					if (c == null){
						zoom *= 4;
					}
					if (fx == ix){
						if (fy < iy){
							int temp = fy;
							fy = iy;
							iy = temp;
						}
						for (int i = iy; i <= fy; i++){
							for (int x = fx - zoom; x <= fx + zoom; x++){
								try{
									drawing[x][i] = c;
								}catch(Exception e){}
							}
						}
					}
					else {
						if (fx < ix) {
							int temp = fx;
							fx = ix;
							ix = temp;
							temp = fy;
							fy = iy;
							iy = temp;
						}
						double slope = ((double)fy - (double)iy) / ((double)fx - (double)ix);
						double prevMax = iy;
						for (int i = ix; i <= fx; i++) {
							double start = prevMax;
							double end = iy + slope * (i - ix) + 0.5;
							prevMax = end;
							if (start > end){
								double temp = start;
								start = end;
								end = temp;
							}
							for (int j = (int)start; j <= end; j++){
								for (int x = i - zoom; x <= i + zoom; x++){
									for (int y = j - zoom; y <= j + zoom; y++){
										try{
											drawing[x][y] = c;
										}catch(Exception e){}
									}
								}
							}
						}
					}
				}catch(IndexOutOfBoundsException e){
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		else if (id.equals("CLR")){
			if (focus == null) return;
			drawing = new Color[focus.getImage().getWidth(null)][focus.getImage().getHeight(null)];
		}
		else if (id.equals("ROL")){
			roll();
			String diceString = " ";
			for (int i = 0; i < dice.length; i++){
				if (dice[i].isVisible()){
					diceString += "d[" + dice[i].getMax() + "] ";
				}
			}
			chatHistory.add(new Message("Server", name + " rolled the dice" + diceString + "for a total of " + total + ".", System.currentTimeMillis()));
		}
		else if (id.equals("TOT")){
			s = s.substring(4);
			dice[0].setCurrent(Integer.parseInt(s.substring(0,s.indexOf(' '))));
			s = s.substring(s.indexOf(' ') + 1);
			dice[1].setCurrent(Integer.parseInt(s.substring(0,s.indexOf(' '))));
			s = s.substring(s.indexOf(' ') + 1);
			dice[2].setCurrent(Integer.parseInt(s.substring(0,s.indexOf(' '))));
			s = s.substring(s.indexOf(' ') + 1);
			dice[3].setCurrent(Integer.parseInt(s.substring(0,s.indexOf(' '))));
			s = s.substring(s.indexOf(' ') + 1);
			dice[4].setCurrent(Integer.parseInt(s.substring(0,s.indexOf(' '))));
			s = s.substring(s.indexOf(' ') + 1);
			dice[5].setCurrent(Integer.parseInt(s));
			prevTotal = total;
			total = 0;
			for (int i = 0; i < dice.length; i++){
				if (dice[i].isVisible()){
					total += dice[i].getCurrent();
				}
			}
			chatHistory.add(new Message("Server", name + " rolled the dice for a total of " + total + ".", System.currentTimeMillis()));
			
		}
		else if (id.equals("SEL")){
			try{
				focus = pictures.get(Integer.parseInt(s.substring(4)));
				drawing = new Color[focus.getImage().getWidth(null)][focus.getImage().getHeight(null)];
				chatHistory.add(new Message("Server", name + " changed the focus to be " + focus.getName() + ".", System.currentTimeMillis()));
			}catch(Exception e){
				System.out.println("Error selecting focus");
			}
		}
		else if (id.equals("LVL")){
			try{
				players.get(Integer.parseInt(s.substring(4,5))).setLevel( Math.min(Math.max(Integer.parseInt(s.substring(6)), 1), 99));
				chatHistory.add(new Message("Server", name + " changed " + players.get(Integer.parseInt(s.substring(4,5))).getName() + "'s level to " + players.get(Integer.parseInt(s.substring(4,5))).getLevel() + ".", System.currentTimeMillis()));
			}catch(Exception e){}
		}
		else if (id.equals("DIE")){
			try{
				int index = Integer.parseInt(s.substring(4,5));
				if (s.charAt(6) == 'x'){
					dice[index].setVisible(false);
					chatHistory.add(new Message("Server", name + " removed Die " + (1 + index) + ".", System.currentTimeMillis()));
				}
				else if (s.charAt(6) == 'o'){
					dice[index].setVisible(true);
					chatHistory.add(new Message("Server", name + " created Die " + (1 + index) + ".", System.currentTimeMillis()));
				}
				else{
					dice[index].setMax( Math.min(Math.max(Integer.parseInt(s.substring(6)), 2), 99));
					chatHistory.add(new Message("Server", name + " changed the max value of Die " + (1 + index) + " to be " + dice[index].getMax() + ".", System.currentTimeMillis()));
				}
				
			}catch(Exception e){
				System.out.println("Error changing Die: " + s);
			}
		}
		else if (id.equals("CHP")){
			try{
				players.get(Integer.parseInt(s.substring(4,5))).setCurrentHp( Math.min(Math.max(Integer.parseInt(s.substring(6)), 0), players.get(Integer.parseInt(s.substring(4,5))).getTotalHp()));
				chatHistory.add(new Message("Server", name + " changed " + players.get(Integer.parseInt(s.substring(4,5))).getName() + "'s current HP to " + players.get(Integer.parseInt(s.substring(4,5))).getCurrentHp() + ".", System.currentTimeMillis()));
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
				chatHistory.add(new Message("Server", name + " changed " + players.get(Integer.parseInt(s.substring(4,5))).getName() + "'s max HP to " + players.get(Integer.parseInt(s.substring(4,5))).getTotalHp() + ".", System.currentTimeMillis()));
			}catch(Exception e){
				System.out.println("Error changing Max HP: " + s);
			}
		}
		else if (id.equals("TME")){
			try{
				String hours = s.substring(4,6);
				String minutes = s.substring(7,9);
				time = (Math.min(Math.max(Integer.parseInt(hours), 0), 23)) + ":" + Math.min(Math.max(Integer.parseInt(minutes), 0), 59);
				chatHistory.add(new Message("Server", name + " changed the time to " + time + ".", System.currentTimeMillis()));
			}catch(Exception e){
				System.out.println("Error changing Time: " + s);
			}
		}
		else if (id.equals("ZOO")){
			zoomScale = Math.min(Math.max(zoomScale + Integer.parseInt(s.substring(4)), 0), 4);
		}
		else if (id.equals("XCG")){
			picX = Math.min(Math.max(picX + Integer.parseInt(s.substring(4)) * (int)(16 / Math.pow(2,zoomScale)), 0), focus.getImage().getWidth(null));
		}
		else if (id.equals("YCG")){
			picY = Math.min(Math.max(picY + Integer.parseInt(s.substring(4)) * (int)(16 / Math.pow(2,zoomScale)), 0), focus.getImage().getHeight(null));
		}
		else if (id.equals("CHT")){
			chatHistory.add(new Message(name, s.substring(4), System.currentTimeMillis()));
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
		try {
			pictures.add(new Picture("Waldo", ImageIO.read(new File("C:\\Users\\Andrew\\Desktop\\waldo.jpg"))));
			pictures.add(new Picture("Waldo", ImageIO.read(new File("C:\\Users\\Andrew\\Desktop\\waldo2.jpg"))));
			pictures.add(new Picture("Waldo", ImageIO.read(new File("C:\\Users\\Andrew\\Desktop\\waldo3.jpg"))));
			pictures.add(new Picture("Waldo", ImageIO.read(new File("C:\\Users\\Andrew\\Desktop\\knuck.jpg"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
