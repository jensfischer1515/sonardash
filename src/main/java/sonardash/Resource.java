package sonardash;

import static com.google.common.collect.Lists.newArrayList;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

@Getter
@Builder
@ToString
@EqualsAndHashCode(of = "key")
@AllArgsConstructor(access = PUBLIC)
@NoArgsConstructor(access = PRIVATE)
public class Resource  {

    private int id;
    private String key;
    private String name;
    private Scope scope;
    private Qualifier qualifier;
    private DateTime date;
    private DateTime creationDate;
    @JsonProperty("lname")
    private String longName;
    private String version;
    private String branch;
    private String description;
    @JsonProperty("msr")
    @JsonManagedReference
    private List<Metric> metrics = newArrayList();

    @JsonIgnore
    public Project getProject() {
        return Project.builder().id(String.valueOf(id)).key(key).name(name).qualifier(qualifier).scope(scope).build();
    }

    static class ListReference extends TypeReference<List<Resource>> {
    }

    @Getter
    @Builder
    @ToString(exclude = {"resource"})
    @EqualsAndHashCode(of = "definition")
    @AllArgsConstructor(access = PUBLIC)
    @NoArgsConstructor(access = PRIVATE)
    public static class Metric {

        @Getter(PROTECTED)
        @JsonBackReference
        private Resource resource;

        @JsonProperty("key")
        private MetricDefinition definition;

        @JsonProperty("val")
        private Double value;

        @JsonProperty("frmt_val")
        private String formattedValue;

        @JsonProperty("data")
        private String data;
    }

    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode(of = "metric")
    @AllArgsConstructor(access = PUBLIC)
    @NoArgsConstructor(access = PRIVATE)
    public static class QualityGateDetailsCondition {

        @JsonProperty("metric")
        private String metric;

        @JsonProperty("op")
        private String operation;

        @JsonProperty("period")
        private int period;

        @JsonProperty("error")
        private String error;

        @JsonProperty("actual")
        private String actual;

        @JsonProperty("level")
        private String level;
    }
}
