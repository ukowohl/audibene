package de.ukowohl.audibene.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.ukowohl.audibene.domain.Appointment;
import de.ukowohl.audibene.utils.domain.EntityUpdater;
import de.ukowohl.audibene.domain.enumeration.Rating;
import de.ukowohl.audibene.utils.web.Json;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Json
@Immutable
@JsonSerialize(as = ImmutableRatingDto.class)
@JsonDeserialize(as = ImmutableRatingDto.class)
public interface RatingDto extends EntityUpdater<Appointment> {

    @NotNull
    @Parameter
    Rating getRating();

    @Parameter
    @Size(max = 8192)
    String getComment();

    @Override
    default void accept(Appointment appointment) {
        appointment.setRating(getRating());
        appointment.setCustomerComment(getComment());
    }
}
