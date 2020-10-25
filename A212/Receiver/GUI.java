import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import javax.swing.*;

/* Relies on the following files to be in default package:
 * 	Receiver
 */

@SuppressWarnings("serial")
public class GUI extends JFrame
{
	public static void main(String[] args) {
		new GUI();
	}

	public GUI()
	{   	
		setSize(660, 200);
		setResizable(false);
		setVisible(true);
		initialize();


	
	}
	public void initialize() 
	{
			//Thread receiver;
		JTextArea packetsText = new JTextArea(1, 15);
		JButton Transfer;
	
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel senderAddrLabel  = new JLabel("Sender IP address:");

		JLabel receiverPortLabel  = new JLabel("Receiver port:         ");
		JLabel outputFile  = new JLabel("Output file:");
		JLabel Unreliable   = new JLabel("Unreliable transport:");
		JLabel senderPort  = new JLabel("Sender port:");
		JLabel Packets  = new JLabel("Number of accepted packets:");

		// check box 
		JCheckBox UnreliableCheckBox  = new JCheckBox("");

		// button 
		Transfer = new JButton("Transfer");

		// text field 
		JTextField senderAddr = new JTextField(17);
		JTextField receiverPort = new JTextField(17);
		JTextField file = new JTextField(17);
		JTextField SenderPortText = new JTextField(17);

		packetsText.setEditable(false);

		JPanel ipPanle1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel ipPanle2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel ipPanle3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel ipPanle4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel ipPanle5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel ipPanle6 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		ipPanle1.add(senderAddrLabel);
		ipPanle1.add(senderAddr);
		ipPanle1.add(senderPort);
		ipPanle1.add(SenderPortText);
		ipPanle2.add(receiverPortLabel);
		ipPanle2.add(receiverPort);
		ipPanle3.add(outputFile);
		ipPanle3.add(file);
		ipPanle4.add(Unreliable);
		ipPanle4.add(UnreliableCheckBox);
		ipPanle5.add(Transfer);
		ipPanle6.add(Packets);
		ipPanle6.add(packetsText);

		
		panel.add(ipPanle1);
		ipPanle4.setPreferredSize(new Dimension(ipPanle1.getPreferredSize().width, ipPanle1.getPreferredSize().height));
		panel.add(ipPanle2);
		panel.add(ipPanle3);
		panel.add(ipPanle4);
		panel.add(ipPanle5);
		panel.add(ipPanle6);

		add(panel);

	}

}