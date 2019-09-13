package com.viadee.sonarquest.externalressources;

import java.io.IOException;
import java.util.Properties;

public class Config {

	private static Config config;

	private static Properties properties;

	public static Config getConfig() {
		if (config == null) {
			config = new Config();
		}
		return config;
	}

	public String getJiraHost(){
		return properties.getProperty("jira.host");
	}

	public String getJiraPort(){
		return properties.getProperty("jira.port");
	}

	public String getJiraUsername(){
		return properties.getProperty("jira.username");
	}


	public String getJiraPwd(){
		return properties.getProperty("jira.pwd");
	}

	public String getJiraFilter(){
		return properties.getProperty("jira.filter");
	}


	private Config() {

		Properties properties = new Properties();

		try {
			properties.load(Config.class.getResourceAsStream("application.properties"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		this.properties = properties;

	}

}
