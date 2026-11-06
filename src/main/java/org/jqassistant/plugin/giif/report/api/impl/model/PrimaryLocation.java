package org.jqassistant.plugin.giif.report.api.impl.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PrimaryLocation {

    private String message;
    private String filePath;
    private TextRange testRange

    @Builder
    @Getter
    @ToString
    public static class TextRange {

        private int startLine;
    }
}
