package org.jqassistant.plugin.sonar.issue.report.it;

import java.util.Map;

import com.buschmais.jqassistant.core.report.api.ReportContext;
import com.buschmais.jqassistant.core.report.api.ReportException;

import org.jqassistant.plugin.sonar.issue.report.impl.SonarIssueReportPlugin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class SonarIssueReportPluginTest {
    private final SonarIssueReportPlugin testPlugin = new SonarIssueReportPlugin();
    private final ReportContext testContext = mock(ReportContext.class);

    @Test
    void defaultLocationMustBeSet() {
        ReportException exception = assertThrows(ReportException.class, () -> testPlugin.configure(testContext, Map.of()));
        assertEquals("Missing required property '" + SonarIssueReportPlugin.PROPERTY_DEFAULT_LOCATION + "'. "
            + "Please set the property according to the specifications in 'jqassistant-sonar-issue-report-plugin/README.adoc'", exception.getMessage());
    }

    @Test
    void defaultLocationMustReferToExistingFile() {
        ReportException exception = assertThrows(ReportException.class,
            () -> testPlugin.configure(testContext, Map.of(SonarIssueReportPlugin.PROPERTY_DEFAULT_LOCATION, "non-existent-file")));
        assertEquals("File not found. The value 'non-existent-file' of property '" + SonarIssueReportPlugin.PROPERTY_DEFAULT_LOCATION + "' is not valid.", exception.getMessage());
    }

    @Test
    void defaultLocationMustNotStartWithPeriod() {
        ReportException exception = assertThrows(ReportException.class,
            () -> testPlugin.configure(testContext, Map.of(SonarIssueReportPlugin.PROPERTY_DEFAULT_LOCATION, ".editorconfig")));
        assertEquals("Filepath must not start with a period. The value '.editorconfig' of property '" + SonarIssueReportPlugin.PROPERTY_DEFAULT_LOCATION + "' is not valid.",
            exception.getMessage());
    }
}
