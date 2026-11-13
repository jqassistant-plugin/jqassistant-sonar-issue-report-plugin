package org.jqassistant.plugin.sonar.issue.report.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Issue {
    private String ruleId;
    private Location primaryLocation;
}
