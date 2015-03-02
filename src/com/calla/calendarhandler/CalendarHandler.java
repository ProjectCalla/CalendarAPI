package com.calla.calendarhandler;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CalendarHandler {
	
	HttpTransport httpTransport;
	JacksonFactory jsonFactory;
	Credential credential;
	EventsContainer eventsContainer; 
	DatabaseConnector databaseConnector;
	
	public static void main (String[]args) throws IOException, GeneralSecurityException{
		CalendarHandler calendarHandler = new CalendarHandler();
		calendarHandler.setUp();
		calendarHandler.getCalendarEvents();
	}
	

	public void setUp() throws GeneralSecurityException, IOException {
		eventsContainer = new EventsContainer();
		
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	    jsonFactory = JacksonFactory.getDefaultInstance();

	    // The clientId and clientSecret can be found in Google Developers Console
	    String clientId = "888492218428-m6ho1inck0seg1u702utp20c3k5or3dm.apps.googleusercontent.com";
	    String clientSecret = "zB_02vhdX6Wu5nf7HJDqtbHf";

	    // Or your redirect URL for web based applications.
	    String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
	    String scope = "https://www.googleapis.com/auth/calendar";

	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow(
	        httpTransport, jsonFactory, clientId, clientSecret, Collections.singleton(scope));
	    
	    // Step 1: Authorize
	    String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUrl).build();

	    // Point or redirect your user to the authorizationUrl.
	    System.out.println("Go to the following link in your browser:");
	    System.out.println(authorizationUrl);

	    // Read the authorization code from the standard input stream.
	    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    System.out.println("What is the authorization code?");
	    String code = in.readLine();
	    // End of Step 1

	    // Step 2: Exchange
	    GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUrl)
	        .execute();
	    // End of Step 2

	    System.out.println("gelukt");
	    
	    credential = new GoogleCredential.Builder()
	        .setTransport(httpTransport)
	        .setJsonFactory(jsonFactory)
	        .setClientSecrets(clientId, clientSecret)
	        .build().setFromTokenResponse(response);
	    }
		
		public void getCalendarEvents() throws IOException{
			Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credential)
	        .setApplicationName("YOUR_APPLICATION_NAME").build();
	    
			// Iterate over the events in the specified calendar
		    String pageToken = null;
		    do {
		        Events events = service.events().list("ml6lm5edcveg4uorg2d8mukgpplcu2nn@import.calendar.google.com").setPageToken(pageToken).execute();
		        List<Event> items = events.getItems();
		        for (Event event : items) {
		    	    
		        	CalendarEvent retrievedEvent = new CalendarEvent();
		        	retrievedEvent.description = event.getDescription();
		        	retrievedEvent.location = event.getLocation();
		        	System.out.println(event.getStart());
		        	
		        	ArrayList startingDateAndTime = parseDateTime(event.getStart());
		        	ArrayList endingDateAndTime = parseDateTime(event.getEnd());
		        	
		        	retrievedEvent.startingDate = (String) startingDateAndTime.get(0);
		        	retrievedEvent.startingTime = (String) startingDateAndTime.get(1);
		        	retrievedEvent.endingDate = (String) endingDateAndTime.get(0);
		        	retrievedEvent.endingTime = (String) endingDateAndTime.get(1);
		        	
		        	//ArrayList<Object> updatedEventsList = eventsContainer.getEventList();
		        	ArrayList<Object> updatedEventsList = new ArrayList<Object>();
		        	
		        	eventsContainer.setEventList(retrievedEvent);
		     
		      }
		        
		      pageToken = events.getNextPageToken();
		    } while (pageToken != null);
		    
		    
		    /** test output 
		    ArrayList<CalendarEvent> testert = new ArrayList<CalendarEvent>();
		    testert = eventsContainer.getEventList();
		    for (int i = 0; i < testert.size(); i++){
		    	CalendarEvent henk = testert.get(i); 
		    	System.out.println(henk.description);
		    	System.out.println(henk.location);
		    	System.out.println(henk.startingTime);
		    	System.out.println(henk.startingDate);
		    	System.out.println(henk.startingTime);
		    	System.out.println(henk.endingDate);
		    	System.out.println(henk.endingTime);
		    // geeft soms null op location, maar dat komt door de vakantie
		    }
		     test output einde **/
			databaseConnector = new DatabaseConnector();
			databaseConnector.connectToDB();
			databaseConnector.storeEvents(eventsContainer.getEventList());		    
		}
		
		public static ArrayList<String> parseDateTime(EventDateTime unparsedDateAndTime){
			ArrayList<String> parsedDateAndTime = new ArrayList<String>();
			
			String stringifiedDateAndTime = unparsedDateAndTime.toString();
			String parsedDate = stringifiedDateAndTime.substring(13, 23);
			String parsedTime = stringifiedDateAndTime.substring(24, 29);
			
			parsedDateAndTime.addAll(Arrays.asList(parsedDate, parsedTime));
			
			return parsedDateAndTime;
		}
}
