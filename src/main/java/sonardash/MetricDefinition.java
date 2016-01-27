package sonardash;

import com.google.common.base.Joiner;

// see http://docs.sonarqube.org/display/SONAR/Metric+definitions
public enum MetricDefinition {
    ncloc,
    alert_status,
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
    coverage,
    branch_coverage,
    line_coverage;

    public static String joinAll() {
        return Joiner.on(',').join(MetricDefinition.values());
    }
}
