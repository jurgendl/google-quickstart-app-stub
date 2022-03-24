package org.jhaws.google.youtube;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jhaws.google.GoogleApi;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.VideoListResponse;

// https://developers.google.com/youtube
// https://developers.google.com/youtube/v3/docs/videos
// https://developers.google.com/youtube/v3/docs/channels
public class YoutubeApi extends GoogleApi<YouTube> {
	@Override
	protected YouTube createService() {
		return new YouTube.Builder(httpTransport, JSON_FACTORY, getCredentials()).setApplicationName(applicationName).build();
	}

	public List<Video> videos(List<String> ids) {
		if (ids.size() > 50) {
			throw new IllegalArgumentException();
		}
		return doAction(localService -> {
			YouTube.Videos.List request = localService.videos().list(Arrays.asList("snippet", "contentDetails", "statistics"));
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

	public List<com.google.api.services.youtube.model.Video> videosInternal(List<String> ids) {
		if (ids.size() > 50) {
			throw new IllegalArgumentException();
		}
		return doAction(localService -> {
			YouTube.Videos.List request = localService.videos().list(Arrays.asList("id", "snippet", "contentDetails", "statistics", "status"));
			VideoListResponse response = request.setId(ids).execute();
			return response.getItems();
		});
	}

	public List<com.google.api.services.youtube.model.Channel> channelInternal(String id) {
		return doAction(localService -> {
			YouTube.Channels.List request = localService.channels().list(Arrays.asList("id", "snippet", "contentDetails", "statistics", "status"));
			ChannelListResponse response = request.setId(Arrays.asList(id)).execute();
			return response.getItems();
		});
	}
}