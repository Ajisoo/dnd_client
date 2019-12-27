package old;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.Serializable;

public class Die  implements Serializable{
	
	public static final int PLUS_WIDTH = 30;
	public static final int X_WIDTH = 50;
	
	private int maximum;
	private int current;
	private boolean visible;
	
	public Die(){
		maximum = 6;
		current = (int)(Math.random()*maximum) + 1;
		visible = Math.random() > 0.5 ? true : false;
	}
	
	public Die(boolean visible, int max, int current){
		this.visible = visible;
		this.current = current;
		this.maximum = max;
	}
	
	public void setCurrent(int current){
		this.current = current;
	}
	
	public void setMax(int max){
		this.maximum = max;
	}
	
	public void roll(){
		current = (int)(Math.random()*maximum) + 1;
	}
	
	public void setVisible(boolean v){
		visible = v;
	}
	
	public void draw(int x, int y, int width, int height, Graphics g){
		if (!visible){
			g.setColor(new Color(255,255,255,128));
			g.fillRect(x + width / 2 - PLUS_WIDTH / 2, y + PLUS_WIDTH, PLUS_WIDTH, height/2 - PLUS_WIDTH * 3 / 2);
			g.fillRect(x + width / 2 - PLUS_WIDTH / 2, y + height / 2 + PLUS_WIDTH / 2, PLUS_WIDTH, height / 2 - PLUS_WIDTH * 3 / 2);
			g.fillRect(x + PLUS_WIDTH, y + height / 2 - PLUS_WIDTH / 2, width - 2 * PLUS_WIDTH, PLUS_WIDTH);
			return;
		}
		
		g.setColor(Color.white);
		g.fillRect(x,y,width,height);
		
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
		g.drawRect(x, y + height - X_WIDTH, X_WIDTH, X_WIDTH);
		g.drawRect(x + X_WIDTH, y + height - X_WIDTH, width - X_WIDTH, X_WIDTH);
		
		Font f = new Font(UI.FONT, Font.BOLD, 150);
		g.setFont(f);
		int start = UI.centerString(width,f,"" + current,g);
		g.setColor(Color.black);
		g.drawString("" + current, start + x, y + 130);
		
		f = new Font(UI.FONT, Font.BOLD, 40);
		g.setFont(f);
		start = UI.centerString(X_WIDTH,f,"X",g);
		g.setColor(Color.black);
		g.drawString("X", start + x, y + height - 10);
		
		f = new Font(UI.FONT, Font.BOLD, 40);
		g.setFont(f);
		start = UI.centerString(width - X_WIDTH,f,"Max: " + maximum,g);
		g.setColor(Color.black);
		g.drawString("Max: " + maximum, start + x + X_WIDTH, y + height - 10);
		
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public int getCurrent(){
		return current;
	}

	public int getMax() {
		return maximum;
	}
}
