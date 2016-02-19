package sonardash;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import sonardash.model.Event;
import sonardash.model.MetricDefinition;
import sonardash.model.MetricValue;
import sonardash.model.Project;
import sonardash.model.Resource;
import sonardash.model.TimeMachine;

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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SonarQubeService {

    @NonNull
    private final ObjectMapper objectMapper;

    @NonNull
    private final SonarQubeCaller sonarQubeCaller;

    @SneakyThrows
    public List<Project> getAllProjects() {
        String endpoint = new StringBuilder("projects/index") //
                .append("?format=json") //
                .toString();
        String json = sonarQubeCaller.get(endpoint);
        return objectMapper.readValue(json, new Project.ListReference());
    }

    @SneakyThrows
    public Resource getResource(String key) {
        String endpoint = new StringBuilder("resources") //
                .append("?format=json") //
                .append("&resource=" + key) //
                .append("&metrics=" + MetricDefinition.joinAll()) //
                .toString();
        String json = sonarQubeCaller.get(endpoint);
        List<Resource> resources = objectMapper.readValue(json, new Resource.ListReference());
        return Iterables.getOnlyElement(resources);
    }

    public ImmutableSortedMap<DateTime, ImmutableList<MetricValue>> getMetricHistory(String key, Interval between) {
        return getTimeMachine(key, between).map(TimeMachine::getHistory).orElse(ImmutableSortedMap.of());
    }

    public ImmutableSortedMap<MetricDefinition, Double> getDeltas(String key, Interval between) {

        final List<Event> events = getEvents(key, between);

        return getTimeMachine(key, between).map(TimeMachine::getDeltas).orElse(ImmutableSortedMap.of());
    }

    @SneakyThrows
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
        List<TimeMachine> timeMachines = objectMapper.readValue(json, new TimeMachine.ListReference());
        return timeMachines.isEmpty() ? Optional.empty() : Optional.of(timeMachines.get(0));
    }

    @SneakyThrows
    public List<Event> getEvents(String key, Interval between) {
        String endpoint = new StringBuilder("events") //
                .append("?format=json") //
                .append("&resource=" + key) //
                .append("&categories=Version") //
                .append("&fromDateTime=" + between.getStart().toString()) //
                .append("&toDateTime=" + between.getEnd().toString()) //
                .toString();

        String json = sonarQubeCaller.get(endpoint);
        return objectMapper.<List<Event>>readValue(json, new Event.ListReference());
    }
}
