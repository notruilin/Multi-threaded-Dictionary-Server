
/*
   COMP90015 Distributed Systems - Assignment 1
   Name: RUILIN LIU
   User Name: RUILINL1
   Student Number: 871076
   Date: 1 Sep 2018
   The University of Melbourne
*/

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;

public class ServerWindow implements Runnable {

	private JFrame frame;
	private JTextArea messageArea;
	private JLabel clientLabel;
	private JLabel addressLabel;
	private JLabel portLabel;
	
	public void showDialogExit(String res) {
		if (res.length() > 2 && res.charAt(0) == '$' && res.charAt(1) == 'E') {
			JOptionPane.showMessageDialog(null, res.substring(3));
			frame.dispose();
			System.exit(1);
		}
	}

	public void showDialog(String res) {
		if (res.length() > 2 && res.charAt(0) == '$' && res.charAt(1) == 'E') {
			JOptionPane.showMessageDialog(null, res.substring(3));
		}
	}

	public void updateClientNum() {
		clientLabel.setText(Integer.toString(DictionaryServer.online));
	}

	public void appendMessage(String res) {
		messageArea.append(res + "\n");
	}

	@Override
	public void run() {
		this.frame.setVisible(true);

	}

	public ServerWindow() {
		ServerUI();
		addressLabel.setText(DictionaryServer.address);
		portLabel.setText(DictionaryServer.port);
	}

	private void ServerUI() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		messageArea = new JTextArea();
		messageArea.setEditable(false);
		messageArea.setBounds(10, 40, 410, 180);
		messageArea.setWrapStyleWord(true);
		messageArea.setLineWrap(true);
		frame.getContentPane().add(messageArea);

		JScrollPane spMessageArea = new JScrollPane(messageArea);
		spMessageArea.setBounds(10, 40, 410, 180);
		spMessageArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		frame.getContentPane().add(spMessageArea);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				DictionaryServer.finish();
				frame.dispose();
			}
		});

		JLabel lblOnlineClient = new JLabel("Online Client:");
		lblOnlineClient.setBounds(10, 12, 97, 16);
		frame.getContentPane().add(lblOnlineClient);

		clientLabel = new JLabel("0");
		clientLabel.setBounds(109, 12, 61, 16);
		frame.getContentPane().add(clientLabel);

		JLabel lblServerAddress = new JLabel("Server Address:");
		lblServerAddress.setBounds(10, 240, 128, 16);
		frame.getContentPane().add(lblServerAddress);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(303, 240, 61, 16);
		frame.getContentPane().add(lblPort);

		addressLabel = new JLabel("");
		addressLabel.setBounds(109, 240, 182, 16);
		frame.getContentPane().add(addressLabel);

		portLabel = new JLabel("");
		portLabel.setBounds(333, 240, 61, 16);
		frame.getContentPane().add(portLabel);

		JButton btnClearScreen = new JButton("Clear Screen");
		btnClearScreen.setBounds(300, 7, 125, 29);
		frame.getContentPane().add(btnClearScreen);

		btnClearScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				messageArea.setText("");
			}
		});

		JButton btnEmptyDict = new JButton("Empty Dictionary");
		btnEmptyDict.setBounds(160, 7, 145, 29);
		frame.getContentPane().add(btnEmptyDict);

		btnEmptyDict.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to empty the dictionary?",
						"Empty Dictionary Warnning", JOptionPane.YES_NO_OPTION);
				if (confirmed == JOptionPane.YES_OPTION) {
					Dictionary.get().emptyDict();
					appendMessage("Dictionary has been emptied.");
				}
			}
		});
	}
}
