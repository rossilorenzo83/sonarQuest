package com.viadee.sonarquest.externalressources.jira;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.hibernate.validator.constraints.ModCheck;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.collect.Lists;
import com.viadee.sonarquest.configurations.ConnectorsConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@TestPropertySource(locations = "classpath:connectors.properties")
public class IssueManagerTest {

	@Mock
	private RestOperations restOperations;

	@Mock
	private ConnectorsConfig connectorsConfig;

	@Value("${jira.host}")
	private String jiraHost;

	@Value("${jira.filterId}")
	private Long filterId;

	private IssueManager underTest;

	private String projectKey = "ANY";

	@Before
	public void before() {
		underTest = new IssueManager(projectKey, restOperations, connectorsConfig);
	}

	@Test
	public void buildOldestBugFilterUriShouldSucceed() {
		String server = "any-server";
		Integer filterId = 21232;
		String expected = "https://" + server + "/rest/api/2/filter/" + filterId;

		when(connectorsConfig.getJiraHost()).thenReturn(server);
		when(connectorsConfig.getJiraFilterId()).thenReturn(Long.valueOf(filterId));

		URI actual = underTest.buildOldestBugFilterUri();
		assertEquals(expected, actual.toString());
	}

	@Test
	public void getOldestBugFilterShouldSucceed() {
		String server = "any-server";
		String user = "user";
		String password = "secret";
		ResponseEntity<JiraFilter> exchange = mock(ResponseEntity.class);
		URI uri =
				UriComponentsBuilder.newInstance().scheme("https").host(server).path("/rest/api/2/filter").path("/" + filterId)
				.build().toUri();
		when(restOperations.exchange(eq(uri.toString()), eq(HttpMethod.GET), any(HttpEntity.class), eq(JiraFilter.class))).thenReturn(exchange);
		JiraFilter filter = mock(JiraFilter.class);
		when(exchange.getBody()).thenReturn(filter);
		when(connectorsConfig.getJiraHost()).thenReturn(server);
		when(connectorsConfig.getJiraUsername()).thenReturn(user);
		when(connectorsConfig.getJiraPassword()).thenReturn(password);
		when(connectorsConfig.getJiraFilterId()).thenReturn(filterId);
		final JiraFilter actual = underTest.getOldestBugFilter();
		assertSame(filter, actual);
	}

	@Test
	public void findOldestIssueWithFilterShouldSucceed() {
		String user = "user";
		String password = "secret";
		ResponseEntity<SearchIssuesResult> exchange = mock(ResponseEntity.class);
		JiraFilter filter = new JiraFilter();
		filter.setSearchUrl("http://search.com");
		URI uri = UriComponentsBuilder.fromUriString(filter.getSearchUrl()).queryParam("maxResults", 1).build().toUri();
		when(restOperations.exchange(eq(uri), eq(HttpMethod.GET), any(HttpEntity.class), eq(SearchIssuesResult.class))).thenReturn(exchange);
		SearchIssuesResult results = new SearchIssuesResult();
		JiraIssue a = new JiraIssue();
		JiraIssue b = new JiraIssue();
		// The oldest issue query will always pick the first element
		List<JiraIssue> issues = Lists.newArrayList(b, a);
		results.setIssues(issues);
		when(exchange.getBody()).thenReturn(results);
		JiraIssue actual = underTest.findOldestIssue(filter);
		assertEquals(b, actual);
	}

	@Test
	public void buildSearchUriShouldReplaceAllPlusIfEncoded() {
		String input = "https://jira.bank.swissquote.ch/rest/api/2/search"
				+ "?jql=project+in+(BOSR)+AND+type+in+(Bug)+AND+status+not+in+(closed,+Abandoned,+%22In+Production%22)";
		String expected = "https://jira.bank.swissquote.ch/rest/api/2/search" + "?jql=project%20in%20(BOSR)%20AND%20type%20in%20(Bug)%20AND%20"
				+ "status%20not%20in%20(closed,%20Abandoned,%20%22In%20Production%22)&maxResults=1";
		URI actual = underTest.buildSearchUri(input, true);
		assertEquals(expected, actual.toString());
	}

	@Test
	public void buildSearchUriShouldReplaceAllSpacesIfNotEncoded() {
		String input = "https://jira.bank.swissquote.ch/rest/api/2/search"
				+ "?jql=project in (BOSR) AND type in (Bug) AND status not in (closed, Abandoned, \"In Production\")";
		String expected = "https://jira.bank.swissquote.ch/rest/api/2/search" + "?jql=project%20in%20(BOSR)%20AND%20type%20in%20(Bug)%20AND%20"
				+ "status%20not%20in%20(closed,%20Abandoned,%20%22In%20Production%22)&maxResults=1";
		URI actual = underTest.buildSearchUri(input, false);
		assertEquals(expected, actual.toString());
	}

	@Test
	public void findOldestIssueWithFilterShouldReturnNull() {
		String user = "user";
		String password = "secret";
		ResponseEntity<SearchIssuesResult> exchange = mock(ResponseEntity.class);
		JiraFilter filter = new JiraFilter();
		filter.setSearchUrl("http://search.com?x=y");
		URI uri = UriComponentsBuilder.fromUriString(filter.getSearchUrl()).queryParam("maxResults", 1).build().toUri();
		when(restOperations.exchange(eq(uri), eq(HttpMethod.GET), any(HttpEntity.class), eq(SearchIssuesResult.class))).thenReturn(exchange);
		SearchIssuesResult results = new SearchIssuesResult();
		when(exchange.getBody()).thenReturn(results);
		JiraIssue actual = underTest.findOldestIssue(filter);
		assertNull(actual);
	}

}
