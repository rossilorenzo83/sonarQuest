package com.viadee.sonarquest.externalressources.jira;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.viadee.sonarquest.externalressources.Config;
import com.viadee.sonarquest.externalressources.BasicAuthUtils;
import com.viadee.sonarquest.externalressources.Manager;

public class IssueManager extends Manager {

	public IssueManager(String projectKey, RestOperations restTemplate, Config config) {
		super(projectKey, restTemplate, config);
	}

	public JiraIssue findOldestIssue(JiraFilter filter) {
		HttpHeaders headers = BasicAuthUtils.headers(getConfig().getJiraUser(), getConfig().getJiraPassword());
		HttpEntity request = new HttpEntity(headers);
		URI uri = buildSearchUri(filter.getSearchUrl(), true);
		SearchIssuesResult result =
				(SearchIssuesResult) getRestTemplate().exchange(uri, HttpMethod.GET, request, SearchIssuesResult.class).getBody();
		List<JiraIssue> issues = result.getIssues();
		if (issues == null || issues.isEmpty()) {
			return null;
		}
		return issues.iterator().next();
	}

	public JiraFilter getOldestBugFilter() {
		HttpHeaders headers = BasicAuthUtils.headers(getConfig().getJiraUser(), getConfig().getJiraPassword());
		HttpEntity request = new HttpEntity(headers);
		return (JiraFilter) getRestTemplate().exchange(buildOldestBugFilterUri().toString(), HttpMethod.GET, request, JiraFilter.class)
				.getBody();
	}

	URI buildOldestBugFilterUri() {
		return UriComponentsBuilder.newInstance().scheme("https").host(getConfig().getJiraHost()).path("/rest/api/2/filter")
				.path("/" + getConfig().getJiraFilter(getProjectKey())).build().toUri();
	}

	/**
	 * This method is not supposed to exist
	 * Unfortunately UriComponentBuilder does not support the '+' character as a valid encoding for ' ' a http url
	 * See https://jira.spring.io/browse/SPR-10172
	 * Fix:
	 * - if (encoded) trust the input except for '+' chars: replace all '+' chars by the corresponding standard %20 encoding
	 * - else let UriComponentBuilder do its job
	 */
	URI buildSearchUri(String url, boolean encoded) {
		String validUrl = url;
		if (encoded) {
			validUrl = url.replaceAll("\\+", "%20");
		}
		return UriComponentsBuilder.fromUriString(validUrl).queryParam("maxResults", 1).build(encoded).toUri();
	}
}
