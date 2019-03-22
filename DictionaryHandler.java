/*
   COMP90015 Distributed Systems - Assignment 1
   Name: RUILIN LIU
   User Name: RUILINL1
   Student Number: 871076
   Date: 1 Sep 2018
   The University of Melbourne
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class DictionaryHandler implements Runnable{
	
	private static ServerWindow window;
	
	private Socket socket;
	private final int num;
	
	DictionaryHandler(Socket socket, int num, ServerWindow theWind) {
		this.socket = socket;
		this.num = num;
		window = theWind;
	}
	
	String searchWord(String word) {
		String respond;
		if (!Dictionary.get().isExist(word))
			respond = "$M@Sorry, \"" + word + "\" doesn't exist.";
		else
			respond = Dictionary.get().getEntity(word);
		return respond;
	}
	
	String addWord(String word, String meanings) {
		String respond;
		if (Dictionary.get().isExist(word))
			respond = "$M@Sorry, \"" + word + "\" has already been added.";
		else {
			Dictionary.get().addWord(word, meanings);
			respond = "$M@Congratulations! \"" + word + "\" has been added.";
		}
		return respond;
	}
	
	String removeWord(String word) {
		String respond;
		if (!Dictionary.get().isExist(word))
			respond = "$M@Sorry, \"" + word + "\" doesn't exist.";
		else {
			Dictionary.get().removeWord(word);
			respond = "$M@Congratulations!\"" + word + "\" has been removed.";
		}
		return respond;
	}
		
	public void run() {
		try {
			window.appendMessage("Client " + num + " accepted:");
			window.appendMessage("Remote Port: " + socket.getPort());
			window.appendMessage("Remote Hostname: " + socket.getInetAddress().getHostName());
			window.appendMessage("Local Port: " + socket.getLocalPort() + "\n");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			String clientMsg = null;
			String[] command;
			String respond;
			while(true) {
				clientMsg = in.readLine();
				if (clientMsg != null) {
					command = clientMsg.split("@");
					window.appendMessage("Message from client " + num + ": " + clientMsg);
					switch (command[0]) {
					case "$sw":
						if (command.length < 2)
							respond = "$M@Lack of conditions!";
						else
							respond = searchWord(command[1]);
						break;
					case "$aw":
						if (command.length < 3)
							respond = "$M@Lack of conditions!";
						else
							respond = addWord(command[1],command[2]);
						break;
					case "$rw":
						if (command.length < 2)
							respond = "$M@Lack of conditions!";
						else
							respond = removeWord(command[1]);
						break;
					default:
						respond = "$M@Invalid Command!";
						break;
					}
					if (clientMsg.equals("$Disconnect")) {
						socket.close();
						window.appendMessage("Client " + num + " has disconnected.\n");
						String s;
						--DictionaryServer.online;
						window.updateClientNum();
						s = Dictionary.get().saveDict(DictionaryServer.dictFile);
						if (!s.equals("$success"))
							window.showDialogExit(s);
						else
							window.appendMessage("Dictionary has been saved.\n");
						break;
					}
					else {
						out.write(respond + "\n");
						out.flush();
						window.appendMessage("Response sent.\n");
					}
				}
			}
		} catch (Exception e) {
			window.showDialogExit("$E@Connection Error!" + "\n");
		}
	}
}
