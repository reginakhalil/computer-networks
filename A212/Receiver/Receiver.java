import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
import java.io.IOException;
import java.net.*;

/* Relies on the following files to be in default package:
 * 	Receiver
 */

@SuppressWarnings("serial")
public class Receiver extends JFrame
{
	public static void main(String[] args) {
		new Receiver();
	}
	
/* -------------------------------- FUNCTIONS --------------------------------
* This is where functions involving the transfer is created to be used with
* the GUI below
*/


	
/* -------------------------------- GUI --------------------------------
 * This is where GUI init and ActionListeners are created
 */

	private Container c;
	private JLabel title;
	private JLabel host; 
	private JLabel sender_udp;
	private JLabel reciever_udp;

	private JLabel file; 
	private JLabel recieved_pckts; 
	

	private JTextField host_text; 
	private JTextField sender_udp_text;
	private JTextField reciever_udp_text;
	private JTextField file_text;
	private JTextArea recieved_pckts_text;

	private JButton recieved; 

	private JCheckBox rdt;   //reliable data transfer
	private JCheckBox urdt;  //unreliable data transfer

	public Receiver()
	{   	
		//main frame layout
		setTitle("Reliable Data Transfer"); 
		setBounds(200, 90, 500, 400); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setResizable(false);
        

        //form layout
        c = getContentPane(); 
        c.setLayout(null); 

        //Title 
        title = new JLabel("Reliable Data Transfer"); 
        title.setFont(new Font("Arial", Font.PLAIN, 20)); 
        title.setSize(300, 30); 
        title.setLocation(146, 5); 
        c.add(title); 

        //Host addres of the sender input
        host = new JLabel("Host IP: "); 
        host.setFont(new Font("Arial", Font.PLAIN, 15)); 
        host.setSize(100, 20); 
        host.setLocation(70, 50); 
        c.add(host); 

        host_text = new JTextField(); 
        host_text.setFont(new Font("Arial", Font.PLAIN, 15)); 
        host_text.setSize(190, 20); 
        host_text.setLocation(170, 50); 
        c.add(host_text);


       	//UDP port of sender
        sender_udp = new JLabel("Sender Port: "); 
        sender_udp.setFont(new Font("Arial", Font.PLAIN, 15)); 
        sender_udp.setSize(100, 20); 
        sender_udp.setLocation(70, 100); 
        c.add(sender_udp); 

        sender_udp_text = new JTextField(); 
        sender_udp_text.setFont(new Font("Arial", Font.PLAIN, 15)); 
        sender_udp_text.setSize(190, 20); 
        sender_udp_text.setLocation(170, 100); 
        c.add(sender_udp_text);


       	//UDP port of reciever
        reciever_udp = new JLabel("Reciever Port: "); 
        reciever_udp.setFont(new Font("Arial", Font.PLAIN, 15)); 
        reciever_udp.setSize(100, 20); 
        reciever_udp.setLocation(70, 150); 
        c.add(reciever_udp); 

        reciever_udp_text = new JTextField(); 
        reciever_udp_text.setFont(new Font("Arial", Font.PLAIN, 15)); 
        reciever_udp_text.setSize(190, 20); 
        reciever_udp_text.setLocation(170, 150); 
        c.add(reciever_udp_text);


       	//File name
        file = new JLabel("File Name: "); 
        file.setFont(new Font("Arial", Font.PLAIN, 15)); 
        file.setSize(100, 20); 
        file.setLocation(70, 200); 
        c.add(file); 

        file_text = new JTextField(); 
        file_text.setFont(new Font("Arial", Font.PLAIN, 15)); 
        file_text.setSize(190, 20); 
        file_text.setLocation(170, 200); 
        c.add(file_text);

        //Reliable Data transfer Checkbox
        rdt = new JCheckBox("Reliable Data Transfer"); 
        rdt.setFont(new Font("Arial", Font.PLAIN, 13)); 
        rdt.setSize(190, 20); 
        rdt.setLocation(70, 250); 
        c.add(rdt);

        //unreliable Data transfer Checkbox
        urdt = new JCheckBox("Unreliable Data Transfer"); 
        urdt.setFont(new Font("Arial", Font.PLAIN, 13)); 
        urdt.setSize(190, 20); 
        urdt.setLocation(70, 280); 
        c.add(urdt);

        //Receive button
        recieved = new JButton("Receive"); 
        recieved.setFont(new Font("Arial", Font.PLAIN, 15)); 
        recieved.setSize(90, 20); 
        recieved.setLocation(300, 250);
        c.add(recieved); 

        // Number of recieved in-order packets
        recieved_pckts = new JLabel("Number of Received Packets: "); 
        recieved_pckts.setFont(new Font("Arial", Font.PLAIN, 13)); 
        recieved_pckts.setSize(200, 20); 
        recieved_pckts.setLocation(70, 320); 
        c.add(recieved_pckts); 

        recieved_pckts_text = new JTextArea(); 
        recieved_pckts_text.setFont(new Font("Arial", Font.PLAIN, 15)); 
        recieved_pckts_text.setSize(100, 20); 
        recieved_pckts_text.setLocation(250, 320); 
        recieved_pckts_text.setLineWrap(true); 
        recieved_pckts_text.setEditable(false); 
        c.add(recieved_pckts_text); 

        setVisible(true);
        
		//-----------------Action Listeners------------
		
		/*
		 * The "Receive" Button 
		 * 
		 */
        recieved.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String host = host_text.getText();
				String sender = sender_udp_text.getText();
				String receiver = reciever_udp_text.getText();
				String file = file_text.getText();
				boolean unreliable = urdt.isSelected();
				boolean reliable = rdt.isSelected();
				int seqNum;
				if (host.equals("") || sender.equals("") || receiver.equals("") || file.equals("") && reliable == false && unreliable == false) {
					JOptionPane.showMessageDialog(null, "Empty field(s) detected", null, JOptionPane.ERROR_MESSAGE);
				} else if (unreliable == true && reliable == false) {
					
				} else if (reliable == true && unreliable == false){
					
				} else {
					JOptionPane.showMessageDialog(null, "Must select only one UDP transfer option", null, JOptionPane.ERROR_MESSAGE);
				}
				
			}
	    });

	
	}

}
