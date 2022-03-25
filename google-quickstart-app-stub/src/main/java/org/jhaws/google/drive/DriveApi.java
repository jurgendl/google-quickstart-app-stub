package org.jhaws.google.drive;

import org.jhaws.google.GoogleApi;

import com.google.api.services.drive.Drive;

// https://developers.google.com/drive/api/v3/about-sdk
// https://developers.google.com/drive/api/v3/quickstart/java
// https://developers.google.com/api-client-library/java/google-api-java-client/media-download
//https://developers.google.com/drive/api/guides/search-files
//https://developers.google.com/drive/api/guides/manage-sharing
//https://developers.google.com/drive/api/guides/handle-errors
//https://developers.google.com/drive/api/guides/manage-uploads
//https://developers.google.com/drive/api/guides/manage-downloads
public class DriveApi extends GoogleApi<Drive> {
	@Override
	protected Drive createService() {
		return new Drive.Builder(httpTransport, JSON_FACTORY, getCredentials()).setApplicationName(applicationName)
				.build();
	}
}