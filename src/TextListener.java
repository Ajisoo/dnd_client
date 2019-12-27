package old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TextListener implements ActionListener{
	public Client c;
	public String s;
	
	public TextListener(String s, Client c){
		this.s = s;
		this.c = c;
	}
	
	public void actionPerformed(ActionEvent e){
		s += ((JTextField)(e.getSource())).getText();
		((JTextField)(e.getSource())).setVisible(false);
		c.sendString(s);
		System.out.println("Sending " + s);
	}
}
