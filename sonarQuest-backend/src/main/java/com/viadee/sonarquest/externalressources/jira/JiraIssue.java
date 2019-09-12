package com.viadee.sonarquest.externalressources.jira;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class JiraIssue implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4321971535905680142L;
	private IssueFields fields;
	private String key;

	public IssueFields getFields() {
		return fields;
	}

	public void setFields(IssueFields fields) {
		this.fields = fields;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getAgeInDays() {
		Instant issueCreationDate = getFields().getCreated();
		return Period.between(LocalDateTime.from(issueCreationDate).toLocalDate(), LocalDate.now()).getDays();
	}
}
