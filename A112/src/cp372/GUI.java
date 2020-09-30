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

	public GUI() {
		
		setSize(660, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Book Storage Application");
		
		// Drop box elements
		String[] requestOptions = {"GET","SUBMIT","UPDATE","REMOVE"};
		JComboBox<String> dropBox = new JComboBox<String>(requestOptions);
		
		
		// Labels 
		JLabel ipLabel = new JLabel("IP:");
		JLabel portLabel  = new JLabel("Port:");
		JLabel dropLable = new JLabel("Requests Type:");
		JLabel cientTextAreaLabel  = new JLabel("Request Message:"); 
		JLabel serverTextAreaLable  = new JLabel("Resposne Message:");
		
		
		// buttons 
		JButton Submit  = new JButton("Send");
		Submit.setEnabled(false);
		JButton Disconnect  = new JButton("Disconnect");
		Disconnect.setEnabled(false);
		
		JButton Connect = new JButton("Connect");
	
		JButton Delete = new JButton("Clear");

		// check box 
		JCheckBox all = new JCheckBox("All");
		JCheckBox bibtex = new JCheckBox("BibTeX"); 

		// text field 
		JTextField ip = new JTextField(17);
		JTextField port = new JTextField(17);

		// text area 
		JTextArea clientTextArea = new JTextArea(10, 50);
		JTextArea serverTextArea = new JTextArea(10, 50);
		serverTextArea.setEditable(false);
		
		// Panels
		JPanel communcasPane = new JPanel();
		communcasPane.setLayout(new BoxLayout(communcasPane,BoxLayout.Y_AXIS));
		JPanel ipPanle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel newIpPanle = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel communcationsPane = new JPanel();
		communcationsPane.setLayout(new BoxLayout(communcationsPane,BoxLayout.Y_AXIS));
		JPanel ClientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel SendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel ServerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		// putting everything together 
		ipPanle.add(portLabel);
		ipPanle.add(port);
		ipPanle.add(ipLabel);
		ipPanle.add(ip);
		ipPanle.add(Connect);
		
		
		newIpPanle.add(dropLable);
		newIpPanle.add(dropBox);
		newIpPanle.add(Submit);
		newIpPanle.add(Delete);
		newIpPanle.add(all);
		newIpPanle.add(bibtex);
		newIpPanle.add(Disconnect);
		communcasPane.add(ipPanle);
		communcasPane.add(newIpPanle);
		
		add(communcasPane, BorderLayout.NORTH);
		
		ClientPanel.add(cientTextAreaLabel);	
		communcationsPane.add(ClientPanel);
		communcationsPane.add(new JScrollPane(clientTextArea));
		communcationsPane.add(SendPanel);
		ServerPanel.add(serverTextAreaLable);
		communcationsPane.add(ServerPanel);
		communcationsPane.add(new JScrollPane(serverTextArea));
		
		add(communcationsPane);
	
		
		//Action Listeners 
	    Connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ip.getText().isEmpty() && port.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Must enter an IP address and a port number", null, JOptionPane.ERROR_MESSAGE);
				}
				else if (port.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Must enter a port number", null, JOptionPane.ERROR_MESSAGE);
				}
				else if (ip.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Must enter an IP address", null, JOptionPane.ERROR_MESSAGE);
				}

					
				
			}
	    });
	    Delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clientTextArea.setText("");
				
			}
	    });

	}
}
