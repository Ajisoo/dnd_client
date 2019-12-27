package old;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFrame;

public class Client{
	
	public static void main(String[] args) {
		String ip = "128.54.244.106";
		
		Scanner scanner = new Scanner(System.in);
		
		JFrame frame = new JFrame();
		frame.setLocation(0, 0);
		frame.setSize(1920,1080);
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UI ui = new UI(1920, 1080, ip);
		frame.setContentPane(ui);
		frame.setVisible(true);
	}
	
	//***********************************************************************************************************************
	
	Data data;
	String name;
	UI ui;
	
	ObjectInputStream Sinput;
    ObjectOutputStream Soutput;
    Socket socket;
    
    InputThread inputThread;
    String ip;
    
    
	public Client(String ip, int port, String name, UI ui){
		this.ip = ip;
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
			Soutput.writeUnshared(s);
			Soutput.flush();
		}catch (Exception e){
			System.out.println("Error printing object");
			try{
				Sinput.close();
				Soutput.close();
				socket.close();
			}catch(Exception e1){}
			ui.makeClient(ip);
			ui = null;
		}
	}
	
	public void sendPic(Picture p){
		try{
			Soutput.writeObject(p);
			Soutput.flush();
		}catch (Exception e){
			System.out.println("Error printing object");
			try{
				Sinput.close();
				Soutput.close();
				socket.close();
			}catch(Exception e1){}
			ui.makeClient(ip);
			ui = null;
		}
	}
	
	//***********************************************************************************************************************
	
	class InputThread extends Thread{
		
		ObjectInputStream Sinput;
		
		public InputThread(ObjectInputStream Sinput){
			this.Sinput = Sinput;
		}
		
		public void run(){  //change to only send/recieve input, change local data.
			Object o = null;
			while(ui != null){
				try {
					
					if (ui.getData() == null){
						ui.finishMakeClient();
					}
					if (ui != null){
						o = Sinput.readObject();
					}
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
					try{
						Sinput.close();
						Soutput.close();
						socket.close();
					}catch(Exception e1){}
					ui.makeClient(ip);
					ui = null;
					;
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