package com.viadee.sonarquest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viadee.sonarquest.externalressources.sonarqube.SonarQubeProject;
import com.viadee.sonarquest.services.ExternalResourceService;

@RestController
@RequestMapping("/externalRessource")
public class ExternalResourceController {

	@Autowired
	private ExternalResourceService externalResourceService;

	@GetMapping(value = "/project")
	public List<SonarQubeProject> getAllSonarQubeProjects() {
		return this.externalResourceService.getSonarQubeProjects();
	}

}
