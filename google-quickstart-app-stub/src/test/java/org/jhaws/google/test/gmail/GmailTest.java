package org.jhaws.google.test.gmail;

import java.io.ByteArrayInputStream;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jhaws.google.gmail.GmailApi;
import org.jhaws.google.test.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Profile;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(SpringExtension.class)
public class GmailTest {
	@Autowired
	GmailApi gmailApi;

	@Test
	public void test() {
		try {
			String user = "me";
			gmailApi.doAction(gmailService -> {
				Profile profile = gmailService.users().getProfile(user).execute();
				System.out.println(profile);
				ListMessagesResponse mails = gmailService.users().messages().list(user).setMaxResults(10l).execute();
				System.out.println(mails);
				Message mail = gmailService.users().messages().get(user, mails.getMessages().get(0).getId())
						.setFormat("raw").execute();
				System.out.println(mail);
				try {
					MimeMessage mm = new MimeMessage(null, new ByteArrayInputStream(mail.decodeRaw()));
					MimeMultipart mmp = (MimeMultipart) mm.getContent();
					for (int i = 0; i < mmp.getCount(); i++) {
						System.out.print(mmp.getBodyPart(i).getContent());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return null;
			});
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			throw ex;
		}
	}
}
