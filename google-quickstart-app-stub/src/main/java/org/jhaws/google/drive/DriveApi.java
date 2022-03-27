package org.jhaws.google.drive;

import java.util.Arrays;
import java.util.List;

import org.jhaws.google.GoogleApi;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

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

	@Override
	protected List<String> getScope() {
		return Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE_READONLY);
	}
}