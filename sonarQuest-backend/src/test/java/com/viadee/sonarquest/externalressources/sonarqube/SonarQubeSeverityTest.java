package com.viadee.sonarquest.externalressources.sonarqube;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class SonarQubeSeverityTest {

	@Test
	public void testFromString() throws Exception {
		SonarQubeSeverity severity = SonarQubeSeverity.fromString("BLOCKER");
		assertSame(SonarQubeSeverity.BLOCKER, severity);
	}

}
