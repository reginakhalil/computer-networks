package cp372;
import java.net.*;
import java.util.ArrayList;
import java.io.*; 
import javax.swing.JOptionPane;
public class Server{

	
	//Main
	public static void main(String[] args) throws IOException  {
		
		serverConnection(args[0]);

	}
	
	//Start server and connect to client when needed
	public static void serverConnection(String port) throws IOException{
		
		//Extract the port number
		int port_num;
		if (port.length() != 0) {
			port_num = Integer.parseInt(port); 
		}
		else {
			port_num = 4444;  
		}
		
		
		//Create server socket
		ServerSocket serverSocket = null; 
		try {
			serverSocket  = new ServerSocket(port_num);
		} catch (IOException e) {
			System.err.println("Could not listen to port: " + port); 
		}
		
		//Accept client socket
		Socket clientSocket = null; 
		boolean open = true;
		while(open) {
			try {
				clientSocket = serverSocket.accept(); 

			}catch(IOException e) {
				if (!open) {
					System.err.println("Server not running");
			}	
		  }	
		}		
	}
	


}
