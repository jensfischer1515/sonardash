package sonardash;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sonardash.model.Project;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HomeController {

    @NonNull
    private final SonarQubeService sonarQubeService;

    @RequestMapping("/")
    public String home(Model model) throws IOException {
        List<Project> projects = sonarQubeService.getAllProjects();
        model.addAttribute("projects", projects);
        return "home";
    }
}
