package sonardash;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static sonardash.MetricDefinition.blocker_violations;
import static sonardash.MetricDefinition.class_complexity;
import static sonardash.MetricDefinition.critical_violations;
import static sonardash.MetricDefinition.info_violations;
import static sonardash.MetricDefinition.major_violations;
import static sonardash.MetricDefinition.minor_violations;
import static sonardash.Qualifier.TRK;
import static sonardash.Scope.PRJ;

import sonardash.Resource.Metric;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResoucesObjectMapperTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new SonarDashApplication().objectMapper().disable(INDENT_OUTPUT);
    }

    @Test
    public void should_deserialize_json_to_object() throws IOException {
        // GIVEN
        String json = "{\"id\":2707,\"key\":\"com.epages:epagesj:origin_dev\",\"name\":\"epagesj origin_dev\",\"scope\":\"PRJ\",\"qualifier\":\"TRK\",\"date\":\"2015-04-09T06:30:17-0400\",\"creationDate\":null,\"lname\":\"epagesj origin_dev\",\"version\":\"6.17.21-SNAPSHOT\",\"branch\":\"origin_dev\",\"description\":\"\",\"msr\":[{\"key\":\"class_complexity\",\"val\":4.8,\"frmt_val\":\"4.8\"},{\"key\":\"violations\",\"val\":599.0,\"frmt_val\":\"599\"},{\"key\":\"blocker_violations\",\"val\":0.0,\"frmt_val\":\"0\"},{\"key\":\"critical_violations\",\"val\":1.0,\"frmt_val\":\"1\"},{\"key\":\"major_violations\",\"val\":417.0,\"frmt_val\":\"417\"},{\"key\":\"minor_violations\",\"val\":128.0,\"frmt_val\":\"128\"},{\"key\":\"info_violations\",\"val\":53.0,\"frmt_val\":\"53\"}]}";

        // WHEN
        Resource object = objectMapper.readValue(json, Resource.class);

        // THEN
        assertThat(object.getId(), is(equalTo(2707)));
        assertThat(object.getKey(), is(equalTo("com.epages:epagesj:origin_dev")));
        assertThat(object.getName(), is(equalTo("epagesj origin_dev")));
        assertThat(object.getQualifier(), is(equalTo(TRK)));
        assertThat(object.getScope(), is(equalTo(PRJ)));
        assertFalse(object.getMetrics().isEmpty());

        Metric classComplexity = Metric.builder().definition(class_complexity).build();
        Metric violations = Metric.builder().definition(MetricDefinition.violations).build();
        Metric blockerViolations = Metric.builder().definition(blocker_violations).build();
        Metric criticalViolations = Metric.builder().definition(critical_violations).build();
        Metric majorViolations = Metric.builder().definition(major_violations).build();
        Metric minorViolations = Metric.builder().definition(minor_violations).build();
        Metric infoViolations = Metric.builder().definition(info_violations).build();
        assertThat(object.getMetrics(),
                hasItems(classComplexity, violations, blockerViolations, criticalViolations, majorViolations, minorViolations,
                        infoViolations));
    }
}
