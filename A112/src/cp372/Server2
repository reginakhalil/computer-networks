package cp372;
import java.net.*;
import java.util.ArrayList;
import java.io.*; 
import javax.swing.JOptionPane;

public class Server{
	
	 private static PrintWriter output; 
	 private static BufferedReader input; 
	
	//Main
	public static void main(String[] args) throws IOException  {

		int port = Integer.valueOf(args[0]);
		ServerSocket ss = new ServerSocket(port);
		
		while (true) {
			Socket s = null;
			
			try {
				s = ss.accept();
				
				//testing purposes only!
				System.out.println("Connected");
				
				//For top efficiency of converting bytes to characters, InputStreamReader is wrapped within BufferedReader
	            input = new BufferedReader(new InputStreamReader(System.in));
	            //wrapping getOutputStream within PrintWrtier
	            output = new PrintWriter(s.getOutputStream()); 
	            
	            Thread thread = new ClientServerThread(s, input, output);
	            
	            thread.start();
			} catch (Exception e){ 
                s.close(); 
                e.printStackTrace();
			}
		}

	}

}

