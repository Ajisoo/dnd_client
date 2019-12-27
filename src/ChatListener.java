package old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class ChatListener implements ActionListener{
	String s;
	Client c;
	
	public ChatListener(String s, Client c){
		this.s = s;
		this.c = c;
	}
	
	public void actionPerformed(ActionEvent e){
		s = ((JTextField)(e.getSource())).getText();
		((JTextField)(e.getSource())).setText("");
		c.sendString("CHT " + s);
		System.out.println("Sending " + s);
	}
	
	
}
