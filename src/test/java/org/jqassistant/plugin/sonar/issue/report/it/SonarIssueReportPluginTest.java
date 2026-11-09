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

    @Test
    void SonarDefaultLocationMustBeSet() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> testPlugin.configure(testContext, Map.of()));
        assertEquals("Missing required property 'sonar.default.location' in '.jqassistant.yml'. "
            + "Please set the property according to the specifications in 'jqassistant-sonar-issue-report-plugin/README.adoc'", exception.getMessage());
    }
}
