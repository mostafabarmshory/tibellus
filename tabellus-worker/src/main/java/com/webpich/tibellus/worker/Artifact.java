package com.webpich.tibellus.worker;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Artifact {

    @JsonProperty("url")
    private String url;

    @JsonProperty("paths")
    private List<String> paths;

}
