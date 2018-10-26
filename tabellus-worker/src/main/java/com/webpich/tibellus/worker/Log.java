package com.webpich.tibellus.worker;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Log {

	@JsonProperty("url")
	private String url;

	@JsonProperty("period")
	private String period;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

}
