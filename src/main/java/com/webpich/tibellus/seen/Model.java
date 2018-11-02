package com.webpich.tibellus.seen;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Model {

	@JsonProperty
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
