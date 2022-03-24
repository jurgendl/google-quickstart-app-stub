package org.jhaws.google.youtube;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Video implements Serializable {
	private String id;
	private String title;
	// PT2H5M17S
	private String duration;
	// https://i.ytimg.com/vi/{ID}/hqdefault.jpg
	private String thumb;
	// contains newlines
	private String description;
	private String channelId;
	private String channelTitle;

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Video [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", duration=");
		builder.append(duration);
		builder.append(", thumb=");
		builder.append(thumb);
		builder.append(", channelId=");
		builder.append(channelId);
		builder.append(", channelTitle=");
		builder.append(channelTitle);
		builder.append("]");
		return builder.toString();
	}
}
