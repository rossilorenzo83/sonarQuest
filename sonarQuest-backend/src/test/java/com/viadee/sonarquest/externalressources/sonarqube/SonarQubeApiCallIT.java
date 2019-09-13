package com.viadee.sonarquest.externalressources.sonarqube;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.viadee.sonarquest.entities.SonarConfig;
import com.viadee.sonarquest.services.RestTemplateService;
import com.viadee.sonarquest.services.SonarConfigService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class SonarQubeApiCallIT {

	@Autowired
	private RestTemplateService restTemplateService;

	@Autowired
	private SonarConfigService sonarConfigService;

	@Test
	public void searchComponents() throws Exception {
		SonarConfig sonarConfig = sonarConfigService.getConfig();
		String sonarQubeServerUrl = sonarConfig.getSonarServerUrl();
		String projectKey = "assertj";
		SonarQubeApiCall apiCall =
				SonarQubeApiCall.onServer(sonarQubeServerUrl).searchComponents(SonarQubeComponentQualifier.TRK).withQuery(projectKey).build();
		RestTemplate restTemplate = restTemplateService.getRestTemplate(sonarConfig);

		final ResponseEntity<SonarQubeProjectResource> response = restTemplate.getForEntity(apiCall.asString(), SonarQubeProjectResource.class);
		assertEquals(1, response.getBody().getSonarQubeProjects().size());
	}

}
