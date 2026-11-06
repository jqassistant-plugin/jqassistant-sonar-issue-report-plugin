package org.jqassistant.plugin.giif.report.api.impl.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Rule {

    private String id;
    private String name;
    private String description;
    private final String engineId = "jQAssistant";
    private final String cleanCodeAttribute "CONVENTIONAL"; // this attribute is needed on SonarServer only but is optional on SonarCloud
    private List<Impact> impacts;

    @Builder
    @Getter
    @ToString
    public static class Impact {

        private final String softwareQuality = "MAINTAINABILITY"

        private Severity severity;
    }
}
