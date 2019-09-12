package com.viadee.sonarquest.externalressources;

import org.springframework.web.client.RestOperations;

import com.swissquote.projects.Config;

public class Manager {

	private final RestOperations restTemplate;
	private final Config config;
	private final String projectKey;

	protected Manager(String projectKey, RestOperations restTemplate, Config config) {
		this.projectKey = projectKey;
		this.restTemplate = restTemplate;
		this.config = config;
	}

	public RestOperations getRestTemplate() {
		return restTemplate;
	}

	public Config getConfig() {
		return config;
	}

	public String getProjectKey() {
		return projectKey;
	}
}
