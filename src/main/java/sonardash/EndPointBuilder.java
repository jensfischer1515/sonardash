package sonardash;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sonardash.model.Event;
import sonardash.model.MetricDefinition;

import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.google.common.base.Joiner;

@RequiredArgsConstructor
public class EndPointBuilder {

    @NonNull
    private final String endpoint;

    private String resource;

    private DateTime from;

    private DateTime to;

    private MetricDefinition[] metrics;

    private Event.Category[] categories;

    public static EndPointBuilder forEndpoint(String endpoint) {
        return new EndPointBuilder(endpoint);
    }

    public EndPointBuilder resource(String resource) {
        this.resource = resource;
        return this;
    }

    public EndPointBuilder from(Interval between) {
        this.from = between.getStart();
        return this;
    }

    public EndPointBuilder to(Interval between) {
        this.to = between.getEnd();
        return this;
    }

    public EndPointBuilder metrics(MetricDefinition... metricDefinitions) {
        this.metrics = metricDefinitions;
        return this;
    }

    public EndPointBuilder categories(Event.Category... categories) {
        this.categories = categories;
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder(this.endpoint).append(param("?format", "json"));
        Optional.ofNullable(resource).ifPresent(s -> builder.append(param("resource", s)));
        Optional.ofNullable(from).ifPresent(s -> builder.append(param("fromDateTime", s)));
        Optional.ofNullable(to).ifPresent(s -> builder.append(param("toDateTime", s)));
        Optional.ofNullable(metrics).ifPresent(s -> builder.append(param("metrics", s)));
        Optional.ofNullable(categories).ifPresent(s -> builder.append(param("categories", s)));
        return builder.toString().replaceAll("&$", "");
    }

    private String param(String key, Object value) {
        return param(key, new Object[] { value });
    }

    private String param(String key, Object[] value) {
        return key + "=" + Joiner.on(',').join(value) + "&";
    }
}
