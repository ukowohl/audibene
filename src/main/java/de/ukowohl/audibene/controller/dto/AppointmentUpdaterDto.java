package de.ukowohl.audibene.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.ukowohl.audibene.domain.Appointment;
import de.ukowohl.audibene.utils.domain.EntityUpdater;

import static org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableAppointmentUpdaterDto.class)
@JsonDeserialize(as = ImmutableAppointmentUpdaterDto.class)
public interface AppointmentUpdaterDto extends EntityUpdater<Appointment> {


    @Override
    default void accept(Appointment appointment) {
        throw new UnsupportedOperationException(); //TODO
    }
}
