package org.jqassistant.plugin.giif.report.impl;

import com.buschmais.jqassistant.core.report.api.model.Result.Status;

import org.jqassistant.plugin.giif.report.api.impl.model.Severity;
import org.mapstruct.Mapper;

@Mapper
public interface SeverityMapper {
    Severity toReport(com.buschmais.jqassistant.core.report.api.model.Result.Status status);
}
