package sonardash.model;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

@Getter
@Builder
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = PUBLIC)
@NoArgsConstructor(access = PRIVATE)
public class Event {

    private String id;
    @JsonProperty("n")
    private String name;
    @JsonProperty("rk")
    private String key;
    @JsonProperty("c")
    private Category category;
    @JsonProperty("dt")
    private DateTime date;

    public static class ListReference extends TypeReference<List<Event>> {
    }

    public enum Category {
        Version, Alert
    }
}
