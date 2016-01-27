package sonardash;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sonarqube")
public class SonarQubeProperties {

    @Value("#{environment.SONARQUBE_ENDPOINT ?: 'http://sonar.epages.works:9000/api/'}")
    private String endpoint;

    public String getEndpoint() {
        return endpoint;
    }
}
