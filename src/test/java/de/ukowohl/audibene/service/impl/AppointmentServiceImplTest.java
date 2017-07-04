package de.ukowohl.audibene.service.impl;

import de.ukowohl.audibene.controller.dto.ImmutableRatingDto;
import de.ukowohl.audibene.domain.Appointment;
import de.ukowohl.audibene.repository.AppointmentRepository;
import de.ukowohl.audibene.service.AppointmentService;
import lombok.Getter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.function.Function;

import static de.ukowohl.audibene.domain.enumeration.Rating.ONE_STAR;
import static java.util.function.Function.identity;
import static lombok.AccessLevel.PROTECTED;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Getter(PROTECTED)
@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceImplTest extends CrudServiceSupportTest<AppointmentService, AppointmentRepository, Appointment, String> {
    public static final OffsetDateTime START = OffsetDateTime.MIN;
    public static final OffsetDateTime END = OffsetDateTime.MAX;
    public static final String CUSTOMER_COMMENT = "comment";
    public static final String CUSTOMER_ID = "customer-id";
    public static final OffsetDateTime NOW = OffsetDateTime.now();
    public static final String EMPLOYEE_ID = "employee-id";
    protected String id = "appointment-id";

    @Mock
    protected AppointmentRepository repository;

    @InjectMocks
    protected AppointmentServiceImpl service;

    @Mock
    protected Appointment entity;

    @Mock
    protected Appointment modifiedEntity;

    private Function<Appointment, Appointment> typedMapper = appointment -> modifiedEntity;
    private Pageable pageable = new PageRequest(0, 10);

    @Before
    public void setUp() throws Exception {
        service.now = () -> NOW;

        when(entity.getId()).thenReturn(id);
        when(repository.getOne(id)).thenReturn(entity);
    }

    @Test
    public void shouldFindAllInRange() throws Exception {
        PageImpl<Appointment> expected = new PageImpl<>(Collections.singletonList(entity), pageable, 1);

        when(repository.findAppointmentByBeginsAfterAndEndsBeforeAndEmployeeId(any(), any(), any(), any())).thenReturn(expected);

        Page<Appointment> result = service.findInRange(pageable, identity(), START, END, EMPLOYEE_ID);

        assertThat(result, is(expected));

        verify(repository).findAppointmentByBeginsAfterAndEndsBeforeAndEmployeeId(START, END, EMPLOYEE_ID, pageable);
    }

    @Test
    public void shouldUpdateRating() throws Exception {
        when(repository.findByIdAndCustomerId(id, CUSTOMER_ID)).thenReturn(entity);

        service.updateRating(CUSTOMER_ID, getId(), ImmutableRatingDto.of(ONE_STAR, CUSTOMER_COMMENT));

        verify(entity).setRating(ONE_STAR);
        verify(entity).setCustomerComment(CUSTOMER_COMMENT);
        verify(repository).saveAndFlush(entity);
    }

    @Test
    public void shouldGetLastByCustomerId() throws Exception {
        when(repository.findFirstByCustomerIdAndEndsBeforeOrderByEndsDesc(CUSTOMER_ID, NOW)).thenReturn(entity);

        assertThat(service.getLastByCustomerId(CUSTOMER_ID, typedMapper), is(modifiedEntity));
    }

    @Test
    public void shouldGetFirstByCustomerId() throws Exception {
        when(repository.findFirstByCustomerIdAndBeginsAfterOrderByBeginsAsc(CUSTOMER_ID, NOW)).thenReturn(entity);

        Appointment result = service.getNextByCustomerId(CUSTOMER_ID, typedMapper);

        assertThat(result, is(modifiedEntity));
    }
}