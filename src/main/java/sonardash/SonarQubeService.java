package sonardash;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@Service
public class SonarQubeService {

    private final ObjectMapper objectMapper;

    private final SonarQubeCaller sonarQubeCaller;

    @Autowired
    private SonarQubeService(SonarQubeCaller sonarQubeCaller, ObjectMapper objectMapper) {
        this.sonarQubeCaller = sonarQubeCaller;
        this.objectMapper = objectMapper;
    }

    public List<Project> getAllProjects() throws IOException {
        String endpoint = new StringBuilder("projects/index") //
                .append("?format=json") //
                .toString();
        String json = sonarQubeCaller.get(endpoint);
        return objectMapper.readValue(json, new Project.ListReference());
    }

    public List<Resource> getResources(String key) throws IOException {
        String endpoint = new StringBuilder("resources") //
                .append("?format=json") //
                .append("&resource=" + key) //
                .append("&metrics=" + MetricDefinition.joinAll()) //
                .toString();
        String json = sonarQubeCaller.get(endpoint);
        return objectMapper.readValue(json, new Resource.ListReference());
    }

    public ImmutableMap<DateTime, ImmutableList<MetricValue>> getMetricHistory(String key, DateTime from) throws IOException {
        return getMetricHistory(key, from, now());
    }

    private DateTime now() {
        return new DateTime();
    }

    public ImmutableMap<DateTime, ImmutableList<MetricValue>> getMetricHistory(String key, DateTime from, DateTime to) throws IOException {
        return getMetricHistory(key, new Interval(from, to));
    }

    public ImmutableMap<DateTime, ImmutableList<MetricValue>> getMetricHistory(String key, Interval between) throws IOException {
        String endpoint = new StringBuilder("timemachine/index") //
                .append("?format=json") //
                .append("&resource=" + key) //
                .append("&fromDateTime=" + between.getStart().toString()) //
                .append("&toDateTime=" + between.getEnd().toString()) //
                .append("&metrics=" + MetricDefinition.joinAll()) //
                .toString();
        String json = sonarQubeCaller.get(endpoint);
        List<TimeMachine> timeMachines = objectMapper.readValue(json, new TimeMachine.ListReference());

        return timeMachines.isEmpty() ? ImmutableMap.of() : timeMachines.get(0).getHistory();
    }
}
