package sonardash;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static sonardash.MetricDefinition.class_complexity;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TimeMachineObjectMapperTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new SonarDashApplication().objectMapper().disable(INDENT_OUTPUT);
    }

    @Test
    public void should_deserialize_json_to_object() throws IOException {
        // GIVEN
        String json = "{\"cols\":[{\"metric\":\"class_complexity\"},{\"metric\":\"violations\"},{\"metric\":\"blocker_violations\"},{\"metric\":\"critical_violations\"},{\"metric\":\"major_violations\"},{\"metric\":\"minor_violations\"},{\"metric\":\"info_violations\"}],\"cells\":[{\"d\":\"2015-02-03T17:08:25-0500\",\"v\":[4.7,0,0,0,0,0,0]},{\"d\":\"2015-02-10T17:57:25-0500\",\"v\":[4.7,981,1,20,756,163,41]},{\"d\":\"2015-02-12T14:05:45-0500\",\"v\":[4.7,677,0,0,474,163,40]},{\"d\":\"2015-02-12T16:13:28-0500\",\"v\":[4.7,675,0,0,473,163,39]},{\"d\":\"2015-02-13T14:05:43-0500\",\"v\":[4.7,555,0,0,353,163,39]},{\"d\":\"2015-02-19T16:19:14-0500\",\"v\":[4.7,528,0,0,359,131,38]},{\"d\":\"2015-02-24T13:26:19-0500\",\"v\":[4.7,528,0,0,359,131,38]},{\"d\":\"2015-03-02T15:25:17-0500\",\"v\":[4.7,534,0,0,362,132,40]},{\"d\":\"2015-03-10T10:42:38-0400\",\"v\":[4.8,615,0,0,415,159,41]},{\"d\":\"2015-03-12T12:06:53-0400\",\"v\":[4.8,614,0,0,421,148,45]},{\"d\":\"2015-03-13T12:14:41-0400\",\"v\":[4.8,614,0,0,416,147,51]},{\"d\":\"2015-03-16T10:46:31-0400\",\"v\":[4.8,619,0,0,422,147,50]},{\"d\":\"2015-03-17T12:41:11-0400\",\"v\":[4.8,619,0,0,423,146,50]},{\"d\":\"2015-03-18T10:10:50-0400\",\"v\":[4.8,617,0,1,422,143,51]},{\"d\":\"2015-03-26T09:49:38-0400\",\"v\":[4.8,615,0,0,421,143,51]},{\"d\":\"2015-03-27T15:31:39-0400\",\"v\":[4.8,615,0,0,421,143,51]},{\"d\":\"2015-03-30T15:46:07-0400\",\"v\":[4.8,615,0,0,421,143,51]},{\"d\":\"2015-03-31T07:19:26-0400\",\"v\":[4.8,621,0,0,426,143,52]},{\"d\":\"2015-04-01T10:16:27-0400\",\"v\":[4.8,624,3,0,426,143,52]},{\"d\":\"2015-04-02T18:11:55-0400\",\"v\":[4.8,602,0,0,419,130,53]},{\"d\":\"2015-04-07T05:55:53-0400\",\"v\":[4.8,596,0,1,414,128,53]},{\"d\":\"2015-04-08T03:44:37-0400\",\"v\":[4.8,599,0,1,417,128,53]}]}";

        // WHEN
        TimeMachine object = objectMapper.readValue(json, TimeMachine.class);

        // THEN
        List<MetricValue> metricValues = object.getHistory().entrySet().iterator().next().getValue();
        MetricValue classComplexity = MetricValue.builder().definition(class_complexity).value(4.7).build();
        assertThat(metricValues, hasItems(classComplexity));
    }
}
