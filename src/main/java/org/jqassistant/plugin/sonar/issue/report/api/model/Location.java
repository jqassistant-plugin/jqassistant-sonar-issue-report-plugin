package org.jqassistant.plugin.sonar.issue.report.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class Location {
    private String message;
    private String filePath;
    private TextRange textRange;

    @Getter
    @Builder
    @ToString
    public static class TextRange {
        private int startLine;
        private int endLine;
    }
}
