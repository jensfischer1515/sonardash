package sonardash;

import static java.util.stream.Collectors.toList;

import lombok.Data;
import sonardash.Resource.Metric;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.ImmutableSortedMap;

@Controller
public class SprintController {

    private final SonarQubeService sonarQubeService;

    @Autowired
    public SprintController(SonarQubeService sonarQubeService) {
        this.sonarQubeService = sonarQubeService;
    }

    @RequestMapping("/sprint")
    public String sprint(Model model, @RequestParam(defaultValue = "14") int days) {
        final List<ProjectMetrics> projectMetrics = sonarQubeService.getAllProjects().stream() //
                .filter(this::isOriginMaster) //
                .map(ProjectMetrics::new) //
                .map(projectMetric -> {
                    final String key = projectMetric.getKey();

                    final Resource resource = sonarQubeService.getResource(key);
                    projectMetric.setLines(resource.getMetric(MetricDefinition.ncloc).map(Metric::getValue).orElse(0.0));
                    projectMetric.setCoverage(resource.getMetric(MetricDefinition.coverage).map(Metric::getValue).orElse(0.0));
                    projectMetric.setViolations(resource.getMetric(MetricDefinition.violations).map(Metric::getValue).orElse(0.0));

                    final ImmutableSortedMap<MetricDefinition, Double> deltas = sonarQubeService.getDeltas(key, getInterval(days));
                    projectMetric.setLinesDelta(deltas.getOrDefault(MetricDefinition.ncloc, 0.0));
                    projectMetric.setCoverageDelta(deltas.getOrDefault(MetricDefinition.coverage, 0.0));
                    projectMetric.setViolationsDelta(deltas.getOrDefault(MetricDefinition.violations, 0.0));
                    return projectMetric;
                }) //
                .collect(toList());

        model.addAttribute("projectMetrics", projectMetrics);
        return "sprint";
    }

    private boolean isOriginMaster(Project project) {
        return project.getName().contains("origin/master");
    }

    private Interval getInterval(int days) {
        final DateTime to = DateTime.now();
        final DateTime from = to.minusDays(days);
        return new Interval(from, to);
    }

    @Data
    public static class ProjectMetrics {
        private String key;

        private String name;

        private double lines;

        private double linesDelta;

        private double coverage;

        private double coverageDelta;

        private double violations;

        private double violationsDelta;

        public ProjectMetrics(Project project) {
            this.key = project.getKey();
            this.name = project.getName();
        }
    }
}
