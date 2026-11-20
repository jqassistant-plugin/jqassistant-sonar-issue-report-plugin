package org.jqassistant.plugin.sonar.issue.report.api.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SonarIssueReport {
    private List<Rule> rules;
    private List<Issue> issues;
}
