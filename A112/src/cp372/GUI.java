package cp372;

import java.awt.BorderLayout;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@SuppressWarnings("serial")
public class GUI extends JFrame {

	private final ClientHandler client;

	public GUI() {
		
		
		setSize(660, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
		setVisible(true);
		setTitle("Book Storage Application");
		client = new ClientHandler();
		

		
	}
	private void initialize() {
		// Drop box elements
		String[] requestOptions = {"GET","SEND","UPDATE","REMOVE"};
		JComboBox<String> dropBox = new JComboBox<String>(requestOptions);
		
		// Labels 
		JLabel ipLabel = new JLabel("IP:");
		JLabel portLabel  = new JLabel("Port:");
		JLabel dropLable = new JLabel("Requests Type:");

		JLabel serverTextAreaLable  = new JLabel("Resposne Message:");
		
		
		JLabel isbnLabel = new JLabel("ISBN: "); 
		JLabel titleLabel = new JLabel("Title: "); 
		JLabel authorLabel = new JLabel("Author: "); 
		JLabel yearLabel = new JLabel("Year: "); 
		JLabel publisherLabel = new JLabel("Publisher: "); 
		
		// buttons 
		JButton Send  = new JButton("Send");
		JButton Disconnect  = new JButton("Disconnect");
		Disconnect.setEnabled(false);
		
		JButton Connect = new JButton("Connect");
		JButton Delete = new JButton("Clear");

		// check box 
		JCheckBox all = new JCheckBox("All");
		JCheckBox bibtex = new JCheckBox("BibTeX"); 

		// text field 
		JTextField ip = new JTextField(10);
		JTextField port = new JTextField(10);

		JTextField isbn = new JTextField(13);
		JTextField title = new JTextField(13);
		JTextField author = new JTextField(13);
		JTextField year = new JTextField(13);
		JTextField publisher = new JTextField(13);
		
		
		JTextArea serverTextArea = new JTextArea(14, 50);
		serverTextArea.setEditable(false);
		
		// Panels
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
		
		//The "Connect" Button 
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
				}
				
				 
				//if connected then enable disconnect button
				if (!ip_address.isEmpty() && !port_number.isEmpty()) {
					//need to validate port and IP
					int port_num = Integer.parseInt(port_number);
					if(client.connect(ip_address, port_num));
						Disconnect.setEnabled(true);
				}
	
			}
	    });
	    
	    //The "Clear" Button
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
	    
	    //The "Send" Button
	    Send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String type = (String) dropBox.getSelectedItem();
				String Isbn = isbn.getText(); 
				String book_title = title.getText(); 
				String book_auth = author.getText();
				String book_pub = publisher.getText();
				String book_year = year.getText();
				
				Request msg = new Request(type, Isbn,book_title, book_auth,book_pub,book_year);
				String request = msg.toString(); 
				
				boolean validrequest = msg.validateRequest();
				
				if (validrequest) {
					client.sendRequest(request);
				}		
			}
	    });
	    
	    //The "Send" Button
	    Disconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.disconnect();
			}
	    });
	    

	}
	


}

