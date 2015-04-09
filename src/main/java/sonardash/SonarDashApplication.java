package sonardash;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.ALL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@SpringBootApplication
@EnableConfigurationProperties(SonarQubeProperties.class)
public class SonarDashApplication {

    public static void main(String[] args) {
        SpringApplication.run(SonarDashApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper() //
                .enable(WRITE_DATES_AS_TIMESTAMPS) //
                .enable(INDENT_OUTPUT) //
                .disable(FAIL_ON_UNKNOWN_PROPERTIES) //
                .setVisibility(ALL, NONE) //
                .setVisibility(FIELD, ANY) //
                .setSerializationInclusion(NON_NULL) //
                .registerModule(new JodaModule());
    }
}
