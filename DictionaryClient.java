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
import java.net.UnknownHostException;

public class DictionaryClient {
	
	private BufferedReader in;
	private BufferedWriter out;
	
	private Socket socket;
	private String address;
	private int port;
	
	String replaceNewline(String m) {
		String newm = "";
		for (int i = 0; i < m.length(); i++) {
			if (m.charAt(i) == '\n')
				newm += '#';
			else
				newm += m.charAt(i);
		}
		return newm;
	}
	
	String recover(String m) {
		String newm = "";
		for (int i = 0; i < m.length(); i++) {
			if (m.charAt(i) == '#')
				newm += '\n';
			else
				if (m.charAt(i) == '@')
					newm += "\n\n";
				else
					newm += m.charAt(i);
		}
		return newm;
	}
	
	String searchAction(String s) {
		try {
			out.write("$sw@" + s + "\n");
			out.flush();
			String received = in.readLine();
			if (received == null)
				throw new Exception();
			received = recover(received);
			return received;
		} catch (Exception e) {
			return "$E@" + "Cannot connect to the server!";
		}
	}
	
	String addAction(String s, String m) {
		try {
			m = replaceNewline(m);
			out.write("$aw@" + s + "@" + m + "\n");
			out.flush();
			String received = in.readLine();
			if (received == null)
				throw new Exception();
			return received;
		} catch (Exception e) {
			return "$E@" + "Cannot connect to the server!";
		}
	}
	
	String removeAction(String s) {
		try {
			out.write("$rw@" + s + "\n");
			out.flush();
			String received = in.readLine();
			if (received == null)
				throw new Exception();
			return received;
		} catch (Exception e) {
			return "$E@" + "Cannot connect to the server!";
		}
	}
	
	String openSocket(String address, String port) {
		try {
			this.address = address;
			this.port = Integer.parseInt(port);
			socket = new Socket(this.address, this.port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			return "$success";
		} catch (UnknownHostException e) {
			return "$E@" + "Unkonwn Host!";
		} catch (Exception e) {
			return "$E@" + "Cannot connect to the server!";
		}
	}
	
	String finish() {
		try {
			out.write("$Disconnect" + "\n");
			out.flush();
			socket.close();
			return "$success";
		} catch (Exception e) {
			return "$Q@";
		}
	}
	
	public static void main(String[] args) {

		if (args.length < 2) {
			System.out.println("Usage: java -jar DictionaryClient.jar <server-address> <server-port>");
			System.exit(1);
		}
			
		DictionaryWindow window = new DictionaryWindow(args[0], args[1]);
		Thread myThread = new Thread(window);
		myThread.start();
	}
}