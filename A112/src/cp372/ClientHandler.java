package cp372;

import java.net.*; 
import java.io.*; 
import javax.swing.JOptionPane;
public class ClientHandler {
    
    private Socket socket = null; 
//  private DataInputStream input = null; 
    private PrintWriter output = null; 
    
    private BufferedReader input = null; 
    
    public boolean connect(String ip, int port) {
        
        boolean isConnected = false;
        try {
            socket = new Socket(ip, port); 
            
            //For top efficiency of converting bytes to characters, InputStreamReader is wrapped within BufferedReader
            input = new BufferedReader(new InputStreamReader(System.in));
            //wrapping getOutputStream within PrintWrtier
            output = new PrintWriter(socket.getOutputStream()); 
            
            //Display success message
            isConnected = true;
            JOptionPane.showMessageDialog(null, "Connection Successful", null, JOptionPane.DEFAULT_OPTION); 
        }
        catch (UnknownHostException u) {
            //Display error message
            isConnected = false;
            JOptionPane.showMessageDialog(null, "Connection Failed", null, JOptionPane.ERROR_MESSAGE); 
        }
        catch (IOException i) {
            //Display error message
            isConnected = false;
            JOptionPane.showMessageDialog(null, "Connection Failed", null, JOptionPane.ERROR_MESSAGE);
        }

        return isConnected;
    }
    
    public void sendRequest(String request) {
        
    }
    
    public void disconnect(){
        //close connections 
        try {
            input.close();
            output.close(); 
            socket.close();
            JOptionPane.showMessageDialog(null, "Disconnected", null, JOptionPane.DEFAULT_OPTION); 
        }
        catch(IOException i) {
            JOptionPane.showMessageDialog(null, "Failure", null, JOptionPane.ERROR_MESSAGE); 
        }
        
    }

}


