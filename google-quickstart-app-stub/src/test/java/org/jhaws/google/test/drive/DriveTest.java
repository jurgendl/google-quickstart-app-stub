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

	class CustomProgressListener implements MediaHttpDownloaderProgressListener {
		public void progressChanged(MediaHttpDownloader downloader) {
			switch (downloader.getDownloadState()) {
			case MEDIA_IN_PROGRESS:
				System.out.println(downloader.getProgress());
				break;
			case MEDIA_COMPLETE:
				System.out.println("Download is complete!");
			default:
				break;
			}
		}
	}

	@Test
	public void test() {
		driveApi.doAction(driveService -> {
			FileList result;
			try {
				result = driveService.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)")
						.execute();
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

				{
					Path path = Paths.get(System.getProperty("java.io.tmpdir"), files.get(0).getName());
					System.out.println(path);
					OutputStream out = Files.newOutputStream(path);
					Get request = driveService.files().get(files.get(0).getId());
					request.getMediaHttpDownloader().setProgressListener(new CustomProgressListener());
					request.executeMediaAndDownloadTo(out);
				}
			}
			return Void.TYPE;
		});

	}
}
