package de.ukowohl.audibene.utils.web;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.immutables.value.Value.Style;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.immutables.value.Value.Style.ValidationMethod.NONE;

@Style(
        jdkOnly = true,
        depluralize = true,
        validationMethod = NONE,
        forceJacksonPropertyNames = false
)
@Target(TYPE)
@Retention(RUNTIME)
@JacksonAnnotation
@JacksonAnnotationsInside
@JsonInclude(NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public @interface Json {
}
