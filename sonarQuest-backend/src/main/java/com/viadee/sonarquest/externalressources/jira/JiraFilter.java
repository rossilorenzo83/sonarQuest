package com.viadee.sonarquest.externalressources.jira;

import java.io.Serializable;

import com.google.common.base.Objects;

public class JiraFilter implements Serializable {

	private static final long serialVersionUID = -543683532351609415L;
	private String id;
	private String searchUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		JiraFilter that = (JiraFilter) o;

		if (id != null ? !id.equals(that.id) : that.id != null) {
			return false;
		}
		return !(searchUrl != null ? !searchUrl.equals(that.searchUrl) : that.searchUrl != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (searchUrl != null ? searchUrl.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).add("searchUrl", searchUrl).toString();
	}
}
