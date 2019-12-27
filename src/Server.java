import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.Timer;

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
        data = new Data();
        try
        {
            serverSocket = new ServerSocket(port);
            System.out.println("Server waiting for client on port " + serverSocket.getInetAddress().getHostAddress() + ":" +  serverSocket.getLocalPort());
 
            while(true)
            {
                sockets.add(serverSocket.accept());
                System.out.println("New client asked for a connection");
                in.add(new InfoThread(sockets.get(sockets.size()-1), this));
                in.get(sockets.size()-1).start();
            }
        }
        catch (IOException e) {
            System.out.println("Exception on new ServerSocket: " + e);
            System.exit(1);
        }
        try{}
		catch(Exception e){} 
    }    
    
	public synchronized void updateData(String s){
		if (s.equals("RES")){
			// Store info on files
			// TODO
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
				e.printStackTrace();
                System.out.println("Error sending to " + name + ".");
                System.out.println("Removing " + name + " from server.");
                server.in.remove(this);
                server.sockets.remove(socket);
			}
        }
        
        public void sendData(Data d){
        	try {
        		Soutput.reset();
    			Soutput.writeUnshared(d);
    			Soutput.flush();
			} catch (IOException e) {
				e.printStackTrace();
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
            		server.updateData((String)o);
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
                }
                catch(Exception e) {}
            }
        }
    }
}
