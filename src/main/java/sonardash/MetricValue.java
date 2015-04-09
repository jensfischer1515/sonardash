package sonardash;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Builder
@ToString
@EqualsAndHashCode(of = "definition")
public class MetricValue {
    @JsonProperty("metric")
    private MetricDefinition definition;

    private double value;
}
