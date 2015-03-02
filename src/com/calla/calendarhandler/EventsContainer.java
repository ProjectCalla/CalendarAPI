package com.calla.calendarhandler;

import java.util.ArrayList;

public class EventsContainer {

	private ArrayList<CalendarEvent> eventList = new ArrayList<CalendarEvent>();
	
	public ArrayList<CalendarEvent> getEventList(){
		return eventList;
	}
	
	public void setEventList(CalendarEvent updatedEventList){
		eventList.add(updatedEventList);
	}
	
}
