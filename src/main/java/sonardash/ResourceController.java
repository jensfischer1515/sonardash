package sonardash;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

@Controller
public class ResourceController {

    private final SonarQubeService sonarQubeService;

    @Autowired
    public ResourceController(SonarQubeService sonarQubeService) {
        this.sonarQubeService = sonarQubeService;
    }

    @RequestMapping("/resource")
    public String resource(Model model, @RequestParam String key) throws IOException {
        final List<Resource> resources = sonarQubeService.getResources(key);
        final Resource resource = Iterables.getOnlyElement(resources);
        model.addAttribute("resource", resource);

        final DateTime from = DateTime.now().minusDays(14);
        final ImmutableMap<MetricDefinition, Double> deltas = sonarQubeService.getDeltas(key, from);
        model.addAttribute("deltas", deltas);

        return "resource";
    }
}
