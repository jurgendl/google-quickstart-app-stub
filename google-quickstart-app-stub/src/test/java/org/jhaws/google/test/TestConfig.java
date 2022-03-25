package org.jhaws.google.test;

import org.jhaws.common.web.spring.AdaptingPropertySourceFactory;
import org.jhaws.google.calendar.CalendarApi;
import org.jhaws.google.drive.DriveApi;
import org.jhaws.google.gmail.GmailApi;
import org.jhaws.google.search.SearchApi;
import org.jhaws.google.youtube.YoutubeApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

// https://www.baeldung.com/spring-5-junit-config
// https://www.baeldung.com/junit-5-migration
// https://stackoverflow.com/questions/26698071/how-to-exclude-autoconfiguration-classes-in-spring-boot-junit-tests
// https://www.baeldung.com/spring-profiles
// https://www.baeldung.com/junit-5-runwith
@Profile("test")
@Configuration
@ComponentScan(//
		basePackages = { //
				"test.google"//
		}//
		, excludeFilters = { //
				@Filter(type = FilterType.ASSIGNABLE_TYPE, //
						value = { //
									//
						}) })
@PropertySource(value = { //
		"file:${user.home}/google-app/google-quickstart.yml" //
}, ignoreResourceNotFound = false, factory = AdaptingPropertySourceFactory.class)
public class TestConfig {
	@Bean
	public DriveApi driveApi() {
		return new DriveApi();
	}

	@Bean
	public YoutubeApi youtubeApi() {
		return new YoutubeApi();
	}

	@Bean
	public CalendarApi calendarApi() {
		return new CalendarApi();
	}

	@Bean
	public SearchApi searchApi() {
		return new SearchApi();
	}

	@Bean
	public GmailApi gmailApi() {
		return new GmailApi();
	}
}
