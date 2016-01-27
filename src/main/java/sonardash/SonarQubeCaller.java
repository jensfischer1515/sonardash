package sonardash;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SonarQubeCaller {

    private final SonarQubeProperties sonarQubeProperties;

    @Autowired
    public SonarQubeCaller(SonarQubeProperties sonarQubeProperties) {
        this.sonarQubeProperties = sonarQubeProperties;
    }

    private Header getAcceptHeader() {
        return new BasicHeader(ACCEPT, APPLICATION_JSON_VALUE);
    }

    public String get(String endpointSuffix) {
        try {
            Response response = Request.Get(sonarQubeProperties.getEndpoint() + endpointSuffix).addHeader(getAcceptHeader()).execute();
            return response.returnContent().asString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
