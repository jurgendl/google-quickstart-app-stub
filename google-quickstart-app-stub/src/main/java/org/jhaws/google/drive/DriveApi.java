package org.jhaws.google.drive;

import org.jhaws.google.GoogleApi;

import com.google.api.services.drive.Drive;

// https://developers.google.com/drive/api/v3/about-sdk
// https://developers.google.com/drive/api/v3/quickstart/java
public class DriveApi extends GoogleApi<Drive> {
	@Override
	protected Drive createService() {
		return new Drive.Builder(httpTransport, JSON_FACTORY, getCredentials()).setApplicationName(applicationName).build();
	}
}