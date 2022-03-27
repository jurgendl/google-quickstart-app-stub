package org.jhaws.google.youtube;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jhaws.google.GoogleApi;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.CommentListResponse;
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

	public List<Video> videos(List<String> ids) {
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

	public List<com.google.api.services.youtube.model.Video> _videos(String... ids) {
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

	public List<com.google.api.services.youtube.model.Channel> _channel(String... ids) {
		return doAction(localService -> {
			YouTube.Channels.List request = localService.channels()
					.list(Arrays.asList("id", "snippet", "contentDetails", "statistics", "status"));
			ChannelListResponse response = request.setId(Arrays.asList(ids)).execute();
			return response.getItems();
		});
	}

	public List<com.google.api.services.youtube.model.Comment> _comments(String parentId) {
		return doAction(localService -> {
			YouTube.Comments.List request = localService.comments().list(Arrays.asList("id", "snippet"))/* .list("") */;
			CommentListResponse response = request.setParentId(parentId)
					// .setId(Arrays.asList(ids))
					.execute();
			return response.getItems();
		});
	}

	@Override
	protected List<String> getScope() {
		return Arrays.asList(YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE, YouTubeScopes.YOUTUBE_FORCE_SSL);
	}
}