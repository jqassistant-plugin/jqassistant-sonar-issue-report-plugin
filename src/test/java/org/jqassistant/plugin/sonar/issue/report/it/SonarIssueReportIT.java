package org.jqassistant.plugin.sonar.issue.report.it;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.WARNING;
import static com.buschmais.jqassistant.core.scanner.api.DefaultScope.NONE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

public class SonarIssueReportIT {
    protected abstract static class AbstractNestedSonarIssueReportIT extends AbstractJavaPluginIT {
        protected void scanSourcesAndClasspathDirectory() {
            File classesDirectory = getClassesDirectory(TypeWithIssues.class);
            File sourceDirectory = new File(classesDirectory, "../../src/test/java");
            getScanner().scan(sourceDirectory, null, NONE);
            scanClassPathDirectory(classesDirectory);
        }
    }

    @Nested
    public class DefaultContentIT extends AbstractNestedSonarIssueReportIT {

        @Test
        void verifyDefaultContentOnWarning() throws RuleException, IOException {
            scanSourcesAndClasspathDirectory();
            Result<Constraint> result = validateConstraint("sonar-issue-report-it:ConstraintWithWarnings", Map.of("fqn", TypeWithIssues.class.getName()));

            verify("ConstraintWithWarnings", WARNING, result);
        }

        @Test
        void verifyDefaultContentOnFailure() throws RuleException, IOException {
            scanSourcesAndClasspathDirectory();

            Result<Constraint> result = validateConstraint("sonar-issue-report-it:ConstraintWithFailures", Map.of("fqn", TypeWithIssues.class.getName()));

            verify("ConstraintWithFailures", FAILURE, result);
        }
    }

    @Nested
    public class ConstraintWithoutLocation extends AbstractNestedSonarIssueReportIT {

        @Test
        void verifyConstraintWithoutLocation() throws RuleException, IOException {
            Result<Constraint> result = validateConstraint("sonar-issue-report-it:ConstraintWithoutLocation");

            verify("ConstraintWithoutLocation", FAILURE, result);
        }
    }

    private void verify(String constraintId, Result.Status expectedStatus, Result<Constraint> result) throws IOException {
        assertThat(result.getStatus()).isEqualTo(expectedStatus);
        File sarifReport = new File("target/jqassistant/report/sonar-issues/jqassistant-sonar-issue-report.json");
        assertThat(sarifReport).exists();
        String expectedJson = IOUtils.toString(Objects.requireNonNull(SonarIssueReportIT.class.getResourceAsStream("/reference/" + constraintId + ".json")),
            UTF_8);
        assertJson(sarifReport).isEqualTo(expectedJson);
    }
}
