package com.viadee.sonarquest.externalressources.jira;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

import com.google.common.base.Objects;

public class IssueFields implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4352437642195203867L;
	private Instant created;

	public Instant getCreated() {
		return created;
	}

	public void setCreated(Instant created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("created", LocalDateTime.from(created)).toString();
	}
}
