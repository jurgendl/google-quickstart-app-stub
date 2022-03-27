package org.jhaws.google.youtube;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jhaws.google.GoogleApi;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.google.api.services.youtube.model.VideoListResponse;

// https://developers.google.com/youtube
// https://developers.google.com/youtube/v3/docs/videos
// https://developers.google.com/youtube/v3/docs/channels
// https://developers.google.com/youtube/v3/code_samples/code_snippets
public class YoutubeApi extends GoogleApi<YouTube> {
	@Override
	protected YouTube createService() {
		return new YouTube.Builder(httpTransport, JSON_FACTORY, getCredentials()).setApplicationName(applicationName)
				.build();
	}

	public List<Video> getVideos(List<String> ids) {
		if (ids.size() > 50) {
			throw new IllegalArgumentException();
		}
		return doAction(localService -> {
			YouTube.Videos.List request = localService.videos()
					.list(Arrays.asList("snippet", "contentDetails", "statistics"));
			VideoListResponse response = request.setId(ids).execute();
			List<Video> list = response.getItems().stream().map(v -> {
				Video video = new Video();
				video.setId(v.getId());
				video.setDuration(v.getContentDetails().getDuration());
				video.setChannelId(v.getSnippet().getChannelId());
				video.setChannelTitle(v.getSnippet().getChannelTitle());
				video.setDescription(v.getSnippet().getDescription());
				video.setTitle(v.getSnippet().getTitle());
				video.setThumb(v.getSnippet().getThumbnails().getHigh().getUrl());
				return video;
			}).collect(Collectors.toList());
			return list;
		});
	}

	public List<com.google.api.services.youtube.model.Video> videos(String... ids) {
		if (ids.length > 50) {
			throw new IllegalArgumentException();
		}
		return doAction(localService -> {
			YouTube.Videos.List request = localService.videos()
					.list(Arrays.asList("id", "snippet", "contentDetails", "statistics", "status"));
			VideoListResponse response = request.setId(Arrays.asList(ids)).execute();
			return response.getItems();
		});
	}

	public List<com.google.api.services.youtube.model.Channel> channel(String... ids) {
		return doAction(localService -> {
			YouTube.Channels.List request = localService.channels()
					.list(Arrays.asList("id", "snippet", "contentDetails", "statistics", "status"));
			ChannelListResponse response = request.setId(Arrays.asList(ids)).execute();
			return response.getItems();
		});
	}

	public List<com.google.api.services.youtube.model.CommentThread> commentThreads(String id) {
		return doAction(localService -> {
			YouTube.CommentThreads.List request = localService.commentThreads()
					.list(Arrays.asList("id", "snippet", "replies"));
			CommentThreadListResponse response = request.setVideoId(id).execute();
			return response.getItems();
		});
	}

	public List<com.google.api.services.youtube.model.Comment> comments(String parentId) {
		return doAction(localService -> {
			YouTube.Comments.List request = localService.comments().list(Arrays.asList("id", "snippet"));
			CommentListResponse response = request.setParentId(parentId).execute();
			return response.getItems();
		});
	}

	public List<com.google.api.services.youtube.model.SearchResult> search(int max, String search) {
		return doAction(localService -> {
			YouTube.Search.List request = localService.search().list(Arrays.asList("id", "snippet"));
			SearchListResponse response = request.setMaxResults((long) max).setQ(search).execute();
			return response.getItems();
		});
	}

	public List<com.google.api.services.youtube.model.Subscription> mySubscriptions() {
		return doAction(localService -> {
			YouTube.Subscriptions.List request = localService.subscriptions()
					.list(Arrays.asList("id", "snippet", "contentDetails"));
			SubscriptionListResponse response = request.setMine(true).execute();
			return response.getItems();
		});
	}

	@Override
	protected List<String> getScope() {
		return Arrays.asList(YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE, YouTubeScopes.YOUTUBE_FORCE_SSL);
	}
}