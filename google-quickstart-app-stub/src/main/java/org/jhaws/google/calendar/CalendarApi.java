package org.jhaws.google.calendar;

import org.jhaws.google.GoogleApi;

import com.google.api.services.calendar.Calendar;

// https://developers.google.com/calendar/quickstart/java
// https://console.developers.google.com/apis/api/calendar-json.googleapis.com/overview?project=${project_id}
public class CalendarApi extends GoogleApi<Calendar> {
	@Override
	protected Calendar createService() {
		return new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials()).setApplicationName(applicationName)
				.build();
	}
}
