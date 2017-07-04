package de.ukowohl.audibene.configuration;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.ukowohl.audibene.utils.web.OffsetDateTimeFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.zalando.problem.ProblemModule;

@Configuration
@EnableSpringDataWebSupport
@Import({Jdk8Module.class, ProblemModule.class, JavaTimeModule.class})
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

//    @Bean
//    public ObjectMapper objectMapper() {
//        return Jackson2ObjectMapperBuilder.json()
//                .modules(new Jdk8Module(), new ProblemModule(), new JavaTimeModule())
//                .dateFormat(new ISO8601DateFormat())
//                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                .build();
//    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new OffsetDateTimeFormatter());
    }

}
