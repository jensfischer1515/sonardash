package sonardash.model;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;

public class TimeMachine {

    @JsonProperty("cols")
    @JsonManagedReference
    private List<Column> columns = newArrayList();
    @JsonManagedReference
    private List<Cell> cells = newArrayList();

    @JsonIgnore
    public ImmutableSortedMap<MetricDefinition, Double> getDeltas() {
        final ImmutableSortedMap<DateTime, ImmutableList<MetricValue>> history = getHistory();

        if (history.isEmpty()) {
            return ImmutableSortedMap.of();
        }

        final Map<MetricDefinition, Double> firstValues = history.firstEntry().getValue().stream()
                .collect(toMap(MetricValue::getDefinition, MetricValue::getValue));

        final Map<MetricDefinition, Double> lastValues = history.lastEntry().getValue().stream()
                .collect(toMap(MetricValue::getDefinition, MetricValue::getValue));

        final Map<MetricDefinition, Double> deltas = firstValues.keySet().stream().collect(toMap(identity(), metricDefinition -> {
            final double firstValue = firstValues.getOrDefault(metricDefinition, 0.0);
            final double lastValue = lastValues.getOrDefault(metricDefinition, 0.0);
            return (lastValue - firstValue);
        }));

        return ImmutableSortedMap.copyOf(deltas);
    }

    @JsonIgnore
    public double getDelta(MetricDefinition metricDefinition) {
        return getDeltas().getOrDefault(metricDefinition, 0.0);
    }

    @JsonIgnore
    public ImmutableSortedMap<DateTime, ImmutableList<MetricValue>> getHistory() {
        ImmutableSortedMap.Builder<DateTime, ImmutableList<MetricValue>> history = ImmutableSortedMap.naturalOrder();

        for (Cell cell : cells) {
            ImmutableList.Builder<MetricValue> metricValues = ImmutableList.builder();
            for (int i = 0; i < cell.values.length; i++) {
                MetricValue metricValue = MetricValue.builder().definition(columns.get(i).definition).value(cell.values[i]).build();
                metricValues.add(metricValue);
            }
            history.put(cell.date, metricValues.build());
        }

        return history.build();
    }

    public static class ListReference extends TypeReference<List<TimeMachine>> {
    }

    @ToString(exclude = "timeMachine")
    @EqualsAndHashCode(of = "definition")
    private static class Column {
        @JsonBackReference
        private TimeMachine timeMachine;

        @JsonProperty("metric")
        private MetricDefinition definition;
    }

    @ToString(exclude = "timeMachine")
    @EqualsAndHashCode(of = "date")
    private static class Cell {
        @JsonBackReference
        private TimeMachine timeMachine;

        @JsonProperty("d")
        private DateTime date;

        @JsonProperty("v")
        private double[] values;
    }
}
