package org.jqassistant.plugin.sonar.issue.report.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * severity
 * Possible values: BLOCKER, HIGH, MEDIUM, LOW, INFO
 * according to <a href="https://docs.sonarsource.com/sonarqube-server/analyzing-source-code/importing-external-issues/generic-issue-import-format?ask=#list-of-objects-and-properties-in-the-report">...</a>
 */
public enum Severity {
    @JsonProperty(value = "INFO") INFO,
    @JsonProperty(value = "LOW") MINOR,
    @JsonProperty(value = "MEDIUM") MAJOR,
    @JsonProperty(value = "HIGH") CRITICAL,
    @JsonProperty(value = "BLOCKER") BLOCKER

}
