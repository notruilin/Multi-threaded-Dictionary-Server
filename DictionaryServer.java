/*
   COMP90015 Distributed Systems - Assignment 1
   Name: RUILIN LIU
   User Name: RUILINL1
   Student Number: 871076
   Date: 1 Sep 2018
   The University of Melbourne
*/

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class DictionaryServer{
	
	static String dictFile;
	static String port;
	static String address;
	static int total = 0;
	static int online = 0;
	
	private static ServerWindow window;
	private static ServerSocket listeningSocket;
	
	static void initDict() {
		String res;
		res = Dictionary.get().recoverDict(dictFile);
		if (res.length() > 2 && res.charAt(0) == '$' && res.charAt(1) == 'E') {
			window.showDialogExit(res);
		}
		else
			window.appendMessage("Successfully recover dictionary from \"" + dictFile + "\" .\n");
	}
	
	static void openSocket() {
		try {
			int portNum;
			portNum = Integer.parseInt(port);
			listeningSocket = new ServerSocket(portNum);
			window.appendMessage("Server listening on port " + port + " for connections\n");
		} catch (NumberFormatException e) {
			window.showDialogExit("$E@Invaild port!");
		} catch (Exception e) {
			window.showDialogExit("$E@Connection failure!");
		}
	}
	
	static void finish() {
		String res;
		res = Dictionary.get().saveDict(dictFile);
		if (res.length() > 2 && res.charAt(0) == '$' && res.charAt(1) == 'E') {
			window.showDialog(res.substring(3));
		}
		else {
			window.appendMessage("Dictionary has been saved.\n");
		}
		window.appendMessage("Server closed.\n");
		if(listeningSocket != null) {
			try {
				listeningSocket.close();
			} catch (IOException e) {
				window.showDialogExit("$E@Cannot close socket!");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		port = args[0];
		dictFile = args[1];
		address = InetAddress.getLocalHost().getHostAddress();
		window = new ServerWindow();
		
		if (args.length < 2) {
			System.out.println("Usage: java -jar DictionaryServer.jar <port> <dictionary-file>");
			System.exit(1);
		}
		
		initDict();
		openSocket();
		
		Thread myThread = new Thread(window);
		myThread.start();
		
		try {
			while (true) {
				Socket clientSocket = listeningSocket.accept();
				++total;
				++online;
				window.updateClientNum();
				DictionaryHandler server = new DictionaryHandler(clientSocket, total, window);
				new Thread(server).start();
			}
		}
		catch (Exception e) {
			window.showDialogExit("$E@Connection failure!");
		}
		finally {
			finish();
		}
	}
}
