package org.jqassistant.plugin.sonar.issue.report.it;

import com.buschmais.jqassistant.plugin.java.annotation.jQASuppress;

public class TypeWithIssues {

    private static String issueField;

    public void issueMethod() {
        System.out.println("issueMethod");
    }

    @jQASuppress(value = { "sonar-issue-report-it:ConstraintWithFailures", "sonar-issue-report-it:ConstraintWitWarnings" })
    public void suppressedIssueMethod() {
        System.out.println("suppressedIssueMethod");
    }

    public void nonIssueMethod() {
        System.out.println("nonIssueMethod");
    }
}
