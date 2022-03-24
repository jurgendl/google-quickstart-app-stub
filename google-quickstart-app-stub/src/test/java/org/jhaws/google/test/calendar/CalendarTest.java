package org.jhaws.google.test.calendar;

import java.util.List;

import org.jhaws.google.calendar.CalendarApi;
import org.jhaws.google.test.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(SpringExtension.class)
public class CalendarTest {
	@Autowired
	CalendarApi calendarApi;

	@Test
	public void test() {
		try {
			calendarApi.doAction(calendarService -> {
				DateTime now = new DateTime(System.currentTimeMillis());
				Events events = calendarService.events().list("primary").setMaxResults(10).setTimeMin(now).setOrderBy("startTime").setSingleEvents(true).execute();
				List<Event> items = events.getItems();
				if (items.isEmpty()) {
					System.out.println("No upcoming events found.");
				} else {
					System.out.println("Upcoming events");
					for (Event event : items) {
						DateTime start = event.getStart().getDateTime();
						if (start == null) {
							start = event.getStart().getDate();
						}
						System.out.printf("%s (%s)\n", event.getSummary(), start);
					}
				}
				return Void.TYPE;
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
