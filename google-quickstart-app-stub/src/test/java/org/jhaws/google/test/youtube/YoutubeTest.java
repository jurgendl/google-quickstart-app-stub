package org.jhaws.google.test.youtube;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.PlaylistItemContentDetails;
import com.google.api.services.youtube.model.PlaylistItemSnippet;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(SpringExtension.class)
public class YoutubeTest {
	private static final String CHANNEL = "UChfEbvaYvIHmHQWKXA6P_2w";

	private static final String VIDEO = "je_K62Ic6fQ";

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
		System.out.println(toString(youtubeApi.videos(VIDEO)));
	}

	@Test
	public void test2() {
		System.out.println(toString(youtubeApi.channel(CHANNEL)));
	}

	@Test
	public void test3() {
		System.out.println(toString(youtubeApi.getVideos(Arrays.asList(
				"[oYn8jhBVKAI, hXbDPODP4zo, Nml_adF1v4I, RewQtBSh-CM, NZR_IqTZIu4, QrEXJZUObPw, TTK2Up-4dNg, j9ygv0L_a_E, DCG-oI8UWVU, _Mjhw6q7E80, J5FJRyLqkkw, Ty0z2--atro, csibAMB0U4M, eGdppBDpsZ4, joFSMgR-OEk, OMjTMybsZvo, 0qq9aWfrcxs, IWrKbrB2MKU, FrCgQgahzsI, Ejn3kLYsPAw, rYijUXn3oGo, HFwqcYJmG6g, I4alYbgDi4I, SF_SwujfiYk, 47zRGaF-veQ, qg-H8IMKdmM, HseLrGAXkP4, NDj3vuVwOs8, wLlovxa3VJ0, 9RaePCdW0NE, 70YRmEYurrE, 9SSrtiPsGOA, ZBBPyCSJDEo, 4Sxni2-yvtg, Eb3A-7jJdO8, QtuLN0lNb-Y, DwKPFT-RioU, CdxbV6BObEQ, sVowb1O69AA, D0-QMDYUBVk, XEA8MWGlWFA, 4gru8FgEOus, OFl9IRb9sEE, MiX-u0vgTFg, JbmGrEirPXE, 73ik-tRqqI0, DwKPFT-RioU, fVK9R92BDYs, yIizC3rp3Do, LgIz1A3VEWo]"
						.replace("[", "").replace("]", "").split(", ")))));
	}

	@Test
	public void test4() {
		System.out.println(toString(youtubeApi.commentThreads(VIDEO)));
	}

	@Test
	public void test5() {
		for (CommentThread commentThread : youtubeApi.commentThreads(VIDEO)) {
			if (commentThread.getReplies() != null && commentThread.getReplies().size() > 0) {
				System.out.println(toString(youtubeApi.comments(commentThread.getId())));
				break;
			}
		}
	}

	@Test
	public void test6() {
		System.out.println(toString(youtubeApi.search(25, "spiderman")));
	}

	@Test
	public void test7() {
		System.out.println(toString(youtubeApi.mySubscriptions()));
	}

	@Test
	public void test8() {
		List<com.google.api.services.youtube.model.PlaylistItem> list = youtubeApi
				.playlistVideos("PLMNHo1-W9uvKS8j_OJS8NNIQ5RJBHVwrN", 200);
		String INVALID_CHARS_REGEX = "[<>:\"/\\\\|?*]";
		list.forEach(it -> {
			PlaylistItemContentDetails playlistItemContentDetails = (PlaylistItemContentDetails) it
					.get("contentDetails");
			PlaylistItemSnippet playlistItemSnippet = (PlaylistItemSnippet) it.get("snippet");
			String title = playlistItemSnippet.getTitle();
			System.out.println(title);
			title = title.replaceAll(INVALID_CHARS_REGEX, " ");
			System.out.println(title);
			File out = new File("d:/__/" + title + ".url");
			try (FileOutputStream fout = new FileOutputStream(out)) {
				byte[] b = ("[InternetShortcut]\nURL=https://www.youtube.com/watch?v="
						+ playlistItemContentDetails.getVideoId() + "\n").getBytes();
				fout.write(b);
			} catch (IOException exx) {
				throw new RuntimeException(exx);
			}
			System.out.println();
		});
	}

}
