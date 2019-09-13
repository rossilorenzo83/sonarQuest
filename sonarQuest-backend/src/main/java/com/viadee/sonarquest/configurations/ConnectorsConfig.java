package com.viadee.sonarquest.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:connectors.properties")
public class ConnectorsConfig {

	@Value("${jira.host}")
	private String jiraHost;

	@Value("${jira.username}")
	private String jiraUsername;

	@Value("${jira.password}")
	private String jiraPassword;

	@Value("${jira.filterId}")
	private Long jiraFilterId;

	public String getJiraHost() {
		return jiraHost;
	}

	public String getJiraUsername() {
		return jiraUsername;
	}

	public String getJiraPassword() {
		return jiraPassword;
	}

	public Long getJiraFilterId() {
		return jiraFilterId;
	}
}
