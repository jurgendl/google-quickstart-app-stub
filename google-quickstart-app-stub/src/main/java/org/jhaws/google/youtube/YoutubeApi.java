package org.jhaws.google.youtube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jhaws.google.GoogleApi;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
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

	public List<com.google.api.services.youtube.model.PlaylistItem> playlistVideos(int max) {
		return playlistVideos(true, null, max);
	}

	public List<com.google.api.services.youtube.model.PlaylistItem> playlistVideos(String playlistId, int max) {
		return playlistVideos(false, playlistId, max);
	}

	private List<com.google.api.services.youtube.model.PlaylistItem> playlistVideos(boolean mine, String playlistId,
			int max) {
		if (max < 1 || max > 1000)
			throw new IllegalArgumentException("max<1||max>1000");
		final int fixedMax = 50;
		int page = 0;
		int pages = 1;
		final int maxPages = max / fixedMax + (max % fixedMax > 0 ? 1 : 0);
		String nextPageToken = null;
		Integer total = null;
		List<com.google.api.services.youtube.model.PlaylistItem> list = new ArrayList<>();
		do {
			final String _nextPageToken = nextPageToken;
			PlaylistItemListResponse response = doAction(localService -> {
				com.google.api.services.youtube.YouTube.PlaylistItems pli = localService.playlistItems();
				com.google.api.services.youtube.YouTube.PlaylistItems.List request = pli
						.list(Arrays.asList("id", "snippet", "contentDetails"));
				request = request.setMaxResults((long) fixedMax);
				if (playlistId != null)
					request = request.setPlaylistId(playlistId);
				// if(mine)
				// request=request.setOnBehalfOfContentOwner();
				if (_nextPageToken != null)
					request.setPageToken(_nextPageToken);
				PlaylistItemListResponse r = request.execute();
				return r;
			});
			nextPageToken = response.getNextPageToken();
			response.getItems().stream().filter(x -> !list.contains(x)).forEach(list::add);
			if (total == null) {
				total = response.getPageInfo().getTotalResults();
				pages = response.getPageInfo().getTotalResults() / fixedMax;
				if (response.getPageInfo().getTotalResults() % fixedMax > 0)
					pages++;
			}
			page++;
		} while (nextPageToken != null && page < (maxPages + 1));
		System.out.println(
				"page=" + page + ", " + "pages=" + pages + ", " + "total=" + total + ", " + "size=" + list.size());
		return list;
	}
}