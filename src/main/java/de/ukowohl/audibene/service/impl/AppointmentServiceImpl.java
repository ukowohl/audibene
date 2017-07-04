package de.ukowohl.audibene.service.impl;

import de.ukowohl.audibene.controller.dto.RatingDto;
import de.ukowohl.audibene.domain.Appointment;
import de.ukowohl.audibene.exception.AppointmentNotFoundException;
import de.ukowohl.audibene.repository.AppointmentRepository;
import de.ukowohl.audibene.service.AppointmentService;
import de.ukowohl.audibene.service.support.CrudServiceSupport;
import de.ukowohl.audibene.utils.Mappers;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.function.Function.identity;
import static lombok.AccessLevel.PROTECTED;

@Service
@Transactional
public class AppointmentServiceImpl extends CrudServiceSupport<Appointment, String> implements AppointmentService {

    @Getter(PROTECTED)
    protected final AppointmentRepository repository;

    protected Supplier<OffsetDateTime> now = OffsetDateTime::now;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Supplier<EntityNotFoundException> notFoundById(String s) {
        return () -> AppointmentNotFoundException.fromId(s);
    }

    @Override
    public <T> Page<T> findInRange(Pageable pageable, Function<Appointment, T> mapper, OffsetDateTime begin, OffsetDateTime end, String employeeId) {
        return Mappers.map(repository.findAppointmentByBeginsAfterAndEndsBeforeAndEmployeeId(begin, end, employeeId, pageable), mapper);
    }

    @Override
    public void updateRating(String customerId, String appointmentId, RatingDto request) {
        Appointment appointment = Optional.ofNullable(repository.findByIdAndCustomerId(appointmentId, customerId))
                .orElseThrow(notFoundById(appointmentId));
        update(appointment.getId(), request, identity());
    }

    @Override
    public <T> T getLastByCustomerId(String customerId, Function<Appointment, T> mapper) {
        return Optional.ofNullable(repository.findFirstByCustomerIdAndEndsBeforeOrderByEndsDesc(customerId, now.get()))
                .map(mapper)
                .orElseThrow(notFoundById(customerId));
    }

    @Override
    public <T> T getNextByCustomerId(String customerId, Function<Appointment, T> mapper) {
        return Optional.ofNullable(repository.findFirstByCustomerIdAndBeginsAfterOrderByBeginsAsc(customerId, now.get()))
                .map(mapper)
                .orElseThrow(notFoundById(customerId));
    }
}

