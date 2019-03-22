/*
   COMP90015 Distributed Systems - Assignment 1
   Name: RUILIN LIU
   User Name: RUILINL1
   Student Number: 871076
   Date: 1 Sep 2018
   The University of Melbourne
*/

import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Dictionary {
	HashMap <String,Entity> dict = new HashMap<String,Entity>(100);
	static Dictionary instance = null;
	
	private Dictionary() {
		
	}
	
	public static Dictionary get() {
		if (instance == null) {
			instance = new Dictionary();
		}
		return instance;
	}
	
	synchronized void emptyDict() {
		dict.clear();
	}
	
	synchronized String recoverDict(String dictFile) {
		try {
			FileInputStream input = new FileInputStream(dictFile);
			ObjectInputStream object = new ObjectInputStream(input);
			dict = (HashMap<String,Entity>) object.readObject();
			input.close();
			object.close();
			return "$success";
		}
		catch (FileNotFoundException e) {
			return "$E@Cannot find the dictionary file.";
		}
		catch (IOException e) {
			return "$E@Reading dictionary file error.";
		}
		catch (ClassNotFoundException e) {
			return "$E@Class not found.";
		}
	}
	
	synchronized String saveDict(String dictFile) {
		try {
			FileOutputStream output = new FileOutputStream(dictFile);
			ObjectOutputStream object = new ObjectOutputStream(output);
			object.writeObject(dict);
			object.close();
			output.close();
			return "$success";
		}
		catch (IOException e) {
			return "$E@Saving dictionary file error.";
		}
	}
	
	synchronized boolean isExist(String word) {
		if (dict.get(word) == null)
			return false;
		return  true;
	}
	
	synchronized void addWord(String word,String meanings) {
		dict.put(word, new Entity(meanings));
	}
	
	synchronized void removeWord(String word) {
		dict.remove(word);
	}
	
	synchronized String getEntity(String word) {
		return dict.get(word).getMeanings() + "@" + dict.get(word).getDate();
	}
	
}
