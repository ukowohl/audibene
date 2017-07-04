package de.ukowohl.audibene.service;

import de.ukowohl.audibene.controller.dto.RatingDto;
import de.ukowohl.audibene.domain.Appointment;
import de.ukowohl.audibene.service.support.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.function.Function;

public interface AppointmentService extends CrudService<Appointment, String> {

    <T> Page<T> findInRange(Pageable pageable, Function<Appointment, T> mapper, OffsetDateTime begin, OffsetDateTime end, String employeeId);

    void updateRating(String customerId, String appointmentId, RatingDto request);

    <T> T getLastByCustomerId(String customerId, Function<Appointment, T> mapper);

    <T> T getNextByCustomerId(String customerId, Function<Appointment, T> mapper);
}
