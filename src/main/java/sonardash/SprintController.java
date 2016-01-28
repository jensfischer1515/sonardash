package sonardash;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                    projectMetric.setValues(sonarQubeService.getResource(key));
                    projectMetric.setDeltas(sonarQubeService.getDeltas(key, getInterval(days)));
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
}
