package old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class NameActionListener implements ActionListener{
	public NameActionListener(UI ui, String name, String ip){
		this.name = name;
		this.ip = ip;
		this.ui = ui;
	}
	String name;
	String ip;
	UI ui;
	
	public void actionPerformed(ActionEvent e){
		name = ((JTextField)(e.getSource())).getText();
		if (name == null) return;
		if (name.equals("")) return;
		if (name.indexOf(' ') != -1) return;
		if (name.length() > 12) return;
		ui.setPlayerName(name);
		ui.makeClient(ip);
		
	}
}
