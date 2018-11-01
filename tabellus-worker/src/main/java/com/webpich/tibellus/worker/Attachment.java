package com.webpich.tibellus.worker;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Attachment {

    @JsonProperty("url")
    private String url;

    @JsonProperty("name")
    private String name;

    @JsonProperty("content")
    private String content;

}
