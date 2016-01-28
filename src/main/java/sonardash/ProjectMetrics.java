package sonardash;

import lombok.Getter;

import java.util.Map;

@Getter
public class ProjectMetrics {
    private final String key;

    private final String name;

    private final ProjectMetric lines = new ProjectMetric();

    private final ProjectMetric classes = new ProjectMetric();

    private final ProjectMetric classComplexity = new ProjectMetric();

    private final ProjectMetric violations = new ProjectMetric();

    private final ProjectMetric coverage = new ProjectMetric();

    private final ProjectMetric testExecutionTime = new ProjectMetric();

    private final ProjectMetric skippedTests = new ProjectMetric();

    public ProjectMetrics(Project project) {
        key = project.getKey();
        name = project.getName();
    }

    public void setValues(Resource resource) {
        lines.value = resource.getMetric(MetricDefinition.ncloc).map(Resource.Metric::getValue).orElse(0.0);
        classes.value = resource.getMetric(MetricDefinition.classes).map(Resource.Metric::getValue).orElse(0.0);
        classComplexity.value = resource.getMetric(MetricDefinition.class_complexity).map(Resource.Metric::getValue).orElse(0.0);
        violations.value = resource.getMetric(MetricDefinition.violations).map(Resource.Metric::getValue).orElse(0.0);
        coverage.value = resource.getMetric(MetricDefinition.coverage).map(Resource.Metric::getValue).orElse(0.0);
        testExecutionTime.value = resource.getMetric(MetricDefinition.test_execution_time).map(Resource.Metric::getValue).orElse(0.0);
        skippedTests.value = resource.getMetric(MetricDefinition.skipped_tests).map(Resource.Metric::getValue).orElse(0.0);
    }

    public void setDeltas(Map<MetricDefinition, Double> deltas) {
        lines.delta = (deltas.getOrDefault(MetricDefinition.ncloc, 0.0));
        classes.delta = (deltas.getOrDefault(MetricDefinition.classes, 0.0));
        classComplexity.delta = (deltas.getOrDefault(MetricDefinition.class_complexity, 0.0));
        violations.delta = (deltas.getOrDefault(MetricDefinition.violations, 0.0));
        coverage.delta = (deltas.getOrDefault(MetricDefinition.coverage, 0.0));
        testExecutionTime.delta = (deltas.getOrDefault(MetricDefinition.test_execution_time, 0.0));
        skippedTests.delta = (deltas.getOrDefault(MetricDefinition.skipped_tests, 0.0));
    }

    @Getter
    public static class ProjectMetric {

        private double value;

        private double delta;
    }
}
