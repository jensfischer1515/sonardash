package sonardash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
// TODO replace those three annoations with @SpringBootApplication once https://youtrack.jetbrains.com/issue/IDEA-119230 is done
public class SonarDashApplication {

    public static void main(String[] args) {
        SpringApplication.run(SonarDashApplication.class, args);
    }

}
