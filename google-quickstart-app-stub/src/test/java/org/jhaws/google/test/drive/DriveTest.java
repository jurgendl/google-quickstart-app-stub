package org.jhaws.google.test.drive;

import java.util.List;

import org.jhaws.google.drive.DriveApi;
import org.jhaws.google.test.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(SpringExtension.class)
public class DriveTest {
	@Autowired
	DriveApi driveApi;

	@Test
	public void test() {
		driveApi.doAction(driveService -> {
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
			}
			return Void.TYPE;
		});

	}
}
