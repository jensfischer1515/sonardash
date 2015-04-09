package sonardash;

import com.google.common.base.Joiner;

// see http://docs.sonarqube.org/display/SONAR/Metric+definitions
public enum MetricDefinition {
    class_complexity,
    violations,
    blocker_violations,
    critical_violations,
    major_violations,
    minor_violations,
    info_violations;

    public static String joinAll() {
        return Joiner.on(',').join(MetricDefinition.values());
    }
}
