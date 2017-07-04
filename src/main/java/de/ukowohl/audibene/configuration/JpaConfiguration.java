package de.ukowohl.audibene.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EntityScan("de.ukowohl.audibene.domain")
@EnableJpaRepositories("de.ukowohl.audibene.repository")
public class JpaConfiguration {
}
