package old;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class UI extends JPanel implements ActionListener{
	
	// Window
	public static final int MAIN_VERTICAL_BORDER = 620;
	public static final int HEALTH_VERTICAL_BORDER = 1200;
	public static final int FOCUS_VERTICAL_BORDER = 1270;
	public static final int INFO_HORIZONTAL_BORDER = 840;
	public static final int INFO_MID_BORDER = 960;
	public static final int BORDER_WIDTH = 10;
	public static final int HORIZONTAL_CHAT_BORDER = 420;
	public static final int FOCUS_BORDER = 170;
	public static final int SELECT_BOX_OFFSET = 175;
	public static final int SELECT_BOX_V_OFFSET = 100;
	
	public static final int ICON_GAP = 50;
	public static final int ICON_V_GAP = 50;
	public static final int ICON_WIDTH = 100;
	public static final int ICON_HEIGHT = 140;
	public static final int ICON_TEXT = 20;
	
	public static final Color DARK_BLUE = new Color(50,150,255);
	public static final Color LIGHT_BLUE = new Color(70,170,255);

	// Chat
	public static final int CHAT_HEIGHT = 40;
	
	// Dice
	public static final int DIE_SIZE = 200;
	
	// Health menu
	public static final int HEALTH_BAR_OFFSET = 50;
	public static final int HEALTH_BAR_V_OFFSET = 30;
	public static final int HEALTH_BAR_CIRCLE_OFFSET = 30;
	public static final int HEALTH_BAR_LENGTH = 250;
	public static final int HEALTH_BAR_V_GAP = 120;
	public static final int HEALTH_BAR_GAP = 350;
	public static final int HEALTH_TEXT_SPACE = 60;
	public static final int HEALTH_TEXT_V = 40;
	public static final int LEVEL_LENGTH = 50;
	
	// Dice menu
	public static final int ROLL_OFFSET = 20;
	public static final int ROLL_V_OFFSET = 20;
	public static final int ROLL_LENGTH = 110;
	public static final int VALUES_OFFSET = 500;
	public static final int VALUES_V_OFFSET = 20;
	
	// Main Menu
	public static final int QUEST_OFFSET = 20;
	public static final int QUEST_V_OFFSET = 20;
	public static final int QUEST_LENGTH = 150;
	public static final int TIME_OFFSET = 210;
	public static final int TIME_V_OFFSET = 20;
	public static final int TIME_LENGTH = 140;
	public static final int RESET_OFFSET = 390;
	public static final int RESET_V_OFFSET = 20;
	public static final int RESET_LENGTH = 150;
	
	// Chat
	public static final int CHAT_SIZE = 20;
	public static final int SERVER_SIZE = 15;
	public static final Color SERVER_COLOR = new Color(0,0,0);
	public static final int CHAT_OFFSET = 10;
	public static final int CHAT_V_OFFSET = 10;
	public static final int MESSAGE_GAP = 25;
	
	// Focus Menu
	public static final int SELECT_OFFSET = 20;
	public static final int SELECT_V_OFFSET = 40;
	public static final int SELECT_LENGTH = 200;
	public static final int LEFT_OFFSET = SELECT_OFFSET + SELECT_LENGTH + 50;
	public static final int LEFT_V_OFFSET = 60;
	public static final int LEFT_LENGTH = 40;
	public static final int RIGHT_OFFSET = SELECT_OFFSET + SELECT_LENGTH + 130;
	public static final int RIGHT_V_OFFSET = 60;
	public static final int RIGHT_LENGTH = 40;
	public static final int UP_OFFSET = SELECT_OFFSET + SELECT_LENGTH + 90;
	public static final int UP_V_OFFSET = 20;
	public static final int UP_LENGTH = 40;
	public static final int DOWN_OFFSET = SELECT_OFFSET + SELECT_LENGTH + 90;
	public static final int DOWN_V_OFFSET = 100;
	public static final int DOWN_LENGTH = 40;
	public static final int ZOOMIN_OFFSET = 440;
	public static final int ZOOMIN_V_OFFSET = 20;
	public static final int ZOOMIN_LENGTH = 60;
	public static final int ZOOMOUT_OFFSET = 440;
	public static final int ZOOMOUT_V_OFFSET = 80;
	public static final int ZOOMOUT_LENGTH = 60;
	public static final int PLUS_OFFSET = 550;
	public static final int PLUS_V_OFFSET = 20;
	public static final int PLUS_LENGTH = 60;
	
	public static final int UPLOAD_BOX_OFFSET = MAIN_VERTICAL_BORDER + 20;
	public static final int UPLOAD_BOX_V_OFFSET = FOCUS_BORDER;
	public static final int UPLOAD_BOX_LENGTH = FOCUS_VERTICAL_BORDER - MAIN_VERTICAL_BORDER - 40;
	public static final int UPLOAD_BOX_HEIGHT = 180;
	public static final int UPLOAD_FIELD_HEIGHT = 40;
	public static final int UPLOAD_FILE_V_OFFSET =  FOCUS_BORDER + 25;
	public static final int UPLOAD_NAME_V_OFFSET =  FOCUS_BORDER + 70;
	public static final int UPLOAD_TEXT_OFFSET =  FOCUS_BORDER + 115;
	public static final int UPLOAD_FIELD_OFFSET = 50;
	public static final int UPLOAD_BUTTON_BORDER = 400;
	
	public static final int RIGHTCLICK_WIDTH = 270;
	public static final int RIGHTCLICK_HEIGHT = 120;
	
	
	public static final String FONT = "BT BARNUM";
	
	JTextField nameField;
	JTextField chatField;
	JTextField uploadField;
	JTextField nameUploadField;
	
	Timer t;
	Picture file;
	int zoomScale;
	int picX, picY;
	
	boolean pen;
	boolean listenerCreated;
	
	public void makeClient(String ip){

		Client c = new Client(ip, 25569, getPlayerName(), this);
		if (!listenerCreated){
			new Listener(this,this,c);
			chatField.addActionListener(new ChatListener("CHT ", c));
			listenerCreated = true;
			validate();
		}
		else{
			Listener.setClient(c);
			chatField.removeAll();
			chatField.addActionListener(new ChatListener("CHT ", c));
			validate();
		}
	}
	
	private boolean canRoll(){
		Die[] dice = data.getDice();
		for (int i = 0; i < dice.length; i++){
			if (dice[i].isVisible()){
				return true;
			}
		}
		return false;
	}
	
	public Data getData(){
		return data;
	}
	
	public void finishMakeClient(){
		settingName = false;

		nameField.setVisible(false);
		chatField.setVisible(true);
		uploadField.setVisible(false);
		nameUploadField.setVisible(false);
		nameUploadField.setText("Name");
		uploadField.setText("File Path");
	}
	
	public UI(int width, int height, String ip){
		t = new Timer(1000/20, (ActionListener) this);
		pen = true;
		zoomScale = 1;
		picX = 0;
		picY = 0;
		setLayout(null);
		settingName = true;
		color = Color.black;
		
		nameField = new JTextField();
		chatField = new JTextField();
		uploadField = new JTextField();
		nameUploadField = new JTextField();
		
		nameField.setBounds(width/2 - 200,height/2 + 100,400,50);
		nameField.setFont(new Font(FONT, Font.PLAIN, 40));
		nameField.setHorizontalAlignment(JTextField.CENTER);
		nameField.addActionListener(new NameActionListener(this,name,ip));
		
		chatField.setBounds(BORDER_WIDTH + BORDER_WIDTH, height - BORDER_WIDTH - CHAT_HEIGHT, MAIN_VERTICAL_BORDER - 4 * BORDER_WIDTH, CHAT_HEIGHT);
		chatField.setFont(new Font(FONT, Font.PLAIN, 30));
		chatField.setBackground(new Color(90,190,255));
		chatField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
		uploadField.setFont(new Font(FONT, Font.PLAIN, 35));
		uploadField.setBackground(new Color(90,190,255));
		uploadField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
		nameUploadField.setFont(new Font(FONT, Font.PLAIN, 35));
		nameUploadField.setBackground(new Color(90,190,255));
		nameUploadField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
		add(nameField);
		add(chatField);
		add(uploadField);
		add(nameUploadField);
		
		nameField.setForeground(Color.black);
		chatField.setForeground(Color.black);
		uploadField.setForeground(Color.black);
		nameUploadField.setForeground(Color.black);
		
		nameField.setVisible(true);
		chatField.setVisible(false);
		uploadField.setVisible(false);
		nameUploadField.setVisible(false);
		this.validate();
		t.start();
	}
	
	private boolean uploading; // new UI pops up to upload an image and send it.
	private boolean choosing; // new UI pops up to choose the main focus of the group.
	private boolean choosingFocus;
	private boolean settingName; //Only a text field while the user sets up username.
	private Data data;
	private boolean error;
	
	private boolean rightClick;
	private int rightClickX;
	private int rightClickY;
	private Color color;
	private int bx, by;
	
	public void setData(Data data){
		this.data = data;
	}
	
	public void setDraw(int bx, int by){
		this.bx = bx;
		this.by = by;
	}
	
	public void createTextBox(int x, int y, int width, int height, ActionListener e){
		JTextField field = new JTextField();
		field.setBounds(x,y,width,height);
		field.setForeground(Color.black);
		field.setFont(new Font(FONT, Font.PLAIN, height));
		field.setHorizontalAlignment(JTextField.CENTER);
		if (x < MAIN_VERTICAL_BORDER){
			field.setBackground(Color.white);
		}
		else{
			field.setBackground(LIGHT_BLUE);
		}
		field.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		field.requestFocus();
		field.addFocusListener(new TextFieldFocusListener());
		field.addActionListener(e);
		add(field);
		field.requestFocusInWindow();
		field.setVisible(true);

		validate();
	}
	
	public void changeX(int x){
		if (file == null) return;
		picX = Math.min(Math.max(x + picX * (int)(16 / Math.pow(2,zoomScale)), 0), file.getImage().getWidth(null));
	}
	
	public void changeY(int y){
		if (file == null) return;
		picY = Math.min(Math.max(y + picY * (int)(16 / Math.pow(2,zoomScale)), 0), file.getImage().getHeight(null));
	}
	
	public void zoom(int z){
		zoomScale = Math.min(Math.max(zoomScale + z, 0), 4);
	}
	
	public void draw(Client c, int x, int y){
		if (choosing){
			return;
		}
		if (x < UI.FOCUS_VERTICAL_BORDER - UI.BORDER_WIDTH && y < UI.INFO_HORIZONTAL_BORDER - BORDER_WIDTH) {
			x -= UI.MAIN_VERTICAL_BORDER + UI.BORDER_WIDTH;
			bx -= UI.MAIN_VERTICAL_BORDER + UI.BORDER_WIDTH;
			y -= UI.FOCUS_BORDER + UI.BORDER_WIDTH;
			by -= UI.FOCUS_BORDER + UI.BORDER_WIDTH;
			int zoom = (int) (Math.pow(2, data.getZoomScale()));
			int colorCode = 0;

			if (color == null || !pen) {
				colorCode = 9;
			} else {

				if (color.equals(Color.black)) {
					colorCode = 0;
				}
				if (color.equals(Color.red)) {
					colorCode = 1;
				}
				if (color.equals(Color.blue)) {
					colorCode = 2;
				}
				if (color.equals(Color.yellow)) {
					colorCode = 3;
				}
				if (color.equals(Color.gray)) {
					colorCode = 4;
				}
				if (color.equals(Color.white)) {
					colorCode = 5;
				}
				if (color.equals(Color.green)) {
					colorCode = 6;
				}
				if (color.equals(new Color(255, 125, 0))) {
					colorCode = 7;
				}
				if (color.equals(new Color(255, 0, 255))) {
					colorCode = 8;
				}
			}

			c.sendString("DRW " + (x / zoom + data.getPicX()) + " " + (y / zoom + data.getPicY()) + " " + (bx / zoom + data.getPicX()) + " " + (by / zoom + data.getPicY()) + " " + colorCode);
			bx = x + UI.MAIN_VERTICAL_BORDER + UI.BORDER_WIDTH;
			by = y + UI.FOCUS_BORDER + UI.BORDER_WIDTH;
		}
	}
	
	public void sendRightClick(Client c, int x, int y){
		if (x > MAIN_VERTICAL_BORDER + BORDER_WIDTH && x < FOCUS_VERTICAL_BORDER - BORDER_WIDTH){
			if (y > FOCUS_BORDER && y < INFO_HORIZONTAL_BORDER - BORDER_WIDTH){
				rightClick = true;
				rightClickX = x;
				rightClickY = y;
			}
		}
	}
	
	public void sendClick(Client c, int x, int y){
		int xOff = 0;
		int yOff = 0;
		if (rightClick){
			if (x > rightClickX && x < rightClickX + RIGHTCLICK_WIDTH && y > rightClickY && y < rightClickY + RIGHTCLICK_HEIGHT){
				x -= rightClickX + BORDER_WIDTH;
				y -= rightClickY + BORDER_WIDTH;
				
				if (x < 0 || y < 0) return;
				
				int scale = (RIGHTCLICK_HEIGHT - 2*BORDER_WIDTH) / 2;
				
				int xIndex = x / scale;
				int yIndex = y / scale;
				
				if (xIndex == RIGHTCLICK_WIDTH / scale) return;
				if (yIndex == RIGHTCLICK_HEIGHT / scale) return;
				System.out.println(xIndex + " " + yIndex);
				
				if (yIndex == 0){
					if (xIndex == 1){
						color = Color.black;
					}
					if (xIndex == 2){
						color = Color.red;
					}
					if (xIndex == 3){
						color = Color.blue;
					}
					if (xIndex == 4){
						color = Color.yellow;
					}
				}
				if (yIndex == 1){
					if (xIndex == 0){
						color = Color.gray;
					}
					if (xIndex == 1){
						color = Color.white;
					}
					if (xIndex == 2){
						color = Color.green;
					}
					if (xIndex == 3){
						color = new Color(255,125,0);
					}
					if (xIndex == 4){
						color = new Color(255,0,255);
					}
				}
				
				rightClick = false;
				return;
			}
		}
		if (uploading){
			int tempX = x;
			tempX = x + MAIN_VERTICAL_BORDER - FOCUS_VERTICAL_BORDER;
			if (tempX > UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + UPLOAD_BUTTON_BORDER  + 10 && tempX < UPLOAD_BOX_LENGTH - UPLOAD_FIELD_OFFSET + UPLOAD_BOX_OFFSET){
				if (y > UPLOAD_TEXT_OFFSET && y < UPLOAD_TEXT_OFFSET + UPLOAD_FIELD_HEIGHT){
					String uploadName = nameUploadField.getText();
					if (uploadName.length() == 0 || uploadName.length() > 15){
						error = true;
						return;
					}
					String fileLocation = uploadField.getText();
					if (fileLocation.charAt(0) == '"'){
						fileLocation = fileLocation.substring(1, fileLocation.length() - 1);
					}
					if (!fileLocation.substring(fileLocation.length() - 4).equals(".png") && !fileLocation.substring(fileLocation.length() - 4).equals(".jpg")){
						error = true;
						return;
					}
					try{
						BufferedImage image = ImageIO.read(new File(fileLocation));
						c.sendPic(new Picture(uploadName, image));
					}catch(Exception e){
						error = true;
						return;
					}
				}
			}
		}
		if (choosing){
			if (x <  SELECT_BOX_OFFSET){
				chatField.setVisible(true);
				choosing = false;
				return;
			}
			if (x > getWidth() - SELECT_BOX_OFFSET){
				chatField.setVisible(true);
				choosing = false;
				return;
			}
			if (y <  SELECT_BOX_V_OFFSET){
				chatField.setVisible(true);
				choosing = false;
				return;
			}
			if (y >  getHeight() - SELECT_BOX_V_OFFSET){
				chatField.setVisible(true);
				choosing = false;
				return;
			}
			x -= SELECT_BOX_OFFSET + ICON_GAP - 20 + BORDER_WIDTH - 5;
			y -= SELECT_BOX_V_OFFSET + ICON_V_GAP - 20 + BORDER_WIDTH - 10;
			xOff += SELECT_BOX_OFFSET + ICON_GAP - 20 + BORDER_WIDTH - 5;
			yOff += SELECT_BOX_V_OFFSET + ICON_V_GAP - 20 + BORDER_WIDTH - 10;
			
			int i = x / (ICON_GAP + ICON_WIDTH);
			int j = y / (ICON_GAP + ICON_HEIGHT + ICON_TEXT);
			
			xOff += i * (ICON_GAP + ICON_WIDTH);
			yOff += j * (ICON_GAP + ICON_HEIGHT + ICON_TEXT);
			if (j*10 + i < data.getPictures().size()){
				if (choosingFocus){
					c.sendString("SEL " + (j*10 + i));
				}
				else{
					file = data.getPictures().get(j*10 + i);
				}
				choosing = false;
				chatField.setVisible(true);
			}
			return;
		}
		int startX = x;
		int startY = y;
		x -= BORDER_WIDTH;
		y -= BORDER_WIDTH;
		if (x < 0) return;
		if (y < 0) return;
		
		xOff = BORDER_WIDTH;
		yOff = BORDER_WIDTH;
		
		if (x < MAIN_VERTICAL_BORDER - BORDER_WIDTH){ // Left of main
			if (y < HORIZONTAL_CHAT_BORDER - BORDER_WIDTH){ // Focus on DICE
				if (y < DIE_SIZE){ // 0-2
					if (x < DIE_SIZE){ // 0
						if (data.getDice()[0].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									createTextBox(xOff + 5 + DIE_SIZE - Die.X_WIDTH, yOff + 5 + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH - 10, Die.X_WIDTH - 10, new TextListener("DIE 0 ",c));
								}
								if (x < Die.X_WIDTH){ // X
									c.sendString("DIE 0 x");
								}
							}
						}
						else{
							c.sendString("DIE 1 o");
						}
						return;
					}
					else if (x <  2 * DIE_SIZE){ // 1
						xOff += DIE_SIZE;
						x -= DIE_SIZE;
						if (data.getDice()[1].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									createTextBox(xOff + 5 + DIE_SIZE - Die.X_WIDTH, yOff + 5 + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH - 10, Die.X_WIDTH - 10, new TextListener("DIE 1 ",c));
								}
								if (x < Die.X_WIDTH){ // X
									c.sendString("DIE 1 x");
								}
							}
						}
						else{
							c.sendString("DIE 1 o");
						}
						return;
					}
					else { // 2
						xOff += 2*DIE_SIZE;
						x -= 2*DIE_SIZE;
						if (data.getDice()[2].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									createTextBox(xOff + 5 + DIE_SIZE - Die.X_WIDTH, yOff + 5 + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH - 10, Die.X_WIDTH - 10, new TextListener("DIE 2 ",c));
								}
								if (x < Die.X_WIDTH){ // X
									c.sendString("DIE 2 x");
								}
							}
						}
						else{
							c.sendString("DIE 2 o");
						}
						return;
					}
				}
				else{ // 3-5
					yOff += DIE_SIZE;
					y -= DIE_SIZE;
					if (x < DIE_SIZE){ // 3
						if (data.getDice()[3].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									createTextBox(xOff + 5 + DIE_SIZE - Die.X_WIDTH, yOff + 5 + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH - 10, Die.X_WIDTH - 10, new TextListener("DIE 3 ",c));
								}
								if (x < Die.X_WIDTH){ // X
									c.sendString("DIE 3 x");
								}
							}
						}
						else{
							c.sendString("DIE 3 o");
						}
						return;
					}
					else if (x <  2 * DIE_SIZE){ // 4
						xOff += DIE_SIZE;
						x -= DIE_SIZE;
						if (data.getDice()[4].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									createTextBox(xOff + 5 + DIE_SIZE - Die.X_WIDTH, yOff + 5 + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH - 10, Die.X_WIDTH - 10, new TextListener("DIE 4 ",c));
								}
								if (x < Die.X_WIDTH){ // X
									c.sendString("DIE 4 x");
								}
							}
						}
						else{
							c.sendString("DIE 4 o");
						}
						return;
					}
					else { // 5
						xOff += 2*DIE_SIZE;
						x -= 2*DIE_SIZE;
						if (data.getDice()[5].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									createTextBox(xOff + 5 + DIE_SIZE - Die.X_WIDTH, yOff + 5 + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH - 10, Die.X_WIDTH - 10, new TextListener("DIE 5 ",c));
								}
								if (x < Die.X_WIDTH){ // X
									c.sendString("DIE 5 x");
								}
							}
						}
						else{
							c.sendString("DIE 5 o");
						}
						return;
					}
				}
			}
			if (y > HORIZONTAL_CHAT_BORDER + BORDER_WIDTH){ // Focus on CHAT
				// Do Nothing
				return;
			}
		}
		if (x > MAIN_VERTICAL_BORDER + BORDER_WIDTH){ // Right of main
			if (y > INFO_HORIZONTAL_BORDER + BORDER_WIDTH){ //  bottom info bar.
				if (x > HEALTH_VERTICAL_BORDER + BORDER_WIDTH){ // health bars
					xOff += HEALTH_VERTICAL_BORDER;
					yOff += INFO_HORIZONTAL_BORDER;
					x -= HEALTH_VERTICAL_BORDER;
					y -= INFO_HORIZONTAL_BORDER;
					xOff += HEALTH_BAR_OFFSET;
					x -= HEALTH_BAR_OFFSET;
					yOff += HEALTH_BAR_V_OFFSET;
					y -= HEALTH_BAR_V_OFFSET;
					
					if (y - HEALTH_BAR_V_GAP / 2> -10 && y  - HEALTH_BAR_V_GAP / 2< 30){ // top
						if (x - HEALTH_BAR_GAP / 2 > 0 && x - HEALTH_BAR_GAP / 2 < HEALTH_BAR_LENGTH - LEVEL_LENGTH){
							//g.fillRect(xOff, yOff - 10, HEALTH_BAR_LENGTH - LEVEL_LENGTH, 33);
						}
						if (x - HEALTH_BAR_GAP / 2> HEALTH_BAR_LENGTH - LEVEL_LENGTH && x  - HEALTH_BAR_GAP / 2 < HEALTH_BAR_LENGTH){
							createTextBox(xOff + HEALTH_BAR_GAP / 2 + HEALTH_BAR_LENGTH - LEVEL_LENGTH, yOff + HEALTH_BAR_V_GAP / 2 - 5, LEVEL_LENGTH - 5, 28, new TextListener("LVL 4 ",c));
							return;
						}
					}
					if (y - HEALTH_BAR_V_GAP / 2 > 30 && y - HEALTH_BAR_V_GAP / 2 < 65){
						if (x - HEALTH_BAR_GAP / 2 > HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE && x - HEALTH_BAR_GAP / 2 < HEALTH_BAR_LENGTH / 2 - 10){
							createTextBox(xOff + HEALTH_BAR_GAP / 2 + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE + 5, yOff + HEALTH_BAR_V_GAP / 2 + 37, HEALTH_TEXT_SPACE - 10 - 5, 28 - 5, new TextListener("CHP 4 ",c));
							return;
						}
						if (x - HEALTH_BAR_GAP / 2 < HEALTH_BAR_LENGTH / 2 + HEALTH_TEXT_SPACE && x - HEALTH_BAR_GAP / 2 > HEALTH_BAR_LENGTH / 2 + 10){
							createTextBox(xOff + HEALTH_BAR_GAP / 2 + HEALTH_BAR_LENGTH / 2 + HEALTH_TEXT_SPACE + 10 - HEALTH_TEXT_SPACE, yOff + HEALTH_BAR_V_GAP / 2 + 37, HEALTH_TEXT_SPACE - 10 - 5, 28 - 5, new TextListener("MHP 4 ",c));
							return;
						}
					}
					
					int playerNum = 0;
					if (y < (getHeight() - INFO_HORIZONTAL_BORDER) / 2){ // top
						if (x < (getWidth() - HEALTH_VERTICAL_BORDER) / 2){ // top left
						}
						else{ // top right
							xOff += HEALTH_BAR_GAP;
							x -= HEALTH_BAR_GAP;
							playerNum = 2;
						}
					}
					else{ // bottom
						if (x < (getWidth() - HEALTH_VERTICAL_BORDER) / 2){ // bottom left
							yOff += HEALTH_BAR_V_GAP;
							y -= HEALTH_BAR_V_GAP;
							playerNum = 1;
						}
						else{ // bottom right
							xOff += HEALTH_BAR_GAP;
							x -= HEALTH_BAR_GAP;
							yOff += HEALTH_BAR_V_GAP;
							y -= HEALTH_BAR_V_GAP;
							
							playerNum = 3;
						}
					}
					
					if (y > -10 && y < 30){ // top
						if (x > 0 && x < HEALTH_BAR_LENGTH - LEVEL_LENGTH){
							//g.fillRect(xOff, yOff - 10, HEALTH_BAR_LENGTH - LEVEL_LENGTH, 33);
						}
						if (x > HEALTH_BAR_LENGTH - LEVEL_LENGTH && x < HEALTH_BAR_LENGTH){
							createTextBox(xOff + HEALTH_BAR_LENGTH - LEVEL_LENGTH, yOff - 5, LEVEL_LENGTH - 5, 28, new TextListener("LVL " + playerNum + " ",c));
						}
					}
					if (y > 30 && y < 65){
						if (x > HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE && x < HEALTH_BAR_LENGTH / 2 - 10){
							createTextBox(xOff + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE + 5, yOff + 37, HEALTH_TEXT_SPACE - 10 - 5, 28 - 5, new TextListener("CHP " + playerNum + " ",c));
						}
						if (x < HEALTH_BAR_LENGTH / 2 + HEALTH_TEXT_SPACE && x > HEALTH_BAR_LENGTH / 2 + 10){
							createTextBox(xOff + HEALTH_BAR_LENGTH / 2 + HEALTH_TEXT_SPACE + 10 - HEALTH_TEXT_SPACE, yOff + 37, HEALTH_TEXT_SPACE - 10 - 5, 28 - 5, new TextListener("MHP " + playerNum + " ",c));
						}
					}
					return;
				}
				if (x < HEALTH_VERTICAL_BORDER){ // left menus
					if ( y > INFO_MID_BORDER){ // NON_Dice
						
						x -= MAIN_VERTICAL_BORDER;
						y -= INFO_MID_BORDER;
						xOff += MAIN_VERTICAL_BORDER;
						yOff += INFO_MID_BORDER;
						
						if (x > QUEST_OFFSET && x < QUEST_OFFSET + QUEST_LENGTH && y > QUEST_V_OFFSET && y < QUEST_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - BORDER_WIDTH - QUEST_V_OFFSET){
							/*
							 * TODO
							 */
						}
						if (x > TIME_OFFSET && x < TIME_OFFSET + TIME_LENGTH && y > TIME_V_OFFSET && y < TIME_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - BORDER_WIDTH - TIME_V_OFFSET){
							createTextBox(xOff + TIME_OFFSET + 5, yOff + TIME_V_OFFSET + 5, TIME_LENGTH - 10, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * TIME_V_OFFSET - 10, new TextListener("TME ",c));
						}
						if (x > RESET_OFFSET && x < RESET_OFFSET + RESET_LENGTH && y > RESET_V_OFFSET && y < RESET_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - BORDER_WIDTH - RESET_V_OFFSET){
							c.sendString("RES");
						}
					}
					else{ // Dice
						x -= MAIN_VERTICAL_BORDER;
						y -= INFO_HORIZONTAL_BORDER;
						xOff += MAIN_VERTICAL_BORDER;
						yOff += INFO_HORIZONTAL_BORDER;
						
						if (x > ROLL_OFFSET && x < ROLL_OFFSET + ROLL_LENGTH && y > ROLL_V_OFFSET && y < ROLL_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - BORDER_WIDTH - ROLL_V_OFFSET){
							if (canRoll()){
								c.sendString("ROL");
							}
						}
					}
					return;
				}
			}
			else if (y < FOCUS_BORDER){ //  top "quarter"
				if (x > FOCUS_VERTICAL_BORDER){
					x -= FOCUS_VERTICAL_BORDER;
					xOff += FOCUS_VERTICAL_BORDER;
					if (x > SELECT_OFFSET && x < SELECT_OFFSET + SELECT_LENGTH && y > SELECT_V_OFFSET && y < SELECT_V_OFFSET + FOCUS_BORDER - BORDER_WIDTH - 2 *SELECT_V_OFFSET){
						choosing = true;
						uploading = false;
						nameUploadField.setVisible(false);
						uploadField.setVisible(false);
						choosingFocus = false;
						chatField.setVisible(false);
					}
					if (x > PLUS_OFFSET && x < PLUS_OFFSET + PLUS_LENGTH && y > PLUS_V_OFFSET && y < PLUS_V_OFFSET + FOCUS_BORDER - BORDER_WIDTH - BORDER_WIDTH - 2 *PLUS_V_OFFSET){
						if (uploading){
							uploading = false;
							nameUploadField.setVisible(false);
							uploadField.setVisible(false);
							nameUploadField.setText("Name");
							uploadField.setText("File Path");
						}
						else{
							int tempX = FOCUS_VERTICAL_BORDER - MAIN_VERTICAL_BORDER;
							nameUploadField.setVisible(true);
							uploadField.setVisible(true);
							nameUploadField.setBounds(tempX + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + 5,UPLOAD_NAME_V_OFFSET + 5, UPLOAD_BOX_LENGTH - 2 * UPLOAD_FIELD_OFFSET - 10, UPLOAD_FIELD_HEIGHT - 10);
							uploadField.setBounds(tempX  + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + 5,UPLOAD_FILE_V_OFFSET + 5, UPLOAD_BOX_LENGTH - 2 * UPLOAD_FIELD_OFFSET - 10, UPLOAD_FIELD_HEIGHT - 10);
							uploading = true;
						}
					}
					if (x > ZOOMIN_OFFSET && x < ZOOMIN_OFFSET + ZOOMIN_LENGTH && y > ZOOMIN_V_OFFSET && y < ZOOMIN_V_OFFSET + 60){
						zoom(1);
					}
					if (x > ZOOMOUT_OFFSET && x < ZOOMOUT_OFFSET + ZOOMOUT_LENGTH && y > ZOOMOUT_V_OFFSET && y < ZOOMOUT_V_OFFSET + 60){
						zoom(-1);
					}
					if (x > UP_OFFSET && x < UP_OFFSET + UP_LENGTH && y > UP_V_OFFSET && y < UP_V_OFFSET + 40){
						changeY(-1);
					}
					if (x > DOWN_OFFSET && x < DOWN_OFFSET + DOWN_LENGTH && y > DOWN_V_OFFSET && y < DOWN_V_OFFSET + 40){
						changeY(1);
					}
					if (x > RIGHT_OFFSET && x < RIGHT_OFFSET + RIGHT_LENGTH && y > RIGHT_V_OFFSET && y < RIGHT_V_OFFSET + 40){
						changeX(1);
					}
					if (x > LEFT_OFFSET && x < LEFT_OFFSET + LEFT_LENGTH && y > LEFT_V_OFFSET && y < LEFT_V_OFFSET + 40){
						changeX(-1);
					}
				}
				else{
					x -= MAIN_VERTICAL_BORDER;
					xOff += MAIN_VERTICAL_BORDER;
					if (x > SELECT_OFFSET && x < SELECT_OFFSET + SELECT_LENGTH && y > SELECT_V_OFFSET && y < SELECT_V_OFFSET + FOCUS_BORDER - BORDER_WIDTH - 2 *SELECT_V_OFFSET){
						choosing = true;
						uploading = false;
						nameUploadField.setVisible(false);
						uploadField.setVisible(false);
						choosingFocus = true;
						chatField.setVisible(false);
					}
					if (x > PLUS_OFFSET && x < PLUS_OFFSET + PLUS_LENGTH && y > PLUS_V_OFFSET && y < PLUS_V_OFFSET + FOCUS_BORDER - BORDER_WIDTH - BORDER_WIDTH - 2 *PLUS_V_OFFSET){
						if (x > PLUS_OFFSET && x < PLUS_OFFSET + PLUS_LENGTH && y > ZOOMOUT_V_OFFSET && y < ZOOMOUT_V_OFFSET + ZOOMOUT_LENGTH){
							pen = !pen;
						}
						else{
							c.sendString("CLR");
						}
					}
					if (x > ZOOMIN_OFFSET && x < ZOOMIN_OFFSET + ZOOMIN_LENGTH && y > ZOOMIN_V_OFFSET && y < ZOOMIN_V_OFFSET + 60){
						c.sendString("ZOO 1");
					}
					if (x > ZOOMOUT_OFFSET && x < ZOOMOUT_OFFSET + ZOOMOUT_LENGTH && y > ZOOMOUT_V_OFFSET && y < ZOOMOUT_V_OFFSET + 60){
						c.sendString("ZOO -1");
					}
					if (x > UP_OFFSET && x < UP_OFFSET + UP_LENGTH && y > UP_V_OFFSET && y < UP_V_OFFSET + 40){
						c.sendString("YCG -1");
					}
					if (x > DOWN_OFFSET && x < DOWN_OFFSET + DOWN_LENGTH && y > DOWN_V_OFFSET && y < DOWN_V_OFFSET + 40){
						c.sendString("YCG 1");
					}
					if (x > RIGHT_OFFSET && x < RIGHT_OFFSET + RIGHT_LENGTH && y > RIGHT_V_OFFSET && y < RIGHT_V_OFFSET + 40){
						c.sendString("XCG 1");
					}
					if (x > LEFT_OFFSET && x < LEFT_OFFSET + LEFT_LENGTH && y > LEFT_V_OFFSET && y < LEFT_V_OFFSET + 40){
						c.sendString("XCG -1");
					}
				}
				return;
			}
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (settingName) drawSettingName(g);
		if (data == null) return;
		else{
			drawMainWindowFrame(g);
			drawChat(g);
			drawDice(g);
			drawPlayers(g);
			drawDiceMenu(g);
			drawMainMenu(g);
			drawFocus(g);
			
			if (choosing){
				drawChoosing(g);
			}
			else if (uploading){
				drawUploading(g);
			}
			else if (rightClick){
				drawColorChart(g);
			}
			drawHighlighted(Listener.x, Listener.y, g);
			if (Listener.mouseButtons[0]) {
				drawHighlighted(Listener.x, Listener.y, g);
			}
		}
		
	}
	
	public void drawColorChart(Graphics g){
		int green = 40;
		int blue = 80;
		for (int i = 0; i < 11; i++){
			g.setColor(new Color(0,green,blue));
			g.drawRect(rightClickX + i, rightClickY + i, RIGHTCLICK_WIDTH - 2*i, RIGHTCLICK_HEIGHT -  2*i);
			green += 5;
			blue += 10;
		}
		
		int x = rightClickX + BORDER_WIDTH;
		int y = rightClickY + BORDER_WIDTH;
		int scale = (RIGHTCLICK_HEIGHT - 2*BORDER_WIDTH)/2;
		g.setColor(Color.lightGray);
		g.fillRect(x, y, scale, scale);
		g.setColor(Color.black);
		Font f = new Font(FONT, Font.PLAIN, scale);
		g.setFont(f);
		int center = centerString(scale,f,"x",g);
		g.drawString("x", x + center, y + scale - 10);
		x += scale;
		g.setColor(Color.black);
		g.fillRect(x, y, scale, scale);
		x += scale;
		g.setColor(Color.red);
		g.fillRect(x, y, scale, scale);
		x += scale;
		g.setColor(Color.blue);
		g.fillRect(x, y, scale, scale);
		x += scale;
		g.setColor(Color.yellow);
		g.fillRect(x, y, scale, scale);
		x = rightClickX + BORDER_WIDTH;
		y += scale;
		g.setColor(Color.gray);
		g.fillRect(x, y, scale, scale);
		x += scale;
		g.setColor(Color.white);
		g.fillRect(x, y, scale, scale);
		x += scale;
		g.setColor(Color.green);
		g.fillRect(x, y, scale, scale);
		x += scale;
		g.setColor(new Color(255,125,0));
		g.fillRect(x, y, scale, scale);
		x += scale;
		g.setColor(new Color(255,0,255));
		g.fillRect(x, y, scale, scale);
	}
	
	public void drawUploading(Graphics g){
		
		int x = 0;
		x = FOCUS_VERTICAL_BORDER - MAIN_VERTICAL_BORDER;
		
		int green = 128 - 41;
		int blue = 255 - 82;
		g.setColor(new Color(0,green,blue));
		g.fillRect(x + UPLOAD_BOX_OFFSET, UPLOAD_BOX_V_OFFSET, UPLOAD_BOX_LENGTH, UPLOAD_BOX_HEIGHT);
		for (int i = 0; i < 40; i++){
			green += 1;
			blue += 2;
			g.setColor(new Color(0,green,blue));
			g.fillOval(x + UPLOAD_BOX_OFFSET + BORDER_WIDTH + i*16, UPLOAD_BOX_V_OFFSET + BORDER_WIDTH + i*9,UPLOAD_BOX_LENGTH -i*32 - 2*BORDER_WIDTH, UPLOAD_BOX_HEIGHT -i*18 - 2*BORDER_WIDTH);
		}
		green = 40;
		blue = 80;
		for (int i = 0; i < 11; i++){
			g.setColor(new Color(0,green,blue));
			g.drawRect(x + UPLOAD_BOX_OFFSET + i, UPLOAD_BOX_V_OFFSET + i, UPLOAD_BOX_LENGTH - 2*i, UPLOAD_BOX_HEIGHT -  2*i);
			g.drawRect(x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + i, UPLOAD_NAME_V_OFFSET + i, UPLOAD_BOX_LENGTH - 2*i - 2 * UPLOAD_FIELD_OFFSET, UPLOAD_FIELD_HEIGHT -  2*i);
			g.drawRect(x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + i, UPLOAD_FILE_V_OFFSET + i, UPLOAD_BOX_LENGTH - 2*i - 2 * UPLOAD_FIELD_OFFSET, UPLOAD_FIELD_HEIGHT -  2*i);
			green += 5;
			blue += 10;
		}
		g.setColor(DARK_BLUE);
		g.fillRect(x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET, UPLOAD_NAME_V_OFFSET, UPLOAD_BOX_LENGTH - 2 * UPLOAD_FIELD_OFFSET, UPLOAD_FIELD_HEIGHT);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + 5, UPLOAD_NAME_V_OFFSET + 5, UPLOAD_BOX_LENGTH - 2 * UPLOAD_FIELD_OFFSET - 10, UPLOAD_FIELD_HEIGHT - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET, UPLOAD_FILE_V_OFFSET, UPLOAD_BOX_LENGTH - 2 * UPLOAD_FIELD_OFFSET, UPLOAD_FIELD_HEIGHT);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + 5, UPLOAD_FILE_V_OFFSET + 5, UPLOAD_BOX_LENGTH - 2 * UPLOAD_FIELD_OFFSET - 10, UPLOAD_FIELD_HEIGHT - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET, UPLOAD_TEXT_OFFSET, UPLOAD_BUTTON_BORDER - 10, UPLOAD_FIELD_HEIGHT);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + 5, UPLOAD_TEXT_OFFSET + 5, UPLOAD_BUTTON_BORDER - 20, UPLOAD_FIELD_HEIGHT - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + UPLOAD_BUTTON_BORDER + 10, UPLOAD_TEXT_OFFSET, UPLOAD_BOX_LENGTH - UPLOAD_BUTTON_BORDER - 2 * UPLOAD_FIELD_OFFSET - 10, UPLOAD_FIELD_HEIGHT);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + UPLOAD_BUTTON_BORDER + 15, UPLOAD_TEXT_OFFSET + 5,  UPLOAD_BOX_LENGTH - UPLOAD_BUTTON_BORDER - 2 * UPLOAD_FIELD_OFFSET - 20, UPLOAD_FIELD_HEIGHT - 10);
		
		String s = "";
		if (error){
			s += "Error: ";
		}
		s += "Choose File and Name";
		
		g.setColor(Color.black);
		Font f = new Font(FONT, Font.PLAIN, UPLOAD_FIELD_HEIGHT - 15);
		g.setFont(f);
		int center = centerString(UPLOAD_BUTTON_BORDER - 10, f, s, g);
		g.drawString(s,center + x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + 5, UPLOAD_TEXT_OFFSET + UPLOAD_FIELD_HEIGHT - 10);
		
		s = "Upload";
		g.setColor(Color.black);
		f = new Font(FONT, Font.PLAIN, UPLOAD_FIELD_HEIGHT - 15);
		g.setFont(f);
		center = centerString(UPLOAD_BOX_LENGTH - UPLOAD_BUTTON_BORDER - 2 * UPLOAD_FIELD_OFFSET - 20, f, s, g);
		g.drawString(s,center + x + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + UPLOAD_BUTTON_BORDER + 15, UPLOAD_TEXT_OFFSET + UPLOAD_FIELD_HEIGHT - 10);
		
	}
	
	public void drawChoosing(Graphics g){
		Color c = new Color(0,0,0,200);
		g.setColor(c);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int green = 128 - 41;
		int blue = 255 - 82;
		g.setColor(new Color(0,green,blue));
		g.fillRect(SELECT_BOX_OFFSET, SELECT_BOX_V_OFFSET, getWidth() - 2 * SELECT_BOX_OFFSET, getHeight() - 2 * SELECT_BOX_V_OFFSET);
		for (int i = 0; i < 40; i++){
			green += 1;
			blue += 2;
			g.setColor(new Color(0,green,blue));
			g.fillOval(SELECT_BOX_OFFSET + BORDER_WIDTH + i*16, SELECT_BOX_V_OFFSET + BORDER_WIDTH + i*9,getWidth()-i*32 - 2*BORDER_WIDTH - 2 * SELECT_BOX_OFFSET, getHeight()-i*18 - 2*BORDER_WIDTH - 2 * SELECT_BOX_V_OFFSET);
		}
		green = 40;
		blue = 80;
		for (int i = 0; i < 11; i++){
			g.setColor(new Color(0,green,blue));
			g.drawRect(SELECT_BOX_OFFSET + i, SELECT_BOX_V_OFFSET + i, getWidth() - 2 * SELECT_BOX_OFFSET - 2*i, getHeight() - 2 * SELECT_BOX_V_OFFSET -  2*i);
			green += 5;
			blue += 10;
		}
		
		int x = SELECT_BOX_OFFSET + BORDER_WIDTH;
		int y = SELECT_BOX_V_OFFSET + BORDER_WIDTH;
		
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 10; i++) {
				if (j * 10 + i < data.getPictures().size()){
					g.setColor(DARK_BLUE);
					g.fillRect(x + ICON_GAP + i * (ICON_GAP + ICON_WIDTH) - 20, y  - 20 + ICON_V_GAP + j * (ICON_V_GAP + ICON_HEIGHT + ICON_TEXT), ICON_WIDTH + 40, ICON_TEXT + 10);
					g.setColor(LIGHT_BLUE);
					g.fillRect(x + ICON_GAP + i * (ICON_GAP + ICON_WIDTH) + 5 - 20, y - 20 + ICON_GAP + j * (ICON_GAP + ICON_HEIGHT) + 5, ICON_WIDTH - 10 + 40, ICON_TEXT);
					
					g.setColor(Color.black);
					Font f = new Font(FONT, Font.PLAIN, ICON_TEXT);
					g.setFont(f);
					String s = data.getPictures().get(j*10 + i).getName();
					int center = centerString(ICON_WIDTH + 30, f, s, g);
					g.drawString(s,x + ICON_GAP + i * (ICON_GAP + ICON_WIDTH) + 5 - 20 + center, y - 20 + ICON_GAP + j * (ICON_GAP + ICON_HEIGHT) + 5 + ICON_TEXT - 2);
					g.drawImage(data.getPictures().get(j*10 + i).getImage(),x + ICON_GAP + i * (ICON_GAP + ICON_WIDTH), y - 20 + ICON_V_GAP + ICON_TEXT + 15 + j * (ICON_V_GAP + ICON_HEIGHT + ICON_TEXT), x + ICON_GAP + i * (ICON_GAP + ICON_WIDTH) + ICON_WIDTH, y - 20 + ICON_V_GAP + ICON_TEXT + 15 + j * (ICON_V_GAP + ICON_HEIGHT + ICON_TEXT) + ICON_HEIGHT, 0, 0, Math.min(data.getPictures().get(j*10 + i).getImage().getWidth(null), ICON_WIDTH*100) ,Math.min(data.getPictures().get(j*10 + i).getImage().getHeight(null), ICON_HEIGHT*100), null);
				}
			}
		}
	}
	
	public void drawFocus(Graphics g){
		int x = BORDER_WIDTH + MAIN_VERTICAL_BORDER;
		int y = BORDER_WIDTH;
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + SELECT_OFFSET, y + SELECT_V_OFFSET, SELECT_LENGTH, FOCUS_BORDER - BORDER_WIDTH - 2 * SELECT_V_OFFSET );
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + SELECT_OFFSET + 5, y + SELECT_V_OFFSET + 5, SELECT_LENGTH - 10, FOCUS_BORDER - BORDER_WIDTH - 2 * SELECT_V_OFFSET - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + UP_OFFSET, y + UP_V_OFFSET, UP_LENGTH, UP_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + UP_OFFSET + 5, y + UP_V_OFFSET + 5, UP_LENGTH - 10, UP_LENGTH - 10);

		g.setColor(DARK_BLUE);
		g.fillRect(x + DOWN_OFFSET, y + DOWN_V_OFFSET, DOWN_LENGTH, DOWN_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + DOWN_OFFSET + 5, y + DOWN_V_OFFSET + 5, DOWN_LENGTH - 10, DOWN_LENGTH - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + RIGHT_OFFSET, y + RIGHT_V_OFFSET, RIGHT_LENGTH, RIGHT_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + RIGHT_OFFSET + 5, y + RIGHT_V_OFFSET + 5, RIGHT_LENGTH - 10, RIGHT_LENGTH - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + LEFT_OFFSET, y + LEFT_V_OFFSET, LEFT_LENGTH, LEFT_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + LEFT_OFFSET + 5, y + LEFT_V_OFFSET + 5, LEFT_LENGTH - 10, LEFT_LENGTH - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + ZOOMIN_OFFSET, y + ZOOMIN_V_OFFSET, ZOOMIN_LENGTH, ZOOMIN_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + ZOOMIN_OFFSET + 5, y + ZOOMIN_V_OFFSET + 5, ZOOMIN_LENGTH - 10, ZOOMIN_LENGTH - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + ZOOMOUT_OFFSET, y + ZOOMOUT_V_OFFSET, ZOOMOUT_LENGTH, ZOOMOUT_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + ZOOMOUT_OFFSET + 5, y + ZOOMOUT_V_OFFSET + 5, ZOOMOUT_LENGTH - 10, ZOOMOUT_LENGTH - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + PLUS_OFFSET, y + ZOOMIN_V_OFFSET, ZOOMOUT_LENGTH, ZOOMOUT_LENGTH );
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + PLUS_OFFSET + 5, y + ZOOMIN_V_OFFSET + 5, ZOOMOUT_LENGTH - 10, ZOOMOUT_LENGTH - 10);

		g.setColor(DARK_BLUE);
		g.fillRect(x + PLUS_OFFSET, y + ZOOMOUT_V_OFFSET, ZOOMOUT_LENGTH, ZOOMOUT_LENGTH );
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + PLUS_OFFSET + 5, y + ZOOMOUT_V_OFFSET + 5, ZOOMOUT_LENGTH - 10, ZOOMOUT_LENGTH - 10);
		
		Font temp = new Font(FONT, Font.PLAIN, 50);
		g.setFont(temp);
		String s;
		int center;
		
		g.setColor(Color.black);
		s = "Select";
		center = centerString(SELECT_LENGTH, temp, s, g);
		g.drawString(s, x + SELECT_OFFSET + center, y + SELECT_V_OFFSET + FOCUS_BORDER - BORDER_WIDTH - BORDER_WIDTH - 2 * SELECT_V_OFFSET - 12);
		
		if (pen){
			g.fillOval(x + PLUS_OFFSET + 14, y + 95, 30, 30);
		}
		else{
			g.fillOval(x + PLUS_OFFSET + 14, y + 95, 30, 30);
			g.setColor(LIGHT_BLUE);
			g.fillOval(x + PLUS_OFFSET + 14 + 3, y + 95 + 3, 30 - 6, 30 - 6);
		}
		
		g.setColor(Color.black);
		temp = new Font(FONT, Font.PLAIN, 50);
		g.setFont(temp);
		s = "x";
		center = centerString(PLUS_LENGTH, temp, s, g);
		g.drawString(s, x + PLUS_OFFSET + center, y + 63);
		
		s = "+";
		center = centerString(ZOOMIN_LENGTH, temp, s, g);
		g.drawString(s, x + ZOOMIN_OFFSET + center - 1, y + 66);
		s = "-";
		center = centerString(ZOOMOUT_LENGTH, temp, s, g);
		g.drawString(s, x + ZOOMOUT_OFFSET + center - 1, y + 123);
		
		x = FOCUS_VERTICAL_BORDER + BORDER_WIDTH;
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + SELECT_OFFSET, y + SELECT_V_OFFSET, SELECT_LENGTH, FOCUS_BORDER - BORDER_WIDTH - 2 * SELECT_V_OFFSET );
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + SELECT_OFFSET + 5, y + SELECT_V_OFFSET + 5, SELECT_LENGTH - 10, FOCUS_BORDER - BORDER_WIDTH - 2 * SELECT_V_OFFSET - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + UP_OFFSET, y + UP_V_OFFSET, UP_LENGTH, UP_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + UP_OFFSET + 5, y + UP_V_OFFSET + 5, UP_LENGTH - 10, UP_LENGTH - 10);

		g.setColor(DARK_BLUE);
		g.fillRect(x + DOWN_OFFSET, y + DOWN_V_OFFSET, DOWN_LENGTH, DOWN_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + DOWN_OFFSET + 5, y + DOWN_V_OFFSET + 5, DOWN_LENGTH - 10, DOWN_LENGTH - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + RIGHT_OFFSET, y + RIGHT_V_OFFSET, RIGHT_LENGTH, RIGHT_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + RIGHT_OFFSET + 5, y + RIGHT_V_OFFSET + 5, RIGHT_LENGTH - 10, RIGHT_LENGTH - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + LEFT_OFFSET, y + LEFT_V_OFFSET, LEFT_LENGTH, LEFT_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + LEFT_OFFSET + 5, y + LEFT_V_OFFSET + 5, LEFT_LENGTH - 10, LEFT_LENGTH - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + ZOOMIN_OFFSET, y + ZOOMIN_V_OFFSET, ZOOMIN_LENGTH, ZOOMIN_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + ZOOMIN_OFFSET + 5, y + ZOOMIN_V_OFFSET + 5, ZOOMIN_LENGTH - 10, ZOOMIN_LENGTH - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + ZOOMOUT_OFFSET, y + ZOOMOUT_V_OFFSET, ZOOMOUT_LENGTH, ZOOMOUT_LENGTH);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + ZOOMOUT_OFFSET + 5, y + ZOOMOUT_V_OFFSET + 5, ZOOMOUT_LENGTH - 10, ZOOMOUT_LENGTH - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + PLUS_OFFSET, y + PLUS_V_OFFSET, PLUS_LENGTH, FOCUS_BORDER - BORDER_WIDTH - 2 * PLUS_V_OFFSET );
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + PLUS_OFFSET + 5, y + PLUS_V_OFFSET + 5, PLUS_LENGTH - 10, FOCUS_BORDER - BORDER_WIDTH - 2 * PLUS_V_OFFSET - 10);
		
		temp = new Font(FONT, Font.PLAIN, 50);
		g.setFont(temp);
		
		g.setColor(Color.black);
		s = "Select";
		center = centerString(SELECT_LENGTH, temp, s, g);
		g.drawString(s, x + SELECT_OFFSET + center, y + SELECT_V_OFFSET + FOCUS_BORDER - BORDER_WIDTH - BORDER_WIDTH - 2 * SELECT_V_OFFSET - 12);
		
		temp = new Font(FONT, Font.PLAIN, 30);
		g.setFont(temp);
		
		g.setColor(Color.black);
		s = "UP";
		center = centerString(PLUS_LENGTH, temp, s, g);
		g.drawString(s, x + PLUS_OFFSET + center, y + 120);
		
		temp = new Font(FONT, Font.PLAIN, 50);
		g.setFont(temp);
		if (uploading){
			s = "x";
		}
		else{
			s = "+";
		}
		center = centerString(PLUS_LENGTH, temp, s, g);
		g.drawString(s, x + PLUS_OFFSET + center, y + 70);
		
		s = "+";
		center = centerString(ZOOMIN_LENGTH, temp, s, g);
		g.drawString(s, x + ZOOMIN_OFFSET + center - 1, y + 66);
		s = "-";
		center = centerString(ZOOMOUT_LENGTH, temp, s, g);
		g.drawString(s, x + ZOOMOUT_OFFSET + center - 1, y + 123);
		
		
		
		x = BORDER_WIDTH + MAIN_VERTICAL_BORDER;
		y = BORDER_WIDTH + FOCUS_BORDER;
		if (data.getFocus() != null){ // 
			data.getFocus().draw(data.getDrawing(), x, y, FOCUS_VERTICAL_BORDER - MAIN_VERTICAL_BORDER - 2 * BORDER_WIDTH, INFO_HORIZONTAL_BORDER - FOCUS_BORDER - 2 * BORDER_WIDTH, g, data.getZoomScale(), data.getPicX(), data.getPicY());
		}
		
		x = BORDER_WIDTH + FOCUS_VERTICAL_BORDER;
		
		if (file != null){ // left
			file.draw(x, y, FOCUS_VERTICAL_BORDER - MAIN_VERTICAL_BORDER - 2 * BORDER_WIDTH, INFO_HORIZONTAL_BORDER - FOCUS_BORDER - 2 * BORDER_WIDTH, g, zoomScale, picX, picY);
		}
		
	}
	
	public void drawChat(Graphics g){
		Vector<Message> messages = data.getChatHistory();
		int x = BORDER_WIDTH + 5 + CHAT_OFFSET;
		int y = getHeight() - CHAT_HEIGHT - BORDER_WIDTH - 5 -  CHAT_V_OFFSET;
		
		int heightOfText = 0;
		int index = messages.size() - 1;
		Font f;
		String s;
		String sender;
		g.setColor(Color.black);
		
		while (y > HORIZONTAL_CHAT_BORDER + BORDER_WIDTH){
			if (index < 0) return;
			s = messages.get(index).getChat();
			sender = messages.get(index).getSender() + " (" + messages.get(index).getTime().toString() + ")";
			
			if (messages.get(index).getSender().equals("Server")){
				f = new Font(FONT, Font.PLAIN, SERVER_SIZE);
			}
			else{
				f = new Font(FONT, Font.PLAIN, CHAT_SIZE);
			}
			
			ArrayList<String> lines = new ArrayList<String>();
			while(true){
				while (true){
					if (s.charAt(0) == ' '){
						s = s.substring(1);
					}
					else{
						break;
					}
				}
				g.setColor(Color.black);
				int j = 0;
				while ((int)(g.getFontMetrics(f).getStringBounds(s.substring(0, j), g).getWidth()) < (MAIN_VERTICAL_BORDER - 2 * BORDER_WIDTH - 2 * CHAT_OFFSET)){
					j++;
					if (j > s.length()){
						break;
					}
				}
				if (j > s.length()){
					lines.add(s);
					break;
				}
				int cutOff = s.substring(0,j).lastIndexOf(' ');
				if (cutOff == -1){
					cutOff = j;
				}
				lines.add(s.substring(0,cutOff));
				s = s.substring(cutOff);
			}

			heightOfText = SERVER_SIZE;
			int scale = CHAT_SIZE;
			g.setFont(new Font(FONT, Font.PLAIN, CHAT_SIZE));
			
			if (messages.get(index).getSender().equals("Server")){
				scale = SERVER_SIZE;
				g.setFont(new Font(FONT, Font.PLAIN, SERVER_SIZE));
			}
			heightOfText += scale*lines.size();

			for (int i = lines.size() - 1; i >= 0; i--){
				if (y - scale < HORIZONTAL_CHAT_BORDER + BORDER_WIDTH + CHAT_V_OFFSET){
					return;
				}
				g.drawString(lines.get(i), x, y);
				y -= scale;
			}
			
			if (y - SERVER_SIZE < HORIZONTAL_CHAT_BORDER + BORDER_WIDTH + CHAT_V_OFFSET){
				return;
			}
			g.setColor(SERVER_COLOR);
			g.setFont(new Font(FONT, Font.PLAIN, SERVER_SIZE));
			g.drawString(sender, x, y);
			
			y -= MESSAGE_GAP;
			index--;
		}
		
	}
	
	public void drawMainMenu(Graphics g){
		int y = INFO_MID_BORDER + BORDER_WIDTH;
		int x = MAIN_VERTICAL_BORDER + BORDER_WIDTH;
		g.setColor(DARK_BLUE);
		g.fillRect(x + QUEST_OFFSET, y + QUEST_V_OFFSET, QUEST_LENGTH, getHeight() - INFO_MID_BORDER - 2 * BORDER_WIDTH - 2 * QUEST_V_OFFSET);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + QUEST_OFFSET + 5, y + QUEST_V_OFFSET + 5, QUEST_LENGTH - 10, getHeight() - INFO_MID_BORDER - 2 * BORDER_WIDTH - 2 * QUEST_V_OFFSET - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + TIME_OFFSET, y + TIME_V_OFFSET, TIME_LENGTH, getHeight() - INFO_MID_BORDER - 2 * BORDER_WIDTH - 2 * TIME_V_OFFSET);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + TIME_OFFSET + 5, y + TIME_V_OFFSET + 5, TIME_LENGTH - 10, getHeight() - INFO_MID_BORDER - 2 * BORDER_WIDTH - 2 * TIME_V_OFFSET - 10);
		
		g.setColor(DARK_BLUE);
		g.fillRect(x + RESET_OFFSET, y + RESET_V_OFFSET, RESET_LENGTH, getHeight() - INFO_MID_BORDER - 2 * BORDER_WIDTH - 2 * RESET_V_OFFSET);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + RESET_OFFSET + 5, y + RESET_V_OFFSET + 5, RESET_LENGTH - 10, getHeight() - INFO_MID_BORDER - 2 * BORDER_WIDTH - 2 * RESET_V_OFFSET - 10);
		
		Font temp = new Font(FONT, Font.PLAIN, 50);
		g.setFont(temp);
		String s;
		int center;
		
		g.setColor(Color.black);
		s = "Quest";
		center = centerString(QUEST_LENGTH, temp, s, g);
		g.drawString(s, x + QUEST_OFFSET + center, y + QUEST_V_OFFSET + getHeight() - INFO_MID_BORDER - 2 * BORDER_WIDTH - 2 * QUEST_V_OFFSET - 12);
		
		s = data.getTime();
		center = centerString(TIME_LENGTH, temp, s, g);
		g.drawString(s, x + TIME_OFFSET + center, y + TIME_V_OFFSET + getHeight() - INFO_MID_BORDER - 2 * BORDER_WIDTH - 2 * TIME_V_OFFSET - 12);
		
		s = "Reset";
		center = centerString(RESET_LENGTH, temp, s, g);
		g.drawString(s, x + RESET_OFFSET + center, y + RESET_V_OFFSET + getHeight() - INFO_MID_BORDER - 2 * BORDER_WIDTH - 2 * RESET_V_OFFSET - 12);
		
		
	}
	
	public void drawDiceMenu(Graphics g){
		int x = MAIN_VERTICAL_BORDER + BORDER_WIDTH;
		int y = INFO_HORIZONTAL_BORDER + BORDER_WIDTH;
		g.setColor(DARK_BLUE);
		g.fillRect(x + ROLL_OFFSET, y + ROLL_V_OFFSET, ROLL_LENGTH, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * ROLL_V_OFFSET);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + ROLL_OFFSET + 5, y + ROLL_V_OFFSET + 5, ROLL_LENGTH - 10, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * ROLL_V_OFFSET - 10);
		
		Font temp = new Font(FONT, Font.PLAIN, 50);
		g.setFont(temp);
		String s;
		int center;
		
		g.setColor(Color.black);
		s = "Roll";
		center = centerString(ROLL_LENGTH, temp, s, g);
		g.drawString(s, x + ROLL_OFFSET + center, y + ROLL_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * ROLL_V_OFFSET - 12);
		
		x += ROLL_LENGTH + ROLL_OFFSET * 2;
		g.setColor(DARK_BLUE);
		g.fillRect(x + ROLL_OFFSET, y + ROLL_V_OFFSET, ROLL_LENGTH * 3 / 2, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * ROLL_V_OFFSET);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + ROLL_OFFSET + 5, y + ROLL_V_OFFSET + 5, ROLL_LENGTH * 3 / 2 - 10, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * ROLL_V_OFFSET - 10);
		
		g.setColor(Color.black);
		temp = new Font(FONT, Font.PLAIN, 40);
		g.setFont(temp);
		s = "Total " + data.getTotal();
		center = centerString(ROLL_LENGTH * 3 /2, temp, s, g);
		g.drawString(s, x + ROLL_OFFSET + center, y + ROLL_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * ROLL_V_OFFSET - 12);
		
		x += ROLL_LENGTH * 3 / 2 + ROLL_OFFSET * 2;
		g.setColor(DARK_BLUE);
		g.fillRect(x + ROLL_OFFSET, y + ROLL_V_OFFSET, ROLL_LENGTH * 3 / 2, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * ROLL_V_OFFSET);
		g.setColor(LIGHT_BLUE);
		g.fillRect(x + ROLL_OFFSET + 5, y + ROLL_V_OFFSET + 5, ROLL_LENGTH * 3 / 2 - 10, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * ROLL_V_OFFSET - 10);
		
		g.setColor(Color.black);
		temp = new Font(FONT, Font.PLAIN, 40);
		g.setFont(temp);
		s = "Prev " + data.getPrevTotal();
		center = centerString(ROLL_LENGTH * 3 /2, temp, s, g);
		g.drawString(s, x + ROLL_OFFSET + center, y + ROLL_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * ROLL_V_OFFSET - 12);
	}
	
	public void drawPlayers(Graphics g){
		int x = HEALTH_VERTICAL_BORDER + BORDER_WIDTH;
		int y = INFO_HORIZONTAL_BORDER + BORDER_WIDTH;
		
		
		g.setColor(DARK_BLUE); // blue
		g.fillRect(x + HEALTH_BAR_OFFSET, y + HEALTH_BAR_V_OFFSET - 10, HEALTH_BAR_LENGTH, 40);
		g.fillRect(x + HEALTH_BAR_OFFSET, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP - 10, HEALTH_BAR_LENGTH, 40);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET - 10, HEALTH_BAR_LENGTH, 40);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP - 10, HEALTH_BAR_LENGTH, 40);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_GAP / 2, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP / 2 - 10, HEALTH_BAR_LENGTH, 40);
		
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_TEXT_V, 2 * HEALTH_TEXT_SPACE, 35);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP - 10 + HEALTH_TEXT_V, 2 * HEALTH_TEXT_SPACE, 35);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_TEXT_V, 2 * HEALTH_TEXT_SPACE, 35);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP - 10 + HEALTH_TEXT_V, 2 * HEALTH_TEXT_SPACE, 35);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE + HEALTH_BAR_GAP / 2, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP / 2 - 10 + HEALTH_TEXT_V, 2 * HEALTH_TEXT_SPACE, 35);
		
		g.setColor(LIGHT_BLUE); // light blue
		g.fillRect(x + HEALTH_BAR_OFFSET + 5, y + HEALTH_BAR_V_OFFSET - 5, HEALTH_BAR_LENGTH - 10, 30);
		g.fillRect(x + HEALTH_BAR_OFFSET + 5, y + HEALTH_BAR_V_OFFSET - 5 + HEALTH_BAR_V_GAP, HEALTH_BAR_LENGTH - 10, 30);
		g.fillRect(x + HEALTH_BAR_OFFSET + 5 + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET - 5, HEALTH_BAR_LENGTH - 10, 30);
		g.fillRect(x + HEALTH_BAR_OFFSET + 5 + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET - 5 + HEALTH_BAR_V_GAP, HEALTH_BAR_LENGTH - 10, 30);
		g.fillRect(x + HEALTH_BAR_OFFSET + 5 + HEALTH_BAR_GAP / 2, y + HEALTH_BAR_V_OFFSET - 5 + HEALTH_BAR_V_GAP / 2, HEALTH_BAR_LENGTH - 10, 30);
		
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE + 5, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_TEXT_V, 2 * HEALTH_TEXT_SPACE - 10, 30);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE + 5, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP - 10 + HEALTH_TEXT_V, 2 * HEALTH_TEXT_SPACE - 10, 30);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE + HEALTH_BAR_GAP + 5, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_TEXT_V, 2 * HEALTH_TEXT_SPACE - 10, 30);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE + HEALTH_BAR_GAP + 5, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP - 10 + HEALTH_TEXT_V, 2 * HEALTH_TEXT_SPACE - 10, 30);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE + HEALTH_BAR_GAP / 2 + 5, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP / 2 - 10 + HEALTH_TEXT_V, 2 * HEALTH_TEXT_SPACE - 10, 30);
		
		g.setColor(new Color(102,51,0)); // brown
		g.fillOval(x + HEALTH_BAR_OFFSET - 10, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_BAR_CIRCLE_OFFSET, 20, 20);
		g.fillOval(x + HEALTH_BAR_OFFSET - 10, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_BAR_CIRCLE_OFFSET + HEALTH_BAR_V_GAP, 20, 20);
		g.fillOval(x + HEALTH_BAR_OFFSET - 10 + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_BAR_CIRCLE_OFFSET, 20, 20);
		g.fillOval(x + HEALTH_BAR_OFFSET - 10 + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_BAR_CIRCLE_OFFSET + HEALTH_BAR_V_GAP, 20, 20);
		g.fillOval(x + HEALTH_BAR_OFFSET - 10 + HEALTH_BAR_GAP / 2, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_BAR_CIRCLE_OFFSET + HEALTH_BAR_V_GAP / 2, 20, 20);
		
		g.fillOval(x + HEALTH_BAR_OFFSET - 10 + HEALTH_BAR_LENGTH, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_BAR_CIRCLE_OFFSET, 20, 20);
		g.fillOval(x + HEALTH_BAR_OFFSET - 10 + HEALTH_BAR_LENGTH, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_BAR_CIRCLE_OFFSET + HEALTH_BAR_V_GAP, 20, 20);
		g.fillOval(x + HEALTH_BAR_OFFSET - 10 + HEALTH_BAR_LENGTH + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_BAR_CIRCLE_OFFSET, 20, 20);
		g.fillOval(x + HEALTH_BAR_OFFSET - 10 + HEALTH_BAR_LENGTH + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_BAR_CIRCLE_OFFSET + HEALTH_BAR_V_GAP, 20, 20);
		g.fillOval(x + HEALTH_BAR_OFFSET - 10 + HEALTH_BAR_LENGTH + HEALTH_BAR_GAP / 2, y + HEALTH_BAR_V_OFFSET - 10 + HEALTH_BAR_CIRCLE_OFFSET + HEALTH_BAR_V_GAP / 2, 20, 20);
		
		g.fillRect(x + HEALTH_BAR_OFFSET, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 7, HEALTH_BAR_LENGTH, 14);
		g.fillRect(x + HEALTH_BAR_OFFSET, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 7 + HEALTH_BAR_V_GAP, HEALTH_BAR_LENGTH, 14);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 7, HEALTH_BAR_LENGTH, 14);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 7 + HEALTH_BAR_V_GAP, HEALTH_BAR_LENGTH, 14);
		g.fillRect(x + HEALTH_BAR_OFFSET + HEALTH_BAR_GAP / 2, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 7 + HEALTH_BAR_V_GAP / 2, HEALTH_BAR_LENGTH, 14);
		
		if (5*data.getPlayers().get(0).getCurrentHp()/data.getPlayers().get(0).getTotalHp() < 1){
			g.setColor(new Color(153,0,0)); // red
		}
		else{
			g.setColor(new Color(0,153,0)); // green
		}
		g.fillRect(x + HEALTH_BAR_OFFSET  - 2, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 4, (HEALTH_BAR_LENGTH + 4)* data.getPlayers().get(0).getCurrentHp()/data.getPlayers().get(0).getTotalHp(), 8);
		if (5*data.getPlayers().get(1).getCurrentHp()/data.getPlayers().get(1).getTotalHp() < 1){
			g.setColor(new Color(153,0,0)); // red
		}
		else{
			g.setColor(new Color(0,153,0)); // green
		}
		g.fillRect(x + HEALTH_BAR_OFFSET  - 2, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 4 + HEALTH_BAR_V_GAP, (HEALTH_BAR_LENGTH + 4) * data.getPlayers().get(1).getCurrentHp()/data.getPlayers().get(1).getTotalHp(), 8);
		if (5*data.getPlayers().get(2).getCurrentHp()/data.getPlayers().get(2).getTotalHp() < 1){
			g.setColor(new Color(153,0,0)); // red
		}
		else{
			g.setColor(new Color(0,153,0)); // green
		}
		g.fillRect(x + HEALTH_BAR_OFFSET  - 2 + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 4, (HEALTH_BAR_LENGTH + 4) * data.getPlayers().get(2).getCurrentHp()/data.getPlayers().get(2).getTotalHp(), 8);
		if (5*data.getPlayers().get(3).getCurrentHp()/data.getPlayers().get(3).getTotalHp() < 1){
			g.setColor(new Color(153,0,0)); // red
		}
		else{
			g.setColor(new Color(0,153,0)); // green
		}
		g.fillRect(x + HEALTH_BAR_OFFSET  - 2 + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 4 + HEALTH_BAR_V_GAP, (HEALTH_BAR_LENGTH + 4) * data.getPlayers().get(3).getCurrentHp()/data.getPlayers().get(3).getTotalHp(), 8);
		if (5*data.getPlayers().get(4).getCurrentHp()/data.getPlayers().get(4).getTotalHp() < 1){
			g.setColor(new Color(153,0,0)); // red
		}
		else{
			g.setColor(new Color(0,153,0)); // green
		}
		g.fillRect(x + HEALTH_BAR_OFFSET  - 2 + HEALTH_BAR_GAP / 2, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 4 + HEALTH_BAR_V_GAP / 2, (HEALTH_BAR_LENGTH + 4) * data.getPlayers().get(4).getCurrentHp()/data.getPlayers().get(4).getTotalHp(), 8);
		
		g.setColor(new Color(0,0,0)); // black
		g.fillRect(x + HEALTH_BAR_OFFSET  - 2 + (HEALTH_BAR_LENGTH + 4)* data.getPlayers().get(0).getCurrentHp()/data.getPlayers().get(0).getTotalHp(), y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 4, HEALTH_BAR_LENGTH + 4 - (HEALTH_BAR_LENGTH + 4)* data.getPlayers().get(0).getCurrentHp()/data.getPlayers().get(0).getTotalHp(), 8);
		g.fillRect(x + HEALTH_BAR_OFFSET  - 2 + (HEALTH_BAR_LENGTH + 4)* data.getPlayers().get(1).getCurrentHp()/data.getPlayers().get(1).getTotalHp(), y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 4 + HEALTH_BAR_V_GAP, HEALTH_BAR_LENGTH + 4 - (HEALTH_BAR_LENGTH + 4) * data.getPlayers().get(1).getCurrentHp()/data.getPlayers().get(1).getTotalHp(), 8);
		g.fillRect(x + HEALTH_BAR_OFFSET  - 2 + (HEALTH_BAR_LENGTH + 4)* data.getPlayers().get(2).getCurrentHp()/data.getPlayers().get(2).getTotalHp() + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 4, HEALTH_BAR_LENGTH + 4 - (HEALTH_BAR_LENGTH + 4) * data.getPlayers().get(2).getCurrentHp()/data.getPlayers().get(2).getTotalHp(), 8);
		g.fillRect(x + HEALTH_BAR_OFFSET  - 2 + (HEALTH_BAR_LENGTH + 4)* data.getPlayers().get(3).getCurrentHp()/data.getPlayers().get(3).getTotalHp() + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 4 + HEALTH_BAR_V_GAP, HEALTH_BAR_LENGTH + 4 - (HEALTH_BAR_LENGTH + 4) * data.getPlayers().get(3).getCurrentHp()/data.getPlayers().get(3).getTotalHp(), 8);
		g.fillRect(x + HEALTH_BAR_OFFSET  - 2 + (HEALTH_BAR_LENGTH + 4)* data.getPlayers().get(4).getCurrentHp()/data.getPlayers().get(4).getTotalHp() + HEALTH_BAR_GAP / 2, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_CIRCLE_OFFSET - 4 + HEALTH_BAR_V_GAP / 2, HEALTH_BAR_LENGTH + 4 - (HEALTH_BAR_LENGTH + 4) * data.getPlayers().get(4).getCurrentHp()/data.getPlayers().get(4).getTotalHp(), 8);
		
		g.setColor(Color.black);
		Font temp = new Font(FONT, Font.PLAIN, 28);
		g.setFont(temp);
		String s;
		int center;
		
		
		s = data.getPlayers().get(0).getName();
		center = centerString(HEALTH_BAR_LENGTH - LEVEL_LENGTH, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center, y + HEALTH_BAR_V_OFFSET + 18);
		s = "" + data.getPlayers().get(0).getLevel();
		center = centerString(LEVEL_LENGTH, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH - LEVEL_LENGTH, y + HEALTH_BAR_V_OFFSET + 18);

		s = data.getPlayers().get(1).getName();
		center = centerString(HEALTH_BAR_LENGTH - LEVEL_LENGTH, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP + 18);
		s = "" + data.getPlayers().get(1).getLevel();
		center = centerString(LEVEL_LENGTH, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH - LEVEL_LENGTH, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP + 18);
		
		s = data.getPlayers().get(2).getName();
		center = centerString(HEALTH_BAR_LENGTH - LEVEL_LENGTH, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET + 18);
		s = "" + data.getPlayers().get(2).getLevel();
		center = centerString(LEVEL_LENGTH, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_GAP + HEALTH_BAR_LENGTH - LEVEL_LENGTH, y + HEALTH_BAR_V_OFFSET + 18);
		
		s = data.getPlayers().get(3).getName();
		center = centerString(HEALTH_BAR_LENGTH - LEVEL_LENGTH, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_GAP, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP + 18);
		s = "" + data.getPlayers().get(3).getLevel();
		center = centerString(LEVEL_LENGTH, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_GAP + HEALTH_BAR_LENGTH - LEVEL_LENGTH, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP + 18);
		
		s = data.getPlayers().get(4).getName();
		center = centerString(HEALTH_BAR_LENGTH - LEVEL_LENGTH, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_GAP / 2, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP / 2 + 18);
		s = "" + data.getPlayers().get(4).getLevel();
		center = centerString(LEVEL_LENGTH, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_GAP / 2 + HEALTH_BAR_LENGTH - LEVEL_LENGTH, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP / 2 + 18);
		
		temp = new Font(FONT, Font.PLAIN, 25);
		g.setFont(temp);
		
		s = "" + data.getPlayers().get(0).getCurrentHp();
		center = centerString(HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + 18 + 40);
		s = "" + data.getPlayers().get(0).getTotalHp();
		center = centerString(HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 , y + HEALTH_BAR_V_OFFSET + 18 + 40);
		s = "/";
		center = centerString(2* HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + 18 + 40);
		
		s = "" + data.getPlayers().get(1).getCurrentHp();
		center = centerString(HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP + 18 + 40);
		s = "" + data.getPlayers().get(1).getTotalHp();
		center = centerString(HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 , y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP + 18 + 40);
		s = "/";
		center = centerString(2* HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP + 18 + 40);
		
		s = "" + data.getPlayers().get(2).getCurrentHp();
		center = centerString(HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 + HEALTH_BAR_GAP - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + 18 + 40);
		s = "" + data.getPlayers().get(2).getTotalHp();
		center = centerString(HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 + HEALTH_BAR_GAP , y + HEALTH_BAR_V_OFFSET + 18 + 40);
		s = "/";
		center = centerString(2* HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 + HEALTH_BAR_GAP - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + 18 + 40);
		
		s = "" + data.getPlayers().get(3).getCurrentHp();
		center = centerString(HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 + HEALTH_BAR_GAP - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP + 18 + 40);
		s = "" + data.getPlayers().get(3).getTotalHp();
		center = centerString(HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 + HEALTH_BAR_GAP , y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP + 18 + 40);
		s = "/";
		center = centerString(2* HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 + HEALTH_BAR_GAP - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP + 18 + 40);
		
		s = "" + data.getPlayers().get(4).getCurrentHp();
		center = centerString(HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 + HEALTH_BAR_GAP / 2 - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP / 2 + 18 + 40);
		s = "" + data.getPlayers().get(4).getTotalHp();
		center = centerString(HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 + HEALTH_BAR_GAP / 2 , y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP / 2 + 18 + 40);
		s = "/";
		center = centerString(2* HEALTH_TEXT_SPACE, temp, s, g);
		g.drawString(s, x + HEALTH_BAR_OFFSET + center + HEALTH_BAR_LENGTH / 2 + HEALTH_BAR_GAP / 2 - HEALTH_TEXT_SPACE, y + HEALTH_BAR_V_OFFSET + HEALTH_BAR_V_GAP / 2 + 18 + 40);
		
		
	}
	
	public void drawHighlighted(int x, int y, Graphics g){
		int xOff = 0;
		int yOff = 0;
		if (rightClick){
			if (x > rightClickX && x < rightClickX + RIGHTCLICK_WIDTH && y > rightClickY && y < rightClickY + RIGHTCLICK_HEIGHT){
				x -= rightClickX + BORDER_WIDTH;
				y -= rightClickY + BORDER_WIDTH;
				
				if (x < 0 || y < 0) return;
				
				int scale = (RIGHTCLICK_HEIGHT  - 2*BORDER_WIDTH) / 2;
				
				int xIndex = x / scale;
				int yIndex = y / scale;
				
				if (xIndex == RIGHTCLICK_WIDTH / scale) return;
				if (yIndex == RIGHTCLICK_HEIGHT / scale) return;
				g.setColor(new Color(0,0,0,75));
				g.fillRect(rightClickX + BORDER_WIDTH + xIndex * scale, rightClickY + BORDER_WIDTH + yIndex * scale, scale, scale);
				return;
			}
		}
		if (uploading){
			int tempX = x;
			tempX = x + MAIN_VERTICAL_BORDER - FOCUS_VERTICAL_BORDER;
			if (tempX > UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + UPLOAD_BUTTON_BORDER  + 10 && tempX < UPLOAD_BOX_LENGTH - UPLOAD_FIELD_OFFSET + UPLOAD_BOX_OFFSET){
				if (y > UPLOAD_TEXT_OFFSET && y < UPLOAD_TEXT_OFFSET + UPLOAD_FIELD_HEIGHT){
					g.setColor(new Color(0,0,0,75));
					g.fillRect(FOCUS_VERTICAL_BORDER - MAIN_VERTICAL_BORDER + UPLOAD_BOX_OFFSET + UPLOAD_FIELD_OFFSET + 10 + UPLOAD_BUTTON_BORDER, UPLOAD_TEXT_OFFSET, UPLOAD_BOX_LENGTH - 2 * UPLOAD_FIELD_OFFSET  - UPLOAD_BUTTON_BORDER - 10, UPLOAD_FIELD_HEIGHT);
				}
			}
		}
		if (choosing){
			if (x <  SELECT_BOX_OFFSET){
				return;
			}
			if (x > getWidth() - SELECT_BOX_OFFSET){
				return;
			}
			if (y <  SELECT_BOX_V_OFFSET){
				return;
			}
			if (y >  getHeight() - SELECT_BOX_V_OFFSET){
				return;
			}
			x -= SELECT_BOX_OFFSET + ICON_GAP - 20 + BORDER_WIDTH - 5;
			y -= SELECT_BOX_V_OFFSET + ICON_V_GAP - 20 + BORDER_WIDTH - 10;
			xOff += SELECT_BOX_OFFSET + ICON_GAP - 20 + BORDER_WIDTH - 5;
			yOff += SELECT_BOX_V_OFFSET + ICON_V_GAP - 20 + BORDER_WIDTH - 10;
			
			int i = x / (ICON_GAP + ICON_WIDTH);
			int j = y / (ICON_GAP + ICON_HEIGHT + ICON_TEXT);
			
			xOff += i * (ICON_GAP + ICON_WIDTH);
			yOff += j * (ICON_GAP + ICON_HEIGHT + ICON_TEXT);
			
			g.setColor(new Color(0,0,0,75));
			if (j*10 + i < data.getPictures().size()){
				g.fillRect(xOff, yOff, (ICON_GAP + ICON_WIDTH), (ICON_GAP + ICON_HEIGHT + ICON_TEXT));
			}
			return;
		}
		x -= BORDER_WIDTH;
		y -= BORDER_WIDTH;
		if (x < 0) return;
		if (y < 0) return;
		
		xOff = BORDER_WIDTH;
		yOff = BORDER_WIDTH;
		
		g.setColor(new Color(0,0,0,75));
		
		if (x < MAIN_VERTICAL_BORDER - BORDER_WIDTH){ // Left of main
			if (y < HORIZONTAL_CHAT_BORDER - BORDER_WIDTH){ // Focus on DICE
				if (y < DIE_SIZE){ // 0-2
					if (x < DIE_SIZE){ // 0
						if (data.getDice()[0].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									g.fillRect(xOff + DIE_SIZE - Die.X_WIDTH, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
								if (x < Die.X_WIDTH){ // X
									g.fillRect(xOff, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
							}
						}
						else{
							g.fillRect(xOff, yOff, DIE_SIZE, DIE_SIZE);
						}
						return;
					}
					else if (x <  2 * DIE_SIZE){ // 1
						xOff += DIE_SIZE;
						x -= DIE_SIZE;
						if (data.getDice()[1].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									g.fillRect(xOff + DIE_SIZE - Die.X_WIDTH, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
								if (x < Die.X_WIDTH){ // X
									g.fillRect(xOff, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
							}
						}
						else{
							g.fillRect(xOff, yOff, DIE_SIZE, DIE_SIZE);
						}
						return;
					}
					else { // 2
						xOff += 2*DIE_SIZE;
						x -= 2*DIE_SIZE;
						if (data.getDice()[2].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									g.fillRect(xOff + DIE_SIZE - Die.X_WIDTH, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
								if (x < Die.X_WIDTH){ // X
									g.fillRect(xOff, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
							}
						}
						else{
							g.fillRect(xOff, yOff, DIE_SIZE, DIE_SIZE);
						}
						return;
					}
				}
				else{ // 3-5
					yOff += DIE_SIZE;
					y -= DIE_SIZE;
					if (x < DIE_SIZE){ // 3
						if (data.getDice()[3].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									g.fillRect(xOff + DIE_SIZE - Die.X_WIDTH, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
								if (x < Die.X_WIDTH){ // X
									g.fillRect(xOff, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
							}
						}
						else{
							g.fillRect(xOff, yOff, DIE_SIZE, DIE_SIZE);
						}
						return;
					}
					else if (x <  2 * DIE_SIZE){ // 4
						xOff += DIE_SIZE;
						x -= DIE_SIZE;
						if (data.getDice()[4].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									g.fillRect(xOff + DIE_SIZE - Die.X_WIDTH, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
								if (x < Die.X_WIDTH){ // X
									g.fillRect(xOff, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
							}
						}
						else{
							g.fillRect(xOff, yOff, DIE_SIZE, DIE_SIZE);
						}
						return;
					}
					else { // 5
						xOff += 2*DIE_SIZE;
						x -= 2*DIE_SIZE;
						if (data.getDice()[5].isVisible()){
							if (y > DIE_SIZE - Die.X_WIDTH){
								if (x > DIE_SIZE - Die.X_WIDTH){ // max
									g.fillRect(xOff + DIE_SIZE - Die.X_WIDTH, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
								if (x < Die.X_WIDTH){ // X
									g.fillRect(xOff, yOff + DIE_SIZE - Die.X_WIDTH, Die.X_WIDTH, Die.X_WIDTH);
								}
							}
						}
						else{
							g.fillRect(xOff, yOff, DIE_SIZE, DIE_SIZE);
						}
						return;
					}
				}
			}
			if (y > HORIZONTAL_CHAT_BORDER + BORDER_WIDTH){ // Focus on CHAT
				// Do Nothing
				return;
			}
		}
		if (x > MAIN_VERTICAL_BORDER + BORDER_WIDTH){ // Right of main
			if (y > INFO_HORIZONTAL_BORDER + BORDER_WIDTH){ //  bottom info bar.
				if (x > HEALTH_VERTICAL_BORDER + BORDER_WIDTH){ // health bars
					xOff += HEALTH_VERTICAL_BORDER;
					yOff += INFO_HORIZONTAL_BORDER;
					x -= HEALTH_VERTICAL_BORDER;
					y -= INFO_HORIZONTAL_BORDER;
					xOff += HEALTH_BAR_OFFSET;
					x -= HEALTH_BAR_OFFSET;
					yOff += HEALTH_BAR_V_OFFSET;
					y -= HEALTH_BAR_V_OFFSET;
					
					if (y - HEALTH_BAR_V_GAP / 2> -10 && y  - HEALTH_BAR_V_GAP / 2< 30){ // top
						if (x - HEALTH_BAR_GAP / 2 > 0 && x - HEALTH_BAR_GAP / 2 < HEALTH_BAR_LENGTH - LEVEL_LENGTH){
							//g.fillRect(xOff, yOff - 10, HEALTH_BAR_LENGTH - LEVEL_LENGTH, 33);
						}
						if (x - HEALTH_BAR_GAP / 2> HEALTH_BAR_LENGTH - LEVEL_LENGTH && x  - HEALTH_BAR_GAP / 2 < HEALTH_BAR_LENGTH){
							g.fillRect(xOff + HEALTH_BAR_GAP / 2 + HEALTH_BAR_LENGTH - LEVEL_LENGTH, yOff + HEALTH_BAR_V_GAP / 2 - 10, LEVEL_LENGTH, 33);
						}
					}
					if (y - HEALTH_BAR_V_GAP / 2 > 30 && y - HEALTH_BAR_V_GAP / 2 < 65){
						if (x - HEALTH_BAR_GAP / 2 > HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE && x - HEALTH_BAR_GAP / 2 < HEALTH_BAR_LENGTH / 2 - 10){
							g.fillRect(xOff + HEALTH_BAR_GAP / 2 + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE, yOff + HEALTH_BAR_V_GAP / 2 + 37, HEALTH_TEXT_SPACE - 10, 28);
						}
						if (x - HEALTH_BAR_GAP / 2 < HEALTH_BAR_LENGTH / 2 + HEALTH_TEXT_SPACE && x - HEALTH_BAR_GAP / 2 > HEALTH_BAR_LENGTH / 2 + 10){
							g.fillRect(xOff + HEALTH_BAR_GAP / 2 + HEALTH_BAR_LENGTH / 2 + 10, yOff + HEALTH_BAR_V_GAP / 2 + 37, HEALTH_TEXT_SPACE - 10, 28);
						}
					}
					
					if (y < (getHeight() - INFO_HORIZONTAL_BORDER) / 2){ // top
						if (x < (getWidth() - HEALTH_VERTICAL_BORDER) / 2){ // top left
						}
						else{ // top right
							xOff += HEALTH_BAR_GAP;
							x -= HEALTH_BAR_GAP;
						}
					}
					else{ // bottom
						if (x < (getWidth() - HEALTH_VERTICAL_BORDER) / 2){ // bottom left
							yOff += HEALTH_BAR_V_GAP;
							y -= HEALTH_BAR_V_GAP;
						}
						else{ // bottom right
							xOff += HEALTH_BAR_GAP;
							x -= HEALTH_BAR_GAP;
							yOff += HEALTH_BAR_V_GAP;
							y -= HEALTH_BAR_V_GAP;
						}
					}
					
					if (y > -10 && y < 30){ // top
						if (x > 0 && x < HEALTH_BAR_LENGTH - LEVEL_LENGTH){
							//g.fillRect(xOff, yOff - 10, HEALTH_BAR_LENGTH - LEVEL_LENGTH, 33);
						}
						if (x > HEALTH_BAR_LENGTH - LEVEL_LENGTH && x < HEALTH_BAR_LENGTH){
							g.fillRect(xOff + HEALTH_BAR_LENGTH - LEVEL_LENGTH, yOff - 10, LEVEL_LENGTH, 33);
						}
					}
					if (y > 30 && y < 65){
						if (x > HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE && x < HEALTH_BAR_LENGTH / 2 - 10){
							g.fillRect(xOff + HEALTH_BAR_LENGTH / 2 - HEALTH_TEXT_SPACE, yOff + 37, HEALTH_TEXT_SPACE - 10, 28);
						}
						if (x < HEALTH_BAR_LENGTH / 2 + HEALTH_TEXT_SPACE && x > HEALTH_BAR_LENGTH / 2 + 10){
							g.fillRect(xOff + HEALTH_BAR_LENGTH / 2 + 10, yOff + 37, HEALTH_TEXT_SPACE - 10, 28);
						}
					}
					return;
				}
				if (x < HEALTH_VERTICAL_BORDER){ // left menus
					if ( y > INFO_MID_BORDER){ // NON_Dice
						
						x -= MAIN_VERTICAL_BORDER;
						y -= INFO_MID_BORDER;
						xOff += MAIN_VERTICAL_BORDER;
						yOff += INFO_MID_BORDER;
						
						if (x > QUEST_OFFSET && x < QUEST_OFFSET + QUEST_LENGTH && y > QUEST_V_OFFSET && y < QUEST_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - BORDER_WIDTH - QUEST_V_OFFSET){
							g.fillRect(xOff + QUEST_OFFSET, yOff + QUEST_V_OFFSET, QUEST_LENGTH, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * QUEST_V_OFFSET);
						}
						if (x > TIME_OFFSET && x < TIME_OFFSET + TIME_LENGTH && y > TIME_V_OFFSET && y < TIME_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - BORDER_WIDTH - TIME_V_OFFSET){
							g.fillRect(xOff + TIME_OFFSET, yOff + TIME_V_OFFSET, TIME_LENGTH, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * TIME_V_OFFSET);
						}
						if (x > RESET_OFFSET && x < RESET_OFFSET + RESET_LENGTH && y > RESET_V_OFFSET && y < RESET_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - BORDER_WIDTH - RESET_V_OFFSET){
							g.fillRect(xOff + RESET_OFFSET, yOff + RESET_V_OFFSET, RESET_LENGTH, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * RESET_V_OFFSET);
						}
					}
					else{ // Dice
						x -= MAIN_VERTICAL_BORDER;
						y -= INFO_HORIZONTAL_BORDER;
						xOff += MAIN_VERTICAL_BORDER;
						yOff += INFO_HORIZONTAL_BORDER;
						
						if (x > ROLL_OFFSET && x < ROLL_OFFSET + ROLL_LENGTH && y > ROLL_V_OFFSET && y < ROLL_V_OFFSET + INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - BORDER_WIDTH - ROLL_V_OFFSET){
							g.fillRect(xOff + ROLL_OFFSET, yOff + ROLL_V_OFFSET, ROLL_LENGTH, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2 * BORDER_WIDTH - 2 * ROLL_V_OFFSET);
						}
					}
					return;
				}
			}
			if (y < FOCUS_BORDER){ //  top "quarter"
				if (x > FOCUS_VERTICAL_BORDER){
					x -= FOCUS_VERTICAL_BORDER;
					xOff += FOCUS_VERTICAL_BORDER;
				}
				else{
					x -= MAIN_VERTICAL_BORDER;
					xOff += MAIN_VERTICAL_BORDER;
				}
				if (x > SELECT_OFFSET && x < SELECT_OFFSET + SELECT_LENGTH && y > SELECT_V_OFFSET && y < SELECT_V_OFFSET + FOCUS_BORDER - BORDER_WIDTH - 2 *SELECT_V_OFFSET){
					g.fillRect(xOff + SELECT_OFFSET, yOff + SELECT_V_OFFSET, SELECT_LENGTH, FOCUS_BORDER - BORDER_WIDTH - 2 *SELECT_V_OFFSET);
				}
				if (x > PLUS_OFFSET && x < PLUS_OFFSET + PLUS_LENGTH && y > ZOOMOUT_V_OFFSET && y < ZOOMOUT_V_OFFSET + ZOOMOUT_LENGTH){
					g.fillRect(xOff + PLUS_OFFSET, yOff + ZOOMOUT_V_OFFSET, ZOOMOUT_LENGTH, ZOOMOUT_LENGTH);
				}
				if (x > PLUS_OFFSET && x < PLUS_OFFSET + PLUS_LENGTH && y > ZOOMIN_V_OFFSET && y < ZOOMIN_V_OFFSET + ZOOMOUT_LENGTH){
					g.fillRect(xOff + PLUS_OFFSET, yOff + ZOOMIN_V_OFFSET, ZOOMOUT_LENGTH, ZOOMOUT_LENGTH);
				}
				if (x > ZOOMIN_OFFSET && x < ZOOMIN_OFFSET + ZOOMIN_LENGTH && y > ZOOMIN_V_OFFSET && y < ZOOMIN_V_OFFSET + 60){
					g.fillRect(xOff + ZOOMIN_OFFSET, yOff + ZOOMIN_V_OFFSET, ZOOMIN_LENGTH, 60);
				}
				if (x > ZOOMOUT_OFFSET && x < ZOOMOUT_OFFSET + ZOOMOUT_LENGTH && y > ZOOMOUT_V_OFFSET && y < ZOOMOUT_V_OFFSET + 60){
					g.fillRect(xOff + ZOOMOUT_OFFSET, yOff + ZOOMOUT_V_OFFSET, ZOOMOUT_LENGTH, 60);
				}
				if (x > UP_OFFSET && x < UP_OFFSET + UP_LENGTH && y > UP_V_OFFSET && y < UP_V_OFFSET + 40){
					g.fillRect(xOff + UP_OFFSET, yOff + UP_V_OFFSET, UP_LENGTH, 40);
				}
				if (x > DOWN_OFFSET && x < DOWN_OFFSET + DOWN_LENGTH && y > DOWN_V_OFFSET && y < DOWN_V_OFFSET + 40){
					g.fillRect(xOff + DOWN_OFFSET, yOff + DOWN_V_OFFSET, DOWN_LENGTH, 40);
				}
				if (x > RIGHT_OFFSET && x < RIGHT_OFFSET + RIGHT_LENGTH && y > RIGHT_V_OFFSET && y < RIGHT_V_OFFSET + 40){
					g.fillRect(xOff + RIGHT_OFFSET, yOff + RIGHT_V_OFFSET, RIGHT_LENGTH, 40);
				}
				if (x > LEFT_OFFSET && x < LEFT_OFFSET + LEFT_LENGTH && y > LEFT_V_OFFSET && y < LEFT_V_OFFSET + 40){
					g.fillRect(xOff + LEFT_OFFSET, yOff + LEFT_V_OFFSET, LEFT_LENGTH, 40);
				}
				return;
			}
		}
	}
	
	public void drawDice(Graphics g){
		Die[] dice = data.getDice();
		int current = 0;
		for (int j = 0; j < 2; j++){
			for (int i = 0; i < 3; i++){
				dice[current].draw(BORDER_WIDTH + DIE_SIZE * i, BORDER_WIDTH + DIE_SIZE * j, DIE_SIZE, DIE_SIZE, g);
				current++;
			}
		}
	}
	
	//***********************************************************************************************************************************
	
	public void drawMainWindowFrame(Graphics g){
		int green = 128 - 41;
		int blue = 255 - 82;
		g.setColor(new Color(0,green,blue));
		g.fillRect(BORDER_WIDTH, BORDER_WIDTH, getWidth() - 2*BORDER_WIDTH, getHeight() - 2*BORDER_WIDTH);
		for (int i = 0; i < 40; i++){
			green += 1;
			blue += 2;
			g.setColor(new Color(0,green,blue));
			g.fillOval(BORDER_WIDTH + i*16, BORDER_WIDTH + i*9, getWidth()-i*32 - 2*BORDER_WIDTH, getHeight()-i*18 - 2*BORDER_WIDTH);
		}
		green = 40;
		blue = 80;
		for (int i = 0; i < 11; i++){
			g.setColor(new Color(0,green,blue));
			g.drawRect(i, i, MAIN_VERTICAL_BORDER - 2*i, HORIZONTAL_CHAT_BORDER - 2*i);
			g.drawRect(i, HORIZONTAL_CHAT_BORDER + i, MAIN_VERTICAL_BORDER - 2*i, getHeight() - HORIZONTAL_CHAT_BORDER - 2*i);
			g.drawRect(MAIN_VERTICAL_BORDER + i, i, FOCUS_VERTICAL_BORDER - MAIN_VERTICAL_BORDER - 2*i, INFO_HORIZONTAL_BORDER - 2*i);
			g.drawRect(FOCUS_VERTICAL_BORDER + i, i, getWidth() - FOCUS_VERTICAL_BORDER - 2*i, INFO_HORIZONTAL_BORDER - 2*i);
			g.drawRect(MAIN_VERTICAL_BORDER + i, INFO_HORIZONTAL_BORDER + i, HEALTH_VERTICAL_BORDER - MAIN_VERTICAL_BORDER - 2*i, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2*i);
			g.drawRect(MAIN_VERTICAL_BORDER + i, INFO_MID_BORDER + i, HEALTH_VERTICAL_BORDER - MAIN_VERTICAL_BORDER - 2*i, INFO_MID_BORDER - INFO_HORIZONTAL_BORDER - 2*i);
			g.drawRect(HEALTH_VERTICAL_BORDER + i, INFO_HORIZONTAL_BORDER + i, getWidth() - HEALTH_VERTICAL_BORDER - 2*i, getHeight() - INFO_HORIZONTAL_BORDER - 2*i);
			green += 5;
			blue += 10;
		}
		g.setColor(new Color(50,150,255));
		g.fillRect(BORDER_WIDTH, BORDER_WIDTH + HORIZONTAL_CHAT_BORDER, MAIN_VERTICAL_BORDER - 2 * BORDER_WIDTH, getHeight() - HORIZONTAL_CHAT_BORDER - 2 * BORDER_WIDTH);
		g.setColor(new Color(70,170,255));
		g.fillRect(BORDER_WIDTH + 5, BORDER_WIDTH + HORIZONTAL_CHAT_BORDER + 5, MAIN_VERTICAL_BORDER - 2 * BORDER_WIDTH - 10, getHeight() - HORIZONTAL_CHAT_BORDER - CHAT_HEIGHT - 2 * BORDER_WIDTH - 10);
		
	}
	
	//***********************************************************************************************************************************
	
	private String name;
	
	public void drawSettingName(Graphics g){
		int green = 128 - 41;
		int blue = 255 - 82;
		g.setColor(new Color(0,green,blue));
		g.fillRect(0, 0, getWidth(), getHeight());
		for (int i = 0; i < 40; i++){
			green += 1;
			blue += 2;
			g.setColor(new Color(0,green,blue));
			g.fillOval(i*16,i*9, getWidth()-i*32, getHeight()-i*18);
		}
		g.setColor(Color.white);
		Font temp = new Font(FONT, Font.PLAIN, 50);
		g.setFont(temp);
		String s = "Choose a Username (No Spaces)";
		int center = centerString(getWidth(), temp, s, g);
		g.drawString(s, center, getHeight() / 2);

		green = 40;
		blue = 80;
		for (int i = 0; i < 10; i++){
			g.setColor(new Color(0,green,blue));
			g.drawRect(i, i, getWidth()-2*i, getHeight()-2*i);
			green += 5;
			blue += 10;
		}
	}
	
	public boolean validName(){
		System.out.println("trying");
		if (name == null) return false;
		if (name.equals("")) return false;
		if (name.indexOf(' ') != -1) return false;
		if (name.length() > 12) return false;
		System.out.println("name valid");
		return true;
	}
	
	public String getPlayerName(){
		settingName = false;
		return name;
	}
	
	public void setPlayerName(String s){
		name = s;
	}
	
	public void actionPerformed(ActionEvent e){
		repaint();
	}
	
	//***********************************************************************************************************************************
	
	public static int centerString (int width, Font f, String s, Graphics g){
		int length = (int)(g.getFontMetrics(f).getStringBounds(s, g).getWidth());
		return width/2 - length/2;
	}
}
