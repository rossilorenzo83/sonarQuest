package com.viadee.sonarquest.externalressources;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.google.common.collect.Lists;

public final class BasicAuthUtils {

	private BasicAuthUtils() {
	}

	public static HttpHeaders headers(String user, String password) {
		String base64Creds = credentialsAsString(user, password);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
		return headers;
	}

	static String credentialsAsString(String user, String password) {
		String plainCreds = user + ":" + password;
		byte[] plainCredsBytes = plainCreds.getBytes(Charset.defaultCharset());
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		return new String(base64CredsBytes, Charset.defaultCharset());
	}

}
