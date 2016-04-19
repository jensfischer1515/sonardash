package sonardash;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sonardash.model.Project;
import sonardash.model.ProjectMetrics;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Sets;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeltaController {

    @NonNull
    private final SonarQubeService sonarQubeService;

    @ModelAttribute("projectFilter")
    public Predicate<Project> populateProjectFilter(@RequestParam(name = "key", required = false) String[] keys) {
        return project -> {
            final Set<String> validKeys = Optional.ofNullable(keys).map(Sets::newHashSet).orElse(newHashSet());
            return validKeys.isEmpty() || validKeys.contains(project.getKey());
        };
    }

    @ModelAttribute("interval")
    public Interval populateInterval(@RequestParam(value = "from", required = false) String from /* 2016-01-24T00:00 */, //
                                     @RequestParam(value = "to", required = false) String to /* 2016-01-29T23:59 */, //
                                     @RequestParam(name = "days", defaultValue = "14") int days //
    ) {
        final DateTime toDate = Optional.ofNullable(to).map(DateTime::parse).orElse(DateTime.now());
        final DateTime fromDate = Optional.ofNullable(from).map(DateTime::parse).orElse(toDate.minusDays(days));
        return new Interval(fromDate, toDate);
    }

    @RequestMapping("/delta")
    public String delta(Model model, //
                        @ModelAttribute("projectFilter") Predicate<Project> projectFilter, //
                        @ModelAttribute("interval") Interval interval //
    ) {
        final List<ProjectMetrics> projectMetrics = sonarQubeService.getAllProjects().parallelStream() //
                .filter(projectFilter) //
                .map(ProjectMetrics::new) //
                .map(projectMetric -> {
                    final String key = projectMetric.getKey();
                    projectMetric.setValues(sonarQubeService.getResource(key));
                    projectMetric.setDeltas(sonarQubeService.getDeltas(key, interval));
                    return projectMetric;
                }) //
                .collect(toList());

        projectMetrics.add(summary(projectMetrics));

        model.addAttribute("projectMetrics", projectMetrics);
        return "delta";
    }

    private ProjectMetrics summary(List<ProjectMetrics> projectMetrics) {
        ProjectMetrics summary = new ProjectMetrics("summary", "Summary");
        projectMetrics.forEach(p -> p.addTo(summary));
        // calculate some averages
        summary.getClassComplexity().setValue(summary.getClassComplexity().getValue() / projectMetrics.size());
        summary.getClassComplexity().setDelta(summary.getClassComplexity().getDelta() / projectMetrics.size());
        summary.getCoverage().setValue(summary.getCoverage().getValue() / projectMetrics.size());
        summary.getCoverage().setDelta(summary.getCoverage().getDelta() / projectMetrics.size());
        return summary;
    }
}
