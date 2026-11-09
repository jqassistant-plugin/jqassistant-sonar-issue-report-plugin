package org.jqassistant.plugin.sonar.issue.report.impl;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.buschmais.jqassistant.core.report.api.ReportContext;
import com.buschmais.jqassistant.core.report.api.ReportPlugin;
import com.buschmais.jqassistant.core.report.api.ReportPlugin.Default;
import com.buschmais.jqassistant.core.report.api.model.Column;
import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.report.api.model.source.FileLocation;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.core.rule.api.model.ExecutableRule;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.cmp.NestedMessageContent;
import org.jqassistant.plugin.sonar.issue.report.api.model.Issue;
import org.jqassistant.plugin.sonar.issue.report.api.model.Location;
import org.jqassistant.plugin.sonar.issue.report.api.model.Rule;
import org.jqassistant.plugin.sonar.issue.report.api.model.SonarIssueReport;
import org.mapstruct.factory.Mappers;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.WARNING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Default
@Slf4j
public class SonarIssueReportPlugin implements ReportPlugin {
    public static final String REPORT_DIRECTORY = "sonar-issues";
    public static final String REPORT_FILE = "jqassistant-sonar-issue-report.json";
    private static final String PROPERTY_DEFAULT_LOCATION = "sonar.default.location";
    private static final SeverityMapper SEVERITY_MAPPER = Mappers.getMapper(SeverityMapper.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setDefaultPropertyInclusion(NON_NULL);

    private ReportContext reportContext;
    private String defaultLocation;
    private List<Rule> rules;
    private List<Issue> issues;
    private Set<String> registeredRuleIds;
    private String message;

    @Override
    public void configure(ReportContext reportContext, Map<String, Object> properties) {
        this.reportContext = reportContext;
        this.defaultLocation = (String) properties.get(PROPERTY_DEFAULT_LOCATION);
        if (this.defaultLocation == null) {
            throw new IllegalStateException("Missing required property 'sonar.default.location' in '.jqassistant.yml'. "
                + "Please set the property according to the specifications in 'jqassistant-sonar-issue-report-plugin/README.adoc'");
        }

    }

    @Override
    public void begin() {
        issues = new ArrayList<>();
        rules = new ArrayList<>();
        registeredRuleIds = new HashSet<>();
    }

    @Override
    public void setResult(Result<? extends ExecutableRule> result) {
        Result.Status status = result.getStatus();
        if (FAILURE.equals(status) || WARNING.equals(status)) {
            ExecutableRule<?> executableRule = result.getRule();
            Constraint constraint = (Constraint) executableRule;

            Rule.Impact.ImpactBuilder severityBuilder = Rule.Impact.builder();
            if (registeredRuleIds.add(constraint.getId())) {
                rules.add(Rule.builder()
                    .id(constraint.getId())
                    .name(constraint.getId())
                    .description(constraint.getDescription())
                    .impacts(List.of(severityBuilder.severity(SEVERITY_MAPPER.toReport(result.getSeverity()))
                        .build()))
                    .build());
            }
            for (Row row : result.getRows()) {
                if (!row.isHidden()) {
                    this.message = formatColumns(constraint.getDescription(), row);
                    issues.add(Issue.builder()
                        .ruleId(constraint.getId())
                        .primaryLocation(getLocation(result, row))
                        .build());
                }
            }
        }
    }

    @Override
    public void end() throws RuntimeException {
        File reportDirectory = reportContext.getReportDirectory(REPORT_DIRECTORY);
        SonarIssueReport report = SonarIssueReport.builder()
            .rules(rules)
            .issues(issues)
            .build();
        try {
            File file = new File(reportDirectory, REPORT_FILE).getCanonicalFile();
            log.info("Writing SonarIssue report to {}.", file);
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
                .writeValue(file, report);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write SonarIssue report file.", e);
        }
    }

    private Location getLocation(Result<? extends ExecutableRule> result, Row row) {
        return result.getPrimaryColumn()
            .map(primaryColumnName -> row.getColumns()
                .get(primaryColumnName))
            .flatMap(Column::getSourceLocation)
            .filter(location -> location instanceof FileLocation)
            .map(location -> (FileLocation) location)
            .filter(location -> location.getPath() != null)
            .map(this::getFileLocation)
            .orElse(Location.builder()
                .message(message)
                .filePath(defaultLocation)
                .textRange(Location.TextRange.builder()
                    .startLine(1)
                    .endLine(1)
                    .build())
                .build());
    }

    private Location getFileLocation(FileLocation location) {
        Location.LocationBuilder locationBuilder = Location.builder()
            .filePath(location.getPath())
            .message(message);
        Location.TextRange textRange = Location.TextRange.builder()
            .startLine(location.getStartLine()
                .orElse(1))
            .endLine(location.getEndLine()
                .orElse(1))
            .build();
        return locationBuilder.textRange(textRange)
            .build();
    }

    private String formatColumns(String description, Row row) {
        StringBuilder message = new StringBuilder();
        if (description != null) {
            message.append(description)
                .append(" ");
        }
        row.getColumns()
            .forEach((key, value) -> {
                if (!"location".equalsIgnoreCase(key)) {
                    message.append("| ")
                        .append(key)
                        .append(": ")
                        .append(value.getLabel())
                        .append(" ");
                }
            });
        return message.toString();
    }

}
