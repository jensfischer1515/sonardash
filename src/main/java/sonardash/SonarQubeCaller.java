package sonardash;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.apache.http.Header;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SonarQubeCaller {

    @NonNull
    private final SonarQubeProperties sonarQubeProperties;

    private Header getAcceptHeader() {
        return new BasicHeader(ACCEPT, APPLICATION_JSON_VALUE);
    }

    @SneakyThrows
    public String get(String endpointSuffix) {
        Response response = Request.Get(sonarQubeProperties.getEndpoint() + endpointSuffix).addHeader(getAcceptHeader()).execute();
        return response.returnContent().asString();
    }
}
