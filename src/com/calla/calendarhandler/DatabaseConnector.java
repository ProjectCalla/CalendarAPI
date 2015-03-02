package com.calla.calendarhandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.mysql.jdbc.Statement;


public class DatabaseConnector {
	static Connection connection;

	public static void main(String[] args) throws Exception
	{
		/*Class.forName("com.mysql.jdbc.Driver");
		
		String serverName = "127.0.0.1:3306";
	    String mydatabase = "calendardump";
	    String url = "jdbc:mysql://" + serverName + "/" + mydatabase; 

	    String username = "root";
	    String password = "berebonk";
	    connection = DriverManager.getConnection(url, username, password);
		
		//Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:POORT/NAAM","root","berebonk");
	
		// pass berebonk 
		// user root
	    
	    System.out.println("klaar");
	    
	    
	    //connection.close();*/
	}
	
	public void connectToDB(){
		String serverName = "127.0.0.1:3306";
	    String mydatabase = "calendardump";
	    String url = "jdbc:mysql://" + serverName + "/" + mydatabase; 

	    String username = "root";
	    String password = "berebonk";
	    try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:POORT/NAAM","root","berebonk");
	
		// pass berebonk 
		// user root
	    
	    System.out.println("klaar");
	}
	
	
	public static void storeEvents(ArrayList<CalendarEvent> retrievedEvents){
		for(int i = 0; i < retrievedEvents.size(); i++){

			CalendarEvent tempEvent = retrievedEvents.get(i);
			System.out.println("1 "+tempEvent.description);
			try {
				if(tempEvent.location == null){
					tempEvent.location = "nvt";
				}
				System.out.println("2.5 ");
			    Statement st = (Statement) connection.createStatement(); 
			    System.out.println("2.6 ");
			    st.executeUpdate("INSERT INTO events(description, location, starttime, startdate, endtime, enddate) " 
			    + "VALUES ("+'"'+tempEvent.description+'"'+","+'"'+tempEvent.location+'"'+","+'"'+tempEvent.startingTime+'"'+","+'"'+tempEvent.startingDate+'"' +","+'"'+tempEvent.endingTime+'"'+","+'"'+tempEvent.endingDate+'"'+")");		
			    System.out.println("2.9 "+tempEvent.description);
			    //st.executeUpdate("DELETE FROM events WHERE location = 'henk';");			        		    
			}
			 		
		    catch (Exception ex) {
			   System.out.println("3 "+ex);	    
		    } 
		}
		
		
	    
	}

	
	    
	
}
