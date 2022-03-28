package org.jhaws.google;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

// ENABLE API FOR APP: https://console.cloud.google.com/apis/dashboard?project={projectname_hash}
// https://console.cloud.google.com/home/dashboard
// https://console.developers.google.com/apis/dashboard?project={projectname_hash}&authuser=0
// https://developers.google.com/api-client-library
// https://developers.google.com/api-client-library/java/
// https://developers.google.com/api-client-library/java/google-api-java-client/dev-guide
// https://developers.google.com/api-client-library/dotnet/guide/aaa_client_secrets
// https://github.com/googleapis/google-api-java-client-services#supported-google-apis
public abstract class GoogleApi<S> {
	protected static final JsonFactory JSON_FACTORY = com.google.api.client.json.gson.GsonFactory.getDefaultInstance();

	@Value("${google.credentials_file_path}")
	protected String credentialsFilePath;

	@Value("${google.applicationName}")
	protected String applicationName = "Google API Java Quickstart";

	@Value("${google.quickstart_tokens}")
	protected String quickstartTokens;

	@Value("${google.port}")
	protected int port;

	protected NetHttpTransport httpTransport = null;

	protected S service;

	@Autowired(required = false)
	protected List<GoogleApi<?>> apis = Collections.emptyList();

	public GoogleApi() {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		} catch (GeneralSecurityException ex) {
			throw new RuntimeException(ex);
		}
	}

	protected abstract List<String> getScope();

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	protected Credential getCredentials() {
		try {
			Set<String> scopes = new HashSet<>();
			scopes.addAll(getScope());
			apis.stream().map(GoogleApi::getScope).forEach(scopes::addAll);
			// Load client secrets.
			String userHome = System.getProperty("user.home");
			Path credentialsPath = Paths.get(userHome).resolve(credentialsFilePath);
			InputStream in = Files.newInputStream(credentialsPath);
			if (in == null)
				throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
			// Build flow and trigger user authorization request.
			Path quickstartTokensPath = Paths.get(userHome).resolve(quickstartTokens);
			FileDataStoreFactory dataStore = new FileDataStoreFactory(quickstartTokensPath.toFile());
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
					clientSecrets, scopes).setDataStoreFactory(dataStore).setAccessType("offline").build();
			LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(port).build();
			return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public void setCredentialsFilePath(String credentialsFilePath) {
		this.credentialsFilePath = credentialsFilePath;
	}

	public void setQuickstartTokens(String quickstartTokens) {
		this.quickstartTokens = quickstartTokens;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getCredentialsFilePath() {
		return credentialsFilePath;
	}

	public String getQuickstartTokens() {
		return quickstartTokens;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	protected abstract S createService();

	protected synchronized S getService() {
		return Optional.ofNullable(service).orElse(service = createService());
	}

	public <R> R doAction(Action<S, R> action) {
		S localService = getService();
		try {
			return action.doAction(localService);
		} catch (GoogleJsonResponseException ex1) {
			if (ex1.getStatusCode() == 403) {
				try {
					try {
						Files.delete(Paths.get(System.getProperty("user.home")).resolve(quickstartTokens)
								.resolve("StoredCredential"));
						this.service = null;
						getService();
					} catch (IOException ioex) {
						throw new UncheckedIOException(ioex);
					}
					return action.doAction(localService);
				} catch (GoogleJsonResponseException ex2) {
					throw new UncheckedIOException(ex2);
				} catch (IOException ex2) {
					throw new UncheckedIOException(ex2);
				}
			}
			throw new UncheckedIOException(ex1);
		} catch (TokenResponseException ex1) {
			// if (ex1.getStatusCode() == 400) {
			// try {
			// try {
			// Files.delete(Paths.get(System.getProperty("user.home")).resolve(quickstartTokens).resolve("StoredCredential"));
			// this.service = null;
			// getService();
			// } catch (IOException ioex) {
			// throw new UncheckedIOException(ioex);
			// }
			// return action.doAction(localService);
			// } catch (GoogleJsonResponseException ex2) {
			// throw new UncheckedIOException(ex2);
			// } catch (IOException ex2) {
			// throw new UncheckedIOException(ex2);
			// }
			// }
			throw new UncheckedIOException(ex1);
		} catch (IOException ex1) {
			throw new UncheckedIOException(ex1);
		} catch (RuntimeException ex1) {
			throw ex1;
		}
	}

	@FunctionalInterface
	public static interface Action<S, R> {
		R doAction(S service) throws GoogleJsonResponseException, IOException;
	}
}
