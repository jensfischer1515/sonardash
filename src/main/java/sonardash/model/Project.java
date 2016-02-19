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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

@Getter
@Builder
@ToString
@EqualsAndHashCode(of = "key")
@AllArgsConstructor(access = PUBLIC)
@NoArgsConstructor(access = PRIVATE)
public class Project {

    @JsonProperty("id")
    private String id;
    @JsonProperty("k")
    private String key;
    @JsonProperty("nm")
    private String name;
    @JsonProperty("qu")
    private Qualifier qualifier;
    @JsonProperty("sc")
    private Scope scope;

    public static class ListReference extends TypeReference<List<Project>> {
    }
}
