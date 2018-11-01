package com.webpich.tibellus.worker;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Log {

    @JsonProperty("url")
    private String url;

    @JsonProperty("period")
    private String period;
}
