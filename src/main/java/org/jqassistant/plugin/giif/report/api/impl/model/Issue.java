package org.jqassistant.plugin.giif.report.api.impl.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Run {

    private String ruleId;
    private PrimayLocation primayLocation;

}
