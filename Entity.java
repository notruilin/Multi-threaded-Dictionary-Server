/*
   COMP90015 Distributed Systems - Assignment 1
   Name: RUILIN LIU
   User Name: RUILINL1
   Student Number: 871076
   Date: 1 Sep 2018
   The University of Melbourne
*/

import java.util.Date;
import java.io.Serializable;

public class Entity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String meanings;
	private Date date;
	
	Entity (String meanings) {
		this.meanings = meanings;
		this.date = new Date();
	}
	
	String getMeanings() {
		return meanings;
	}
	
	String getDate() {
		return date.toString();
	}
}
