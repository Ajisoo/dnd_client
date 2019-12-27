package old;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;

public class Server{

    public static void main(String[] arg) {
        new Server(25569);
    }
    
	//***********************************************************************************************************************
    
    private ServerSocket serverSocket;
    private Data data;
    
    private Vector<InfoThread> in;
    private Vector<Socket> sockets;
    private String location;
    
    Server(int port) {
    	sockets = new Vector<Socket>();
    	in = new Vector<InfoThread>();
//    	try{
//    		URL url = new URL("http://checkip.amazonaws.com/");
//    		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//    		System.out.println(br.readLine());
//    	}catch(Exception e ){}
    	
    	location = "C:\\Everything\\DnD\\";
        data = new Data(location);
        try
        {
            serverSocket = new ServerSocket(port);
            System.out.println("Server waiting for client on port " + serverSocket.getInetAddress().getHostAddress() + ":" +  serverSocket.getLocalPort());
 
            while(true)
            {
                Socket newSocket = serverSocket.accept();
                update(null,null,true,newSocket);
            }
        }
        catch (IOException e) {
            System.out.println("Exception on new ServerSocket: " + e);
            System.exit(1);
        }
        try{}
		catch(Exception e){} 
    }    
    
    public void saveData(){
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(location + "players.txt")));
			for (int i = 0; i < data.getPlayers().size(); i++){
				writer.write(data.getPlayers().get(i).getName() + " " + data.getPlayers().get(i).getLevel() + " " + data.getPlayers().get(i).getCurrentHp() + " " + data.getPlayers().get(i).getTotalHp());
				if (i != data.getPlayers().size() - 1){
					writer.write("\n");
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(location + "chat.txt")));
			for (int i = 0; i < data.getChatHistory().size(); i++){
				writer.write(data.getChatHistory().get(i).getSender() + " " + data.getChatHistory().get(i).getTime().getTime() + " " + data.getChatHistory().get(i).getChat());
				if (i != data.getChatHistory().size() - 1){
					writer.newLine();
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(location + "time.txt")));
			writer.write(data.getTime());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(location + "dice.txt")));
			for (int i = 0; i < 6; i++){
				String s = "";
				if (data.getDice()[i].isVisible()){
					s = "o";
				}
				else{
					s = "x";
				}
				writer.write(s + data.getDice()[i].getMax() + " " + data.getDice()[i].getCurrent());
				if (i != data.getDice().length - 1){
					writer.newLine();
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	public void updateData(String s){
		data.update(s);
		if (s.substring(s.indexOf(' ') + 1).equals("ROL")){
			for (int i = 0; i < in.size(); i++){
				in.get(i).sendString(s.substring(0,s.indexOf(' '))+ " TOT " + data.getDice()[0].getCurrent() + " " + data.getDice()[1].getCurrent() + " " + data.getDice()[2].getCurrent() + " " + data.getDice()[3].getCurrent() + " " + data.getDice()[4].getCurrent() + " " + data.getDice()[5].getCurrent());
	    	}
		}
		else if (s.substring(s.indexOf(' ') + 1).equals("RES")){
			data.getChatHistory().add(new Message("Server", s.substring(0,s.indexOf(' ')) + " reset the server.", System.currentTimeMillis()));
			saveData();
			data = new Data(location);
			for (int i = 0; i < in.size(); i++){
	    		in.get(i).sendData(data);
	    	}
		}
		else{
			for (int i = 0; i < in.size(); i++){
	    		in.get(i).sendString(s);
	    	}
		}
    }
	
	public void addPicture(Picture p, String name){
		try{
			BufferedImage image = null;
			if (p.getImage() instanceof BufferedImage){
				image = (BufferedImage)p.getImage();
			}
			else{
				image = new BufferedImage(p.getImage().getWidth(null), p.getImage().getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.drawImage(p.getImage(), 0, 0 , null);
				g.dispose();
			}
			ImageIO.write(image, "jpg", new File(location + "Images\\" + p.getName() + ".jpg"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		data.getPictures().add(p);
		data.update("Server CHT " + name + " added a new picture: " + p.getName());
		for (int i = 0; i < in.size(); i++){
    		in.get(i).sendData(data);
    	}
	}
	
	public synchronized void update(Object o, String s, boolean settingUp, Socket socket){
		if (settingUp){
			 sockets.add(socket);
             System.out.println("New client asked for a connection");
             in.add(new InfoThread(sockets.get(sockets.size()-1), this));
             in.get(sockets.size()-1).start();
		}
		if (o instanceof String){
			updateData(s + " " + (String)o);
		}
		else if (o instanceof Picture){
			addPicture((Picture)(o), s);
		}
	}
    
    public Data getData(){
    	return data;
    }
    
  //***********************************************************************************************************************
    
    public class InfoThread extends Thread{ // One thread for each user.
    	Socket socket;
        ObjectInputStream Sinput;
        ObjectOutputStream Soutput;
        String name;
        Server server;
         
        public InfoThread(Socket socket, Server server) {
        	this.server = server;
        	this.socket = socket;
        }
        
        public void sendString(String s){
        	try {
    			Soutput.writeUnshared(s);
    			Soutput.flush();
			} catch (IOException e) {
                System.out.println("Error sending to " + name + ".");
                System.out.println("Removing " + name + " from server.");
                server.in.remove(this);
                server.sockets.remove(socket);
			}
        }
        
        public void sendData(Data d){
        	try {
        		Soutput.reset();
    			Soutput.writeObject(d);
    			Soutput.flush();
			} catch (IOException e) {
                System.out.println("Error sending to " + name + ".");
                System.out.println("Removing " + name + " from server.");
                server.in.remove(this);
                server.sockets.remove(socket);
			}
        }
        
        public void run() {
            try{
            	Soutput = new ObjectOutputStream(socket.getOutputStream());
            	Soutput.flush();
                Sinput  = new ObjectInputStream(socket.getInputStream());

                try {
					name = (String)Sinput.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.exit(1);
				}
                sendData(data);
            }
            catch (IOException e) {
            	e.printStackTrace();
                System.out.println(name + ": Exception creating new Input stream: " + e);
                return;
            }
            
            System.out.println("Thread created for " + name + ".");
            
            
            do{
            	try {
            		Object o = Sinput.readObject();
            		server.update(o, name, false, null);
            	}
                catch (Exception e) {
                    System.out.println("Error receiving from " + name + ".");
                    System.out.println("Removing " + name + " from server.");
                    server.in.remove(this);
                    server.sockets.remove(socket);
                }
            }while(server.in.contains(this));
            
            
            // Finally close the input stream, no matter what.
            try{}
    		catch(Exception e){}
            finally{
            	try{
                    Sinput.close();
                    Soutput.close();
                    socket.close();
                }
                catch(Exception e) {}
            }
        }
    }
}
