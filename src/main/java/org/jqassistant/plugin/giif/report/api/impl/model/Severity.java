package org.jqassistant.plugin.giif.report.api.impl.model;

import com.fasterxml.jackson.annotation.JsonProperty;

//possible values documented by Sonarqube BLOCKER, HIGH, MEDIUM, LOW, INFO

public enum Severity {

    @JsonProperty(value = "INFO") SUCCESS,
    @JsonProperty(value = "MEDIUM") WARNING,
    @JsonProperty(value = "HIGH") FAILURE,
    @JsonProperty(value = "INFO") SKIPPED
}
