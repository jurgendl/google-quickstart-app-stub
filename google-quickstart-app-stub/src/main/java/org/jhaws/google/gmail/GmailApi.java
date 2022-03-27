package org.jhaws.google.gmail;

import java.util.Arrays;
import java.util.List;

import org.jhaws.google.GoogleApi;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

// https://www.tabnine.com/code/java/classes/com.google.api.services.gmail.Gmail
// https://github.com/google/mail-importer/blob/master/src/main/java/to/lean/tools/gmail/importer/gmail/Mailbox.java
// https://github.com/googleworkspace/java-samples/blob/master/gmail/quickstart/src/main/java/GmailQuickstart.java
//https://github.com/googleworkspace/java-samples/blob/master/gmail/snippets/src/main/java/SendEmail.java
public class GmailApi extends GoogleApi<Gmail> {
	@Override
	protected Gmail createService() {
		return new Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials()).setApplicationName(applicationName)
				.build();
	}

	@Override
	protected List<String> getScope() {
		return Arrays.asList(GmailScopes.GMAIL_READONLY);
	}
}