package de.ukowohl.audibene.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.ukowohl.audibene.domain.Appointment;
import de.ukowohl.audibene.utils.domain.EntitySupplier;
import de.ukowohl.audibene.domain.enumeration.Rating;
import de.ukowohl.audibene.utils.web.Json;
import org.hibernate.validator.constraints.NotEmpty;
import org.immutables.value.Value.Immutable;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.function.Function;


@Json
@Immutable
@JsonSerialize(as = ImmutableAppointmentDto.class)
@JsonDeserialize(as = ImmutableAppointmentDto.class)
public interface AppointmentDto extends EntitySupplier<Appointment> {

    Function<Appointment, AppointmentDto> FROM_ENTITY = appointment -> ImmutableAppointmentDto.builder()
            .id(appointment.getId())
            .customerId(appointment.getCustomerId())
            .employeeId(appointment.getEmployeeId())
            .begin(appointment.getBegins())
            .end(appointment.getEnds())
            .rating(appointment.getRating())
            .customerComment(appointment.getCustomerComment())
            .build();

    @Nullable
    String getId();

    @NotEmpty
    String getCustomerId();

    @NotEmpty
    String getEmployeeId();

    @NotNull
    OffsetDateTime getBegin();

    @NotNull
    OffsetDateTime getEnd();

    @Nullable
    Rating getRating();

    @Nullable
    String getCustomerComment();

    @Override
    default Appointment get() {
        return Appointment.builder()
                .customerId(getCustomerId())
                .employeeId(getEmployeeId())
                .begins(getBegin())
                .ends(getEnd())
                .rating(getRating())
                .customerComment(getCustomerComment())
                .build();
    }

}
