package com.viadee.sonarquest.externalressources;

import org.springframework.web.client.RestOperations;

import com.viadee.sonarquest.configurations.ConnectorsConfig;

public class Manager {

	private final RestOperations restTemplate;
	private final String projectKey;
	private final ConnectorsConfig connectorsConfig;


	protected Manager(String projectKey, RestOperations restTemplate, ConnectorsConfig connectorsConfig) {
		this.projectKey = projectKey;
		this.restTemplate = restTemplate;
		this.connectorsConfig = connectorsConfig;
	}

	public RestOperations getRestTemplate() {
		return restTemplate;
	}

	public String getProjectKey() {
		return projectKey;
	}

	public ConnectorsConfig getConnectorsConfig() {
		return connectorsConfig;
	}
}
