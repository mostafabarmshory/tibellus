package com.webpich.tibellus.worker;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Attachment {

	@JsonProperty("url")
	private String url;

	@JsonProperty("archive")
	private boolean archive;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

}
