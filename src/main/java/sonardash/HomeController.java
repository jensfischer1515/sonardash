package sonardash;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final SonarQubeService sonarQubeService;

    @Autowired
    public HomeController(SonarQubeService sonarQubeService) {
        this.sonarQubeService = sonarQubeService;
    }

    @RequestMapping("/")
    public String home(Model model) throws IOException {
        List<Project> projects = sonarQubeService.getAllProjects();
        model.addAttribute("projects", projects);
        return "home";
    }
}
