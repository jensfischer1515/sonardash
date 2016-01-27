package sonardash;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Iterables;

@Service
public class SonarQubeService {

    private final ObjectMapper objectMapper;

    private final SonarQubeCaller sonarQubeCaller;

    @Autowired
    private SonarQubeService(SonarQubeCaller sonarQubeCaller, ObjectMapper objectMapper) {
        this.sonarQubeCaller = sonarQubeCaller;
        this.objectMapper = objectMapper;
    }

    public List<Project> getAllProjects() {
        String endpoint = new StringBuilder("projects/index") //
                .append("?format=json") //
                .toString();
        String json = sonarQubeCaller.get(endpoint);
        try {
            return objectMapper.readValue(json, new Project.ListReference());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource getResource(String key) {
        String endpoint = new StringBuilder("resources") //
                .append("?format=json") //
                .append("&resource=" + key) //
                .append("&metrics=" + MetricDefinition.joinAll()) //
                .toString();
        String json = sonarQubeCaller.get(endpoint);
        try {
            List<Resource> resources = objectMapper.readValue(json, new Resource.ListReference());
            return Iterables.getOnlyElement(resources);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImmutableSortedMap<DateTime, ImmutableList<MetricValue>> getMetricHistory(String key, Interval between) {
        return getTimeMachine(key, between).map(TimeMachine::getHistory).orElse(ImmutableSortedMap.of());
    }

    public ImmutableSortedMap<MetricDefinition, Double> getDeltas(String key, Interval between) {
        return getTimeMachine(key, between).map(TimeMachine::getDeltas).orElse(ImmutableSortedMap.of());
    }

    private Optional<TimeMachine> getTimeMachine(String key, Interval between) {
        // http -vj http://sonar.epages.works:9000/api/timemachine/index format=json resource=shared:origin/master fromDateTime=2016-01-13T00:00:00+0100 toDate=2016-01-25T23:59:59+0100 metrics=ncloc,violations,coverage
        // http://sonar.epages.works:9000/api/timemachine/index?format=json&resource=shared%3Aorigin%2Fmaster&fromDateTime=2016-01-13T00%3A00%3A00%2B0100&toDate=2016-01-25T23%3A59%3A59%2B0100&metrics=ncloc%2Cviolations%2Ccoverage
        String endpoint = new StringBuilder("timemachine/index") //
                .append("?format=json") //
                .append("&resource=" + key) //
                .append("&fromDateTime=" + between.getStart().toString()) //
                .append("&toDateTime=" + between.getEnd().toString()) //
                .append("&metrics=" + MetricDefinition.joinAll()) //
                .toString();
        String json = sonarQubeCaller.get(endpoint);
        try {
            List<TimeMachine> timeMachines = objectMapper.readValue(json, new TimeMachine.ListReference());
            return timeMachines.isEmpty() ? Optional.empty() : Optional.of(timeMachines.get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
