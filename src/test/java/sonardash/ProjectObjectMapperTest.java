package sonardash;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static sonardash.Qualifier.TRK;
import static sonardash.Scope.PRJ;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectObjectMapperTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new SonarDashApplication().objectMapper().disable(INDENT_OUTPUT);
    }

    @Test
    public void should_serialize_object_to_json() throws IOException {
        // GIVEN
        Project object = Project.builder()
                .id("123")
                .key("myproject")
                .name("My Project")
                .qualifier(TRK)
                .scope(PRJ)
                .build();

        // WHEN
        String json = objectMapper.writeValueAsString(object);

        // THEN
        assertThat(json, is(equalTo("{\"id\":\"123\",\"k\":\"myproject\",\"nm\":\"My Project\",\"qu\":\"TRK\",\"sc\":\"PRJ\"}")));
    }

    @Test
    public void should_deserialize_json_to_object() throws IOException {
        // GIVEN
        String json = "{\"id\":\"123\",\"k\":\"myproject\",\"nm\":\"My Project\",\"qu\":\"TRK\",\"sc\":\"PRJ\"}";

        // WHEN
        Project object = objectMapper.readValue(json, Project.class);

        // THEN
        assertThat(object.getId(), is(equalTo("123")));
        assertThat(object.getKey(), is(equalTo("myproject")));
        assertThat(object.getName(), is(equalTo("My Project")));
        assertThat(object.getQualifier(), is(equalTo(TRK)));
        assertThat(object.getScope(), is(equalTo(PRJ)));
    }
}
