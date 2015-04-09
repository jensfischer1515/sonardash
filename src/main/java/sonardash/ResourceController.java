package sonardash;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<Resource> resources = sonarQubeService.getResources(key);
        Resource resource = Iterables.getOnlyElement(resources);
        model.addAttribute("resource", resource);
        return "resource";
    }
}
