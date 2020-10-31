import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;

/* Relies on the following files to be in default package:
 * 	Receiver
 */

@SuppressWarnings("serial")
public class Receiver extends JFrame
{
	private static String fileName = "";
    private static String decodedDataUsingUTF82 = null;
    private static int total = 0;
    
	public static void main(String[] args) {
		new Receiver();
	}
	
/* -------------------------------- FUNCTIONS --------------------------------
* This is where functions involving the transfer is created to be used with
* the GUI below
*/
	public static int initSend (int rport, InetAddress address, int sport, String file, boolean rdtBool) throws IOException {
		DatagramSocket rsocket = new DatagramSocket(rport);
		byte[] receiveFile = new byte[1024];
		DatagramPacket filePckt = new DatagramPacket(receiveFile, receiveFile.length);
		rsocket.receive(filePckt);
		
		try {
            decodedDataUsingUTF82 = new String(receiveFile, "UTF-8");
        } catch (Exception unsupportedFile) {
        	unsupportedFile.printStackTrace();
        }
		
		setFileName(file);
		File fileExport = new File (file);
		FileOutputStream outFile = new FileOutputStream(fileExport);
		
		if (rdtBool) {
			total = rdt(outFile, rsocket, sport, address);
			byte [] finalData = new byte[1024];
			DatagramPacket rcvd = new DatagramPacket (finalData, finalData.length);
			rsocket.receive(rcvd);
		} else {
			total = udt(outFile, rsocket, sport, address);
			byte [] finalData = new byte[1024];
			DatagramPacket rcvd = new DatagramPacket (finalData, finalData.length);
			rsocket.receive(rcvd);
		}
		
		
		return total;
	}
	
	private static int udt(FileOutputStream outFile, DatagramSocket ssocket, int port, InetAddress address) throws IOException {
		// flag to receive the final pckt
			boolean flag;
			int seqNum = 0;
			int last = 0;
			
			while (true) {
				byte[] msg = new byte [1024];
				byte [] fileArr = new byte [1021];
				
				// receive pckt and msg
				DatagramPacket rcvd = new DatagramPacket(msg, msg.length);
				
				// if it is a multiple of 10, we will drop
				ssocket.receive(rcvd);
			
				//get pckt data
				msg = rcvd.getData();
				
				/*
				 * here, we are getting the temporary # of total packets
				 * we then will do a for and loop thru and do a counter 
				 * when we encounter a pckt whose seqNum is a multiple of 10
				 * then, we will subtract total packets from that,
				 * to simulate every 10th packet being dropped.
				 */
				int total1 = 0;
				total1 = rcvd.getLength() + total1;
				int drop = 0;
				for (int i = 0; i < total1; i++) {
					if (i%10==0) {
						drop++;
					}
				}
				
				total = rcvd.getLength()-drop;
				
				//get seqNum
				seqNum = ((msg[0] & 0xff) << 8) + (msg[1] & 0xff);
				
				/* getting flag for last msg 
				 * -> if true, something's wrong
				 */
				flag = (msg[2] & 0xff) == 1;
				
				/*
				 * checking if seqNum is last val + 1 (correct)
				 * gotta get data and write it if correct
				 */
				if (seqNum == (last+1)) {
					last = seqNum;
					System.arraycopy(msg, 3, fileArr, 0, 1021);
					
					// write to file
					sendACK(last, ssocket, address, port);
				}
				
				// checking last msg
				if (flag) {
					outFile.close();
					break;
				}
				
			}	
			return total;
	}


	
	private static int rdt(FileOutputStream outFile, DatagramSocket ssocket, int port, InetAddress address) throws IOException {
	// flag to receive the final pckt
		boolean flag;
		int seqNum = 0;
		int last = 0;
		
		while (true) {
			byte[] msg = new byte [1024];
			byte [] fileArr = new byte [1021];
			
			// receive pckt and msg
			DatagramPacket rcvd = new DatagramPacket(msg, msg.length);
			ssocket.receive(rcvd);
			//get pckt data
			msg = rcvd.getData();
			
			total = rcvd.getLength() + total;
			
			//get seqNum
			seqNum = ((msg[0] & 0xff) << 8) + (msg[1] & 0xff);
			
			/* getting flag for last msg 
			 * -> if true, something's wrong
			 */
			flag = (msg[2] & 0xff) == 1;
			
			/*
			 * checking if seqNum is last val + 1 (correct)
			 * gotta get data and write it if correct
			 */
			if (seqNum == (last+1)) {
				last = seqNum;
				System.arraycopy(msg, 3, fileArr, 0, 1021);
				
				// write to file
				sendACK(last, ssocket, address, port);
			}
			
			// checking last msg
			if (flag) {
				outFile.close();
				break;
			}
			
		}	
		return total;
	}

	private static void sendACK(int last, DatagramSocket ssocket, InetAddress address, int port) throws IOException {
		byte[] ack = new byte[2];
		ack[0] = (byte) (last >> 8);
		ack[1] = (byte) (last);
		
		//sending
		DatagramPacket ackPckt = new DatagramPacket(ack, ack.length, address, port);
        ssocket.send(ackPckt);
		
	}

	
	private static void setFileName(String file) {
		fileName = file;
	}

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
        recieved.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sendText = sender_udp_text.getText();
				String host = host_text.getText();
				String recText = reciever_udp_text.getText();
				String file = file_text.getText();
				boolean unreliable = urdt.isSelected();
				boolean reliable = rdt.isSelected();
				InetAddress ip = null;
				try {
					ip = InetAddress.getByName(host);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
	
				if (host.equals("") || sendText.equals("") || recText.equals("") || file.equals("") || (reliable == false && unreliable == false)) {
					JOptionPane.showMessageDialog(null, "Empty field(s) detected", null, JOptionPane.ERROR_MESSAGE);
				} else if ((unreliable == true && reliable == false)|| (reliable == true && unreliable == false)) {
					int sender = Integer.parseInt(sender_udp_text.getText());
					int receiver = Integer.parseInt(reciever_udp_text.getText());
					try {
						total = initSend(receiver, ip, sender, file, reliable);
						recieved_pckts_text.setText(Integer.toString(total));
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} else {
					JOptionPane.showMessageDialog(null, "Must select only one UDP transfer option", null, JOptionPane.ERROR_MESSAGE);
				}
				
			}
	    } );

	}

}
