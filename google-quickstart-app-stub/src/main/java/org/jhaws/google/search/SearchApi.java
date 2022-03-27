package org.jhaws.google.search;

import java.util.Collections;
import java.util.List;

import org.jhaws.google.GoogleApi;

import com.google.api.services.customsearch.v1.CustomSearchAPI;

public class SearchApi extends GoogleApi<CustomSearchAPI> {
	@Override
	protected CustomSearchAPI createService() {
		return new CustomSearchAPI.Builder(httpTransport, JSON_FACTORY, getCredentials())
				.setApplicationName(applicationName).build();
	}

	@Override
	protected List<String> getScope() {
		return Collections.emptyList();
	}
}
