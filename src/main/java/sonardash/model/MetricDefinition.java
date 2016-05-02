package sonardash.model;

import com.google.common.base.Joiner;

// see http://docs.sonarqube.org/display/SONAR/Metric+definitions
public enum MetricDefinition {
    ncloc,
    classes,
    //alert_status,
    //quality_gate_details,
    complexity,
    class_complexity,
    violations,
    blocker_violations,
    critical_violations,
    major_violations,
    minor_violations,
    info_violations,
    test_execution_time,
    skipped_tests,
    coverage,
    branch_coverage,
    line_coverage,
    it_coverage,
    it_branch_coverage,
    it_line_coverage,
    overall_coverage,
    overall_branch_coverage,
    overall_line_coverage;

    public static String joinAll() {
        return Joiner.on(',').join(MetricDefinition.values());
    }
}
