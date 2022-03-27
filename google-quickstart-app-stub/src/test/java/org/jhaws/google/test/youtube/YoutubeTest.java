package org.jhaws.google.test.youtube;

import java.util.Arrays;

import org.jhaws.google.test.CustomObjectMapper;
import org.jhaws.google.test.TestConfig;
import org.jhaws.google.youtube.YoutubeApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(SpringExtension.class)
public class YoutubeTest {
	@Autowired
	YoutubeApi youtubeApi;

	static CustomObjectMapper om = new CustomObjectMapper();

	static String toString(Object o) {
		try {
			return om.writeValueAsString(o);
		} catch (JsonProcessingException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Test
	public void test1() {
		System.out.println(toString(youtubeApi._videos("je_K62Ic6fQ")));
	}

	@Test
	public void test2() {
		System.out.println(toString(youtubeApi._channel("UChfEbvaYvIHmHQWKXA6P_2w")));
	}

	@Test
	public void test3() {
		System.out.println(toString(youtubeApi.videos(Arrays.asList(
				"[oYn8jhBVKAI, hXbDPODP4zo, Nml_adF1v4I, RewQtBSh-CM, NZR_IqTZIu4, QrEXJZUObPw, TTK2Up-4dNg, j9ygv0L_a_E, DCG-oI8UWVU, _Mjhw6q7E80, J5FJRyLqkkw, Ty0z2--atro, csibAMB0U4M, eGdppBDpsZ4, joFSMgR-OEk, OMjTMybsZvo, 0qq9aWfrcxs, IWrKbrB2MKU, FrCgQgahzsI, Ejn3kLYsPAw, rYijUXn3oGo, HFwqcYJmG6g, I4alYbgDi4I, SF_SwujfiYk, 47zRGaF-veQ, qg-H8IMKdmM, HseLrGAXkP4, NDj3vuVwOs8, wLlovxa3VJ0, 9RaePCdW0NE, 70YRmEYurrE, 9SSrtiPsGOA, ZBBPyCSJDEo, 4Sxni2-yvtg, Eb3A-7jJdO8, QtuLN0lNb-Y, DwKPFT-RioU, CdxbV6BObEQ, sVowb1O69AA, D0-QMDYUBVk, XEA8MWGlWFA, 4gru8FgEOus, OFl9IRb9sEE, MiX-u0vgTFg, JbmGrEirPXE, 73ik-tRqqI0, DwKPFT-RioU, fVK9R92BDYs, yIizC3rp3Do, LgIz1A3VEWo]"
						.replace("[", "").replace("]", "").split(", ")))));
	}

	@Test
	public void test4() {
		System.out.println(toString(youtubeApi._comments("UgzDE2tasfmrYLyNkGt4AaABAg")));
	}
}
