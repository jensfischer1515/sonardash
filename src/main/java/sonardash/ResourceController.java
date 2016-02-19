package sonardash;

import lombok.RequiredArgsConstructor;
import sonardash.model.MetricDefinition;
import sonardash.model.Resource;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.ImmutableMap;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceController {

    private final SonarQubeService sonarQubeService;

    @RequestMapping("/resource")
    public String resource(Model model, @RequestParam String key, @RequestParam(defaultValue = "14") int days) throws IOException {
        final Resource resource = sonarQubeService.getResource(key);
        model.addAttribute("resource", resource);

        final ImmutableMap<MetricDefinition, Double> deltas = sonarQubeService.getDeltas(key, getInterval(days));
        model.addAttribute("deltas", deltas);

        return "resource";
    }

    private Interval getInterval(int days) {
        final DateTime to = DateTime.now();
        final DateTime from = to.minusDays(days);
        return new Interval(from, to);
    }
}
