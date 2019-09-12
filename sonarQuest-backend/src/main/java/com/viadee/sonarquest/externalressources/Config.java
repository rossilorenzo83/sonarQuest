package com.viadee.sonarquest.externalressources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;

@SuppressWarnings("PMD.GodClass")
public class Config {

	private static final String USER = "user";
	private static final String PASSWORD = "password";
	private static final String HOST = "host";
	private static final String TOKEN = "token";
	private static final String SCHEME = "scheme";
	private static final String PORT = "port";
	private static final String JENKINS = "jenkins";
	private static final String MERCURIAL = "mercurial";
	private static final String GIT = "git";
	private static final String ARTIFACT_SOA = "artifactSoa";
	private final Map<String, Object> shared;
	private final Map<String, Object> jiraConfig;
	private final Map<String, Object> fisheyeConfig;
	private final Map<String, Object> influxDbConfig;
	private final Map<String, Object> reportConfig;
	private final Map<String, Object> artifactSoaConfig;
	private List<Map<String, Object>> projects;

	public Config(Map<String, Object> configMap) {
		this.shared = (Map<String, Object>) configMap.get("shared");
		this.jiraConfig = (Map<String, Object>) shared.get("jira");
		this.fisheyeConfig = (Map<String, Object>) shared.get("fisheye");
		this.influxDbConfig = (Map<String, Object>) shared.get("influxdb");
		this.projects = (List<Map<String, Object>>) configMap.get("projects");
		this.reportConfig = (Map<String, Object>) shared.get("report");
		this.artifactSoaConfig = (Map<String, Object>) shared.get(ARTIFACT_SOA);
	}

	public void setProjects(List<Map<String, Object>> projects) {
		this.projects = projects;
	}

	public String getJiraUser() {
		return (String) jiraConfig.get(USER);
	}

	public String getJiraPassword() {
		return (String) jiraConfig.get(PASSWORD);
	}

	public String getJiraHost() {
		return (String) jiraConfig.get(HOST);
	}

	public Integer getJiraPort() {
		return (Integer) jiraConfig.get(PORT);
	}

	public String getArtifactSoaUser() {
		return (String) artifactSoaConfig.get(USER);
	}

	public String getArtifactSoaPassword() {
		return (String) artifactSoaConfig.get(PASSWORD);
	}

	public String getFisheyeUser() {
		return (String) fisheyeConfig.get(USER);
	}

	public String getFisheyePassword() {
		return (String) fisheyeConfig.get(PASSWORD);
	}

	public String getFisheyeHost() {
		return (String) fisheyeConfig.get(HOST);
	}

	public Integer getFisheyePort() {
		return (Integer) fisheyeConfig.get(PORT);
	}

	public Boolean shouldPersistReport() {
		return (Boolean) reportConfig.get("persist");
	}

	public String getInfluxDbUser() {
		return (String) influxDbConfig.get(USER);
	}

	public String getInfluxDbPassword() {
		return (String) influxDbConfig.get(PASSWORD);
	}

	public String getInfluxDbDatabase() {
		return (String) influxDbConfig.get("database");
	}

	public String getInfluxDbMeasurement() {
		return (String) influxDbConfig.get("measurement");
	}

	public String getInfluxDbHost() {
		return (String) influxDbConfig.get(HOST);
	}

	public Integer getInfluxDbPort() {
		return (Integer) influxDbConfig.get(PORT);
	}

	public List<String> getMercurialRepositories(final String projectKey) {
		return getRepositories(projectKey, MERCURIAL);
	}

	public List<String> getGitRepositories(final String projectKey) {
		return getRepositories(projectKey, GIT);
	}

	public List<String> getRepositories(final String projectKey, String scmType) {
		final Map<String, Object> project = findProjectByKey(projectKey);
		Map<String, Object> config = (Map<String, Object>) project.get(scmType);
		if (config == null) {
			return new ArrayList<>();
		}
		List<String> repositories = (List<String>) config.get("repositories");
		if (repositories == null) {
			return new ArrayList<>();
		} else {
			return repositories;
		}
	}

	public List<String> getMaintainers(final String projectKey) {
		final Map<String, Object> project = findProjectByKey(projectKey);

		List<String> maintainers = (List<String>) project.get("maintainers");
		if (maintainers == null) {
			return new ArrayList<>();
		} else {
			return maintainers;

		}
	}

	public Integer getJiraFilter(final String projectKey) {
		final Map<String, Object> project = findProjectByKey(projectKey);
		return (Integer) ((Map<String, Object>) project.get("jira")).get("filterId");
	}

	public List<String> getFisheyeModules(final String projectKey) {
		final Map<String, Object> project = findProjectByKey(projectKey);
		return (List<String>) ((Map<String, Object>) project.get("fisheye")).get("modules");
	}

	public String getJenkinsScheme(final String projectKey) {
		final Map<String, Object> project = findProjectByKey(projectKey);
		return (String) ((Map<String, Object>) project.get(JENKINS)).get(SCHEME);
	}

	public String getJenkinsHost(final String projectKey) {
		final Map<String, Object> project = findProjectByKey(projectKey);
		return (String) ((Map<String, Object>) project.get(JENKINS)).get(HOST);
	}

	public Integer getJenkinsPort(final String projectKey) {
		final Map<String, Object> project = findProjectByKey(projectKey);
		return (Integer) ((Map<String, Object>) project.get(JENKINS)).get(PORT);
	}

	public String getJenkinsContext(final String projectKey) {
		final Map<String, Object> project = findProjectByKey(projectKey);
		return (String) ((Map<String, Object>) project.get(JENKINS)).get("context");
	}

	public String getJenkinsView(final String projectKey) {
		final Map<String, Object> project = findProjectByKey(projectKey);
		final String view = (String) ((Map<String, Object>) project.get("jenkins")).get("view");
		if (Strings.isNullOrEmpty(view)) {
			return "health-report";
		}
		return view;
	}

	private Map<String, Object> findProjectByKey(final String projectKey) {
		final Collection<Map<String, Object>> filtered = Collections2.filter(projects, new Predicate<Map<String, Object>>() {
			@Override
			public boolean apply(Map<String, Object> input) {
				return input.get("key").equals(projectKey);
			}
		});
		return filtered.iterator().next();
	}

	public String getDepartment(final String projectKey) {
		final Map<String, Object> project = findProjectByKey(projectKey);
		return project.get("department").toString();
	}

	public Boolean shouldDumpDetails() {
		return (Boolean) ((Map) shared.get("log")).get("dumpDetails");
	}

	@Override
	public String toString() {
		return "Config{" + "shared=" + shared + ", jiraConfig=" + jiraConfig + ", fisheyeConfig=" + fisheyeConfig + ", influxDbConfig="
				+ influxDbConfig + ", reportConfig=" + reportConfig + ", artifactSoaConfig=" + artifactSoaConfig + ", projects=" + projects
				+ '}';
	}
}
