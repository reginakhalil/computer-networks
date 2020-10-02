package cp372;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.ArrayList;

public class ClientServerThread extends Thread {
	//Global list 
	ArrayList<Request> Books = new ArrayList<Request>();
	PrintWriter output = null; 
	BufferedReader input = null; 
	
	Socket clientSocket = null; 
	
	public void run() {
		
		try {
			output = new PrintWriter(clientSocket.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
