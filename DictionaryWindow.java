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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.UIManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTabbedPane;
import java.awt.Font;

public class DictionaryWindow implements Runnable{

	private JFrame frame;
	private JTextField wordField;
	private JTextField addWordField;
	private DictionaryClient client;
	private JTextField removeField;
	
	private String address;
	private String port;
	
	@Override
	public void run() {
		this.frame.setVisible(true);
		client = new DictionaryClient();
		String res = client.openSocket(address, port);
		if (res.length() > 2 && res.charAt(0) == '$') {
			if (res.charAt(1) == 'E') {
				JOptionPane.showMessageDialog(null, res.substring(3));
		        	frame.dispose();
		        	System.exit(1);
			}
			if (res.charAt(1) == 'Q') {
				frame.dispose();
	        		System.exit(1);
			}
		}
	}
	
	private void showDialogExit(String res) {
		if (res.length() > 2 && res.charAt(0) == '$' && res.charAt(1) == 'E') {
			JOptionPane.showMessageDialog(null, res.substring(3));
			frame.dispose();
			System.exit(1);
		}
	}
	
	private boolean checkValid(String s) {
		if (s.indexOf('$') != -1 || s.indexOf('@') != -1) {
			return false;
		}
		return true;
	}
	
	private void invalidInput() {
		JOptionPane.showMessageDialog(null, "Invalid Input!");
	}
	
	public DictionaryWindow(String address, String port) {
		this.address = address;
		this.port = port;
		GUI();
	}

	private void GUI() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(UIManager.getColor("ColorChooser.background"));
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		frame.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    		super.windowClosing(e);
		    		
		    		String res = client.finish();
		    		frame.dispose();
		    		if (res.equals("$success"))
		    			System.exit(0);
		    		else
		    			System.exit(1);
		    }
		});
		
		
		
		//Search Panel
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(UIManager.getColor("CheckBox.background"));
		
		tabbedPane.addTab("Search", null, searchPanel, null);
		tabbedPane.setEnabledAt(0, true);
		searchPanel.setLayout(null);
		
		wordField = new JTextField();
		wordField.setBounds(6, 6, 334, 26);
		searchPanel.add(wordField);
		wordField.setColumns(10);
		wordField.setEditable(false);
		wordField.setBackground(Color.WHITE);
		wordField.setForeground(Color.GRAY);
		wordField.setText("Please type word here");
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(340, 6, 80, 29);
		searchPanel.add(btnSearch);
		
		JTextArea meaningsArea = new JTextArea();
		meaningsArea.setLineWrap(true);
		meaningsArea.setBounds(10, 40, 326, 180);
		searchPanel.add(meaningsArea);
		
		JScrollPane spMeaningsArea = new JScrollPane(meaningsArea);
		spMeaningsArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		spMeaningsArea.setBounds(10, 40, 326, 180);
		searchPanel.add(spMeaningsArea);
		
		wordField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (wordField.getText().equals("Please type word here")) {
					wordField.setEditable(true);
					wordField.setText("");
					wordField.setForeground(Color.BLACK);
				}
			}
		});
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				meaningsArea.setText("");
				String s = wordField.getText();
				if (checkValid(s)) {
					String res = client.searchAction(s);
					if (res.length() > 2 && res.charAt(0) == '$') {
						if (res.charAt(1) == 'M') {
							JOptionPane.showMessageDialog(null, res.substring(3));
						}
						else {
							showDialogExit(res);
						}
					}
					else {
						meaningsArea.setText(res);
					}
				}
				else invalidInput();
			}
		});
		
		
		
		
		
		//Add Panel
		JPanel addPanel = new JPanel();
		tabbedPane.addTab("Add", null, addPanel, null);
		addPanel.setLayout(null);
		
		addWordField = new JTextField();
		addWordField.setBounds(6, 6, 334, 26);
		addPanel.add(addWordField);
		addWordField.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(340, 6, 80, 29);
		addPanel.add(btnAdd);
		
		addWordField.setEditable(false);
		addWordField.setBackground(Color.WHITE);
		addWordField.setForeground(Color.GRAY);
		addWordField.setText("e.g. people");
		
		JTextArea addMeaningsArea = new JTextArea();
		addMeaningsArea.setWrapStyleWord(true);
		addMeaningsArea.setLineWrap(true);
		addMeaningsArea.setForeground(Color.GRAY);
		addMeaningsArea.setBounds(10, 40, 326, 180);
		addPanel.add(addMeaningsArea);
		addMeaningsArea.append("e.g. 1.men, women, and children.\n");
		addMeaningsArea.append("2.used to refer to everyone, or informally to the group that you are speaking to.\n");
		addMeaningsArea.append("3.men and women who are involved in a particular type of work.\n");
		
		JScrollPane spAddMeaningsArea = new JScrollPane(addMeaningsArea);
		spAddMeaningsArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		spAddMeaningsArea.setBounds(10, 40, 326, 180);
		addPanel.add(spAddMeaningsArea);
		
		addWordField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (addWordField.getText().equals("e.g. people")) {
					addWordField.setEditable(true);
					addWordField.setText("");
					addWordField.setForeground(Color.BLACK);
				}
			}
		});
		
		addMeaningsArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (addMeaningsArea.getText().length() > 4 && addMeaningsArea.getText().substring(0,4).equals("e.g.")) {
					addMeaningsArea.setEditable(true);
					addMeaningsArea.setText("");
					addMeaningsArea.setForeground(Color.BLACK);
				}
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = addWordField.getText();
				String m = addMeaningsArea.getText();
				if (addMeaningsArea.getText().length() > 4 && addMeaningsArea.getText().substring(0,4).equals("e.g.")) {
					m = "";
				}
				if (checkValid(s) && checkValid(m)) {
					String res = client.addAction(s,m);
					if (res.length() > 2 && res.charAt(0) == '$') {
						if (res.charAt(1) == 'M') {
							JOptionPane.showMessageDialog(null, res.substring(3));
						}
						else {
							showDialogExit(res);
						}
					}
				}
				else {
					invalidInput();
				}
			}
		});
		
		
		
		
		//Remove Panel
		JPanel removePanel = new JPanel();
		tabbedPane.addTab("Remove", null, removePanel, null);
		removePanel.setLayout(null);
		
		removeField = new JTextField();
		removeField.setBounds(6, 89, 334, 26);
		removePanel.add(removeField);
		removeField.setColumns(10);
		
		removeField.setEditable(false);
		removeField.setBackground(Color.WHITE);
		removeField.setForeground(Color.GRAY);
		removeField.setText("e.g. people");
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(343, 89, 80, 29);
		removePanel.add(btnRemove);
		
		removeField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (removeField.getText().equals("e.g. people")) {
					removeField.setEditable(true);
					removeField.setText("");
					removeField.setForeground(Color.BLACK);
				}
			}
		});
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = removeField.getText();
				String res = client.removeAction(s);
				if (checkValid(s)) {
					if (res.length() > 2 && res.charAt(0) == '$') {
						if (res.charAt(1) == 'M') {
							JOptionPane.showMessageDialog(null, res.substring(3));
						}
						else {
							showDialogExit(res);
						}
					}
				}
				else {
					invalidInput();
				}
			}
		});
	}
}
