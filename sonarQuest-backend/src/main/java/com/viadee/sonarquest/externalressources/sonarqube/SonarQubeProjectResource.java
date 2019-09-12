package com.viadee.sonarquest.externalressources.sonarqube;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SonarQubeProjectResource {

    private SonarQubePaging paging;

    @JsonProperty("components")
    private List<SonarQubeProject> sonarQubeProjects;

    public List<SonarQubeProject> getSonarQubeProjects() {
        return sonarQubeProjects;
    }

    public void setSonarQubeProjects(List<SonarQubeProject> sonarQubeProjects) {
        this.sonarQubeProjects = sonarQubeProjects;
    }

    public SonarQubePaging getPaging() {
        return paging;
    }

    public void setPaging(SonarQubePaging paging) {
        this.paging = paging;
    }

    @Override
    public String toString() {
        return "SonarQubeProjectRessource{" +
                "paging=" + paging +
                ", projects=" + sonarQubeProjects +
                '}';
    }
}
