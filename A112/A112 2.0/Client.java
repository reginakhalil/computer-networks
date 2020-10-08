import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.Socket;


@SuppressWarnings("serial")
public class Client extends JFrame {
	
	// connection purposes
	public static Socket socket;
	private static BufferedReader input;
	private static ObjectOutputStream output;
	public int port;
	public String ip;
	
	/*
	 *  check if the year is valid
	 */
	
	public static boolean validYear (String year) {
		try {
			int yr = Integer.parseInt(year);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	/* 
	 * check if isbn is valid
	 */
	public static boolean validISBN (String isbn) {
		int len = isbn.length();
        if ((isbn == null) || (len!=13)){
        	return false;
        }
        /*
         * remove any hyphens
         */
        isbn = isbn.replaceAll( "-", "" );
       
        try {
            int sum = 0;
            for (int i = 0; i < 12; i++) {
                int digit = Integer.parseInt( isbn.substring( i, i + 1 ) );
                sum+= (i % 2 == 0) ? (digit*1) : (digit*3);
             }

            /*
             * testsum must be 0-9. If calculated as 10 then = 0
             */
            int testsum = 10 - (sum % 10);
            if (testsum == 10) {
                testsum = 0;
            } if ((testsum == Integer.parseInt(isbn.substring(12))) == true) {
            	return true; 
            } else {
            	return false; 
            }
        } catch (NumberFormatException nfe) {
            //to catch invalid ISBNs that have non-numeric characters in them
        	return false;
        }
       
	}
	// dont need an IP addr validator because someone might type in localHost and not 127.0.0.1 for ex.
	
	public Client () {
		setSize(660, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
		setVisible(true);
		setTitle("Book Storage Application");
	}
	public void initialize() {
		/*
		 *  Drop box elements
		 */
		String[] requestOptions = {"GET","SUBMIT","UPDATE","REMOVE"};
		JComboBox<String> dropBox = new JComboBox<String>(requestOptions);
		
		/*
		 *  Labels 
		 */
		JLabel ipLabel = new JLabel("IP:");
		JLabel portLabel  = new JLabel("Port:");
		JLabel dropLable = new JLabel("Requests Type:");

		JLabel serverTextAreaLable  = new JLabel("Response Message:");
		
		
		JLabel isbnLabel = new JLabel("ISBN: "); 
		JLabel titleLabel = new JLabel("Title: "); 
		JLabel authorLabel = new JLabel("Author: "); 
		JLabel yearLabel = new JLabel("Year: "); 
		JLabel publisherLabel = new JLabel("Publisher: "); 
		
		/*
		 *  buttons 
		 */
		JButton Send  = new JButton("Send");
		JButton Disconnect  = new JButton("Disconnect");
		Disconnect.setEnabled(false);
		
		JButton Connect = new JButton("Connect");
		JButton Delete = new JButton("Clear");

		/*
		 *  check boxes 
		 */
		JCheckBox all = new JCheckBox("All");
		JCheckBox bibtex = new JCheckBox("BibTeX"); 

		/*
		 *  text field 
		 */
		JTextField ip = new JTextField(10);
		JTextField port = new JTextField(10);

		JTextField isbn = new JTextField(13);
		JTextField title = new JTextField(13);
		JTextField author = new JTextField(13);
		JTextField year = new JTextField(13);
		JTextField publisher = new JTextField(13);
		
		
		JTextArea serverTextArea = new JTextArea(14, 50);
		serverTextArea.setEditable(false);
		
		/*
		 *  Panels
		 */
		JPanel communcasPane = new JPanel();
		communcasPane.setLayout(new BoxLayout(communcasPane,BoxLayout.Y_AXIS));
		JPanel ipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel newipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel communcationsPane = new JPanel();
		communcationsPane.setLayout(new BoxLayout(communcationsPane,BoxLayout.Y_AXIS));
		JPanel ClientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel SendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel ServerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
 
		ipPanel.add(portLabel);
		ipPanel.add(port);
		ipPanel.add(ipLabel);
		ipPanel.add(ip);
		ipPanel.add(Connect);
		
		
		newipPanel.add(dropLable);
		newipPanel.add(dropBox);
		newipPanel.add(Send);
		newipPanel.add(Delete);
		newipPanel.add(all);
		newipPanel.add(bibtex);
		newipPanel.add(Disconnect);
		communcasPane.add(ipPanel);
		communcasPane.add(newipPanel);
		
		add(communcasPane, BorderLayout.NORTH);
		
		ClientPanel.add(isbnLabel);
		ClientPanel.add(isbn);
		
		ClientPanel.add(titleLabel);
		ClientPanel.add(title);
		
		ClientPanel.add(authorLabel);
		ClientPanel.add(author);
		
		ClientPanel.add(yearLabel);
		ClientPanel.add(year);
		
		ClientPanel.add(publisherLabel);
		ClientPanel.add(publisher);
		 
		communcationsPane.add(ClientPanel);
		communcationsPane.add(SendPanel);
		ServerPanel.add(serverTextAreaLable);
		communcationsPane.add(ServerPanel);
		communcationsPane.add(new JScrollPane(serverTextArea));
		
		add(communcationsPane);
		
		//-----------------Action Listeners------------
		
		/*
		 * The "Connect" Button 
		 * Connects to the server
		 */
	    Connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String ip_address = ip.getText(); 
				String port_number = port.getText(); 
				
				if (ip_address.equals("") && port_number.equals("")) {
					JOptionPane.showMessageDialog(null, "Must enter an IP address and a port number", null, JOptionPane.ERROR_MESSAGE);
				}
				else if (port_number.equals("")) {
					JOptionPane.showMessageDialog(null, "Must enter a port number", null, JOptionPane.ERROR_MESSAGE);
				}
				else if (ip_address.equals("")) {
					JOptionPane.showMessageDialog(null, "Must enter an IP address", null, JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						int portNum = Integer.parseInt(port_number);
						socket = new Socket (ip_address, portNum);
						//if connected then enable disconnect button
						JOptionPane.showMessageDialog(null, "Connection success", null, JOptionPane.PLAIN_MESSAGE);
						Disconnect.setEnabled(true);
					} catch (Exception connect) {
						JOptionPane.showMessageDialog(null, "Connection failed", null, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
	    });
	    
	    /*
	     * The "Clear" Button
	     * Clears ALL contents of the list
	     */
	    Delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isbn.setText("");
				title.setText("");
				author.setText("");
				year.setText("");
				publisher.setText("");
			}
	    });
	    
	    /*
	     * The "Send" Button which sends to server
	     */
	    Send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				
				/*
				 * Array list of book entry to be sent
				 */
				ArrayList<String> entry = new ArrayList<String>();
				
				try {
					String type = (String) dropBox.getSelectedItem();
					String Isbn = isbn.getText(); 
					String book_title = title.getText(); 
					String book_auth = author.getText();
					String book_pub = publisher.getText();
					String book_year = year.getText();
					boolean allState = all.isSelected();
					boolean bibtexState = bibtex.isSelected();
					
					/*
					 *  if statements to check if they fields are empty before sending data
					 */
					if (Isbn.isEmpty()) {
						Isbn = null;
					}
					
					if (book_title.isEmpty()) {
						book_title = null;
					}
					
					if (book_auth.isEmpty()) {
						book_auth = null;
					}
					
					if (book_year.isEmpty()) {
						book_year = null;
					}
					
					/*
					 *  init sending to server int to 0 aka false
					 */
					int sending = 0;
					
					/*
					 *  as long one field isnt null - can send 
					 */
					if (Isbn != null || book_title != null || book_auth != null || book_year != null || book_year != null) {
						entry.add(type);
						entry.add(Isbn);
						entry.add(book_title);
						entry.add(book_auth);
						entry.add(book_pub);
						entry.add(book_year);
						entry.add(String.valueOf(allState));
						entry.add(String.valueOf(bibtexState));
						sending = 1;
						
					}
					
					/*
					 *  sends to server through the stream and reader
					 */
					if (sending == 1) {
						try {
							ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
							output.writeObject(entry);
							input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							String re = input.readLine();
							serverTextArea.setText(re);
								
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "Cannot send to server", null, JOptionPane.ERROR_MESSAGE);
						}
					}
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Cannot create entry", null, JOptionPane.ERROR_MESSAGE);
				}
				

			}
	    });
	    
	    /*
	     * The "Disconnect" Button
	     * Closes the socket and clears
	     */
	    Disconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Client.socket.close();
					/* 
					 * this will invoke the delete button to clear contents once server disconnects
					 */
					Delete.doClick();
					System.exit(0);
				} catch (Exception e3) {
					JOptionPane.showMessageDialog(null, "Unable to disconnect", null, JOptionPane.ERROR_MESSAGE);
				}	
			}
	    });
	}
	
	public static void main(String[] args) {
		new Client();
	}
}
