package org.jhaws.google.calendar;

import java.util.Arrays;
import java.util.List;

import org.jhaws.google.GoogleApi;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

// https://developers.google.com/calendar/quickstart/java
// https://console.developers.google.com/apis/api/calendar-json.googleapis.com/overview?project=${project_id}
public class CalendarApi extends GoogleApi<Calendar> {
	@Override
	protected Calendar createService() {
		return new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials()).setApplicationName(applicationName)
				.build();
	}

	@Override
	protected List<String> getScope() {
		return Arrays.asList(CalendarScopes.CALENDAR);
	}
}
