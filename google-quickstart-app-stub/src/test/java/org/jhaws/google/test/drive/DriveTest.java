package org.jhaws.google.test.drive;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jhaws.google.drive.DriveApi;
import org.jhaws.google.test.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(SpringExtension.class)
public class DriveTest {
	@Autowired
	DriveApi driveApi;

	// @Test
	public void testCleanUp() {
		driveApi.cleanUpTokens();
	}

	class CustomProgressListener implements MediaHttpDownloaderProgressListener {
		@Override
		public void progressChanged(MediaHttpDownloader downloader) {
			switch (downloader.getDownloadState()) {
				case MEDIA_IN_PROGRESS:
					System.out.println(downloader.getProgress());
					break;
				case MEDIA_COMPLETE:
					System.out.println("Download is complete!");
					break;
				case NOT_STARTED:
					System.out.println("not-started");
					break;
				default:
					break;
			}
		}
	}

	@Test
	public void test() {
		driveApi.doAction(driveService -> {
			if (true) {
				FileList result;
				try {
					result = driveService.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
				List<File> files = result.getFiles();
				if (files == null || files.isEmpty()) {
					System.out.println("No files found.");
				} else {
					System.out.println("Files:");
					for (File file : files) {
						System.out.printf("%s (%s)\n", file.getName(), file.getId());
					}

					if (false) {
						Path path = Paths.get(System.getProperty("java.io.tmpdir"), files.get(0).getName());
						System.out.println(path);
						OutputStream out = Files.newOutputStream(path);
						Get request = driveService.files().get(files.get(0).getId());
						request.getMediaHttpDownloader().setProgressListener(new CustomProgressListener());
						// request.getMediaHttpDownloader().setDirectDownloadEnabled(true);
						request.executeMediaAndDownloadTo(out);
						// request.executeMediaAsInputStream();
					}
				}
			}
			if (true) {
				int pages = 0;
				String pageToken = null;
				do {
					FileList result = driveService.files().list().setQ("mimeType='image/jpeg'").setSpaces("drive").setFields("nextPageToken, files(id, name)").setPageToken(pageToken).execute();
					for (File file : result.getFiles()) {
						System.out.printf("Found file: %s - %s - %s - %s\n", file.getOriginalFilename(), file.getName(), "" + file.getSize(), file.getId());
					}
					pageToken = result.getNextPageToken();
					pages++;
				} while (pageToken != null && pages < 5);

			}
			return Void.TYPE;
		});

	}
}
