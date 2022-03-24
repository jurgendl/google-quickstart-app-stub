package org.jhaws.google.test.search;

import org.jhaws.google.search.SearchApi;
import org.jhaws.google.test.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(SpringExtension.class)
public class SearchTest {
	@Autowired
	SearchApi searchApi;

	@Test
	public void test() {
		searchApi.doAction(searchService -> {
			// Customsearch.Cse.List list = searchService.cse().list("google api");
			// Search results = list.execute();
			// List<Result> resultList = results.getItems();
			return Void.TYPE;
		});
	}
}
