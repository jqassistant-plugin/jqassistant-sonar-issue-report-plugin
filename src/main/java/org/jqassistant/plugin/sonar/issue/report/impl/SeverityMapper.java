package org.jqassistant.plugin.sonar.issue.report.impl;

import org.jqassistant.plugin.sonar.issue.report.api.model.Severity;
import org.mapstruct.Mapper;

@Mapper
public interface SeverityMapper {
    Severity toReport(com.buschmais.jqassistant.core.rule.api.model.Severity severity);
}
