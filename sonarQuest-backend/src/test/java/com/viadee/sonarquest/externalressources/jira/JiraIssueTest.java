package com.viadee.sonarquest.externalressources.jira;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JiraIssueTest {

	private JiraIssue underTest;

	@Before
	public void before() {
		underTest = new JiraIssue();
	}

	@Test
	public void testGetAgeInDays() {
		IssueFields issueFields = new IssueFields();
		int expected = 5;
		issueFields.setCreated(Instant.now().minus(Duration.ofDays(expected)));
		underTest.setFields(issueFields);
		int actual = underTest.getAgeInDays();
		assertEquals(expected, actual);
	}
}
