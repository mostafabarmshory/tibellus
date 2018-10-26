package com.webpich.tibellus.worker;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Artifact {

	@JsonProperty("url")
	private String url;

	@JsonProperty("paths")
	private List<String> paths;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}

}
