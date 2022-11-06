package org.jhaws.google.test.translate;

import org.jhaws.google.test.TestConfig;
import org.jhaws.google.translate.TranslateApi;
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
public class TranslateTest {
	@Autowired
	TranslateApi translateApi;

	@Test
	public void test() {
		try {
			translateApi.translateText("en", "voorbeeld");
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex);
		}
	}
}
