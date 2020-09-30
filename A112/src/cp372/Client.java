package cp372;

import java.net.*; 
import java.io.*; 
import javax.swing.JOptionPane;
public class Client {
    
    private Socket socket = null; 
    private DataInputStream input = null; 
    private DataOutputStream output = null; 
    
    
    public void connect(String ip, int port) {
        
        try {
            socket = new Socket(ip, port); 
            input = new DataInputStream(System.in); 
            output = new DataOutputStream(socket.getOutputStream()); 
            JOptionPane.showMessageDialog(null, "Connection Successful", null, JOptionPane.ERROR_MESSAGE); 
        }
        catch (UnknownHostException u) {
            JOptionPane.showMessageDialog(null, "Connection Failed", null, JOptionPane.ERROR_MESSAGE); 
        }
        catch (IOException i) {
            JOptionPane.showMessageDialog(null, "Connection Failed", null, JOptionPane.ERROR_MESSAGE);
        }
        //close connections
        close(); 
    
    }
    
    public void close(){
        //close connections 
        try {
            input.close();
            output.close(); 
            socket.close();
        }
        catch(IOException i) {
        }
        
    }

}


