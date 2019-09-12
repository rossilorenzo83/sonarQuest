package com.viadee.sonarquest.externalressources.jira;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Objects;

public class SearchIssuesResult implements Serializable {

	private static final long serialVersionUID = -3084958648898295793L;
	private List<JiraIssue> issues;
	private int total;
	private int startAt;
	private int maxResults;

	public List<JiraIssue> getIssues() {
		return issues;
	}

	public void setIssues(List<JiraIssue> issues) {
		this.issues = issues;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SearchIssuesResult that = (SearchIssuesResult) o;

		if (maxResults != that.maxResults) {
			return false;
		}
		if (startAt != that.startAt) {
			return false;
		}
		if (total != that.total) {
			return false;
		}
		return !(issues != null ? !issues.equals(that.issues) : that.issues != null);

	}

	@Override
	public int hashCode() {
		int result = issues != null ? issues.hashCode() : 0;
		result = 31 * result + total;
		result = 31 * result + startAt;
		result = 31 * result + maxResults;
		return result;

	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("issues", issues).add("total", total).add("startAt", startAt).add("maxResults", maxResults)
				.toString();
	}
}
