package com.viadee.sonarquest.externalressources;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.google.common.collect.Lists;

public class BasicAuthUtilsTest {

	@Test
	public void headersShouldSucceed() {
		final HttpHeaders actual = BasicAuthUtils.headers("user", "secret");
		final HttpHeaders expected = new HttpHeaders();
		expected.add("Authorization", "Basic dXNlcjpzZWNyZXQ=");
		expected.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void credentialsAsStringShouldSucceed() {
		String user = "user";
		String password = "secret";
		final String actual = BasicAuthUtils.credentialsAsString(user, password);
		final String expected = "dXNlcjpzZWNyZXQ=";
		Assert.assertEquals(expected, actual);
	}
}
