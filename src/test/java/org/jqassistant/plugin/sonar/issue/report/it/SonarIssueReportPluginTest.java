package org.jqassistant.plugin.sonar.issue.report.it;

import java.util.Map;

import com.buschmais.jqassistant.core.report.api.ReportContext;

import org.jqassistant.plugin.sonar.issue.report.impl.SonarIssueReportPlugin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class SonarIssueReportPluginTest {
    private final SonarIssueReportPlugin testPlugin = new SonarIssueReportPlugin();
    private final ReportContext testContext = mock(ReportContext.class);
    private final String defaultLocationProperty = "sonar.issue.report.default-location";

    @Test
    void defaultLocationMustBeSet() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> testPlugin.configure(testContext, Map.of()));
        assertEquals("Missing required property '" + defaultLocationProperty + "' in '.jqassistant.yml'. "
            + "Please set the property according to the specifications in 'jqassistant-sonar-issue-report-plugin/README.adoc'", exception.getMessage());
    }

    @Test
    void defaultLocationMustReferToExistingFile() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> testPlugin.configure(testContext, Map.of(defaultLocationProperty, "none-existent-file")));
        assertEquals("File not found. The value of '" + defaultLocationProperty + "' in '.jqassistant.yml' is not valid.", exception.getMessage());
    }

    @Test
    void defaultLocationMustNotStartWithPeriod() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> testPlugin.configure(testContext, Map.of(defaultLocationProperty, ".editorconfig")));
        assertEquals("Filepath must not start with a period. The value of '" + defaultLocationProperty + "' in '.jqassistant.yml' is not valid.",
            exception.getMessage());
    }
}
