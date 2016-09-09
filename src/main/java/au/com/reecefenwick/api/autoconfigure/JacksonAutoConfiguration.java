package au.com.reecefenwick.api.autoconfigure;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;

import static au.com.reecefenwick.api.EnvironmentConstants.SPRING_PROFILE_DEVELOPMENT;
import static java.util.Arrays.asList;

@Configuration
public class JacksonAutoConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder(
        List<Jackson2ObjectMapperBuilderCustomizer> customizers) {
        Jackson2ObjectMapperBuilder builder = configureObjectMapper();
        customize(builder, customizers);
        return builder;
    }

    private void customize(Jackson2ObjectMapperBuilder builder,
                           List<Jackson2ObjectMapperBuilderCustomizer> customizers) {
        for (Jackson2ObjectMapperBuilderCustomizer customizer : customizers) {
            customizer.customize(builder);
        }
    }

    private Jackson2ObjectMapperBuilder configureObjectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        List<String> activeProfiles = asList(env.getActiveProfiles());
        if (activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT)) {
            builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
        }
        return builder;
    }
}
