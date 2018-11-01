package com.webpich.tibellus.worker;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WJC job context
 * 
 * @author maso
 *
 */
public class JobContext {

    @JsonProperty("image")
    private String image;

    @JsonProperty("timeout")
    private String timeout;

    @JsonProperty("log")
    private Log log;

    @JsonProperty("variables")
    private Map<String, String> variables;

    @JsonProperty("script")
    private List<String> scripts;

    @JsonProperty("artifacts")
    private List<Artifact> artifacts;

    @JsonProperty("attachments")
    private List<Attachment> attachments;

    /**
     * Build a new job
     * 
     * @author maso
     */
    public static class Builder {

	/**
	 * Build a new job context
	 * 
	 * @return
	 */
	public JobContext build() {
	    JobContext jobContext = null;
	    // XXX: maso, 2018: fill the job with configurations
	    return jobContext;
	}
    }

}
