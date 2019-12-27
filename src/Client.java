import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.Point;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Client{
	
	public static void main(String[] args) {
		String ip = "128.54.251.40";
		
		Scanner scanner = new Scanner(System.in);
		
		JFrame frame = new JFrame();
		frame.setLocation(0, 0);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UI ui = new UI(1920, 1080, ip);
		//ui.setData(new Data());
		frame.setContentPane(ui);
		frame.setVisible(true);
//		int i = 0;
//		while (!ui.validName()){
//			//System.out.println();
//		}
//		System.out.println("hi");
//		Client c = new Client(ip, 25569, ui.getPlayerName(), ui);
//		new Listener(ui,ui,c);
	}
	
	//***********************************************************************************************************************
	
	Data data;
	String name;
	UI ui;
	
	ObjectInputStream Sinput;
    ObjectOutputStream Soutput;
    Socket socket;
    
    InputThread inputThread;
    
	public Client(String ip, int port, String name, UI ui){
		this.ui = ui;
		this.name = name;
		Runtime.getRuntime().addShutdownHook(new EndThread());
        try {
            socket = new Socket(ip, port);
        }
        catch(Exception e) {
            System.out.println("Error connecting to server: " + e);
            System.exit(1);
        }
        System.out.println("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort() + "\n");
        try{ 
        	Soutput = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            Soutput.writeObject(name);
            Soutput.flush();
        }
        catch (IOException e) {
            System.out.println("Exception creating new Input/Output Streams: " + e);
            System.exit(1);
        }
		try {
			inputThread = new InputThread(new ObjectInputStream(new BufferedInputStream(socket.getInputStream())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inputThread.start();
	}
	
	public void sendString(String s){
		try{
			Soutput.reset();
			Soutput.writeObject(s);
			System.out.println("String sent : " + System.currentTimeMillis());
			Soutput.flush();
		}catch (Exception e){
			System.out.println("Error printing object");
		}
	}
	
	//***********************************************************************************************************************
	
	class InputThread extends Thread{
		
		ObjectInputStream Sinput;
		
		public InputThread(ObjectInputStream Sinput){
			this.Sinput = Sinput;
		}
		
		public void run(){  //change to only send/recieve input, change local data.
			Object o = new Object();
			while(true){
				try {
					o = Sinput.readObject();
					System.out.println("data received : " + System.currentTimeMillis());
					if (o instanceof String){
						data.update((String)(o));
						ui.repaint();
					}
					if (o instanceof Data){
						data = (Data)(o);
						ui.setData(data);
						ui.repaint();
					}
				} catch (Exception e) {
					System.out.println("Problem reading data from server: " + e);
					System.exit(1);
				}
			}
		}
	}
	
	//*************************************************************************************************************************
	
	class EndThread extends Thread{
		public EndThread(){}
		public void run(){
			try{
				Sinput.close();
				Soutput.close();
				socket.close(); 
			}catch(Exception e ){}
		}
	}
}