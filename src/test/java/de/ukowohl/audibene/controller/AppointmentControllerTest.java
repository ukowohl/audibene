package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.controller.dto.AppointmentDto;
import de.ukowohl.audibene.controller.dto.ImmutableAppointmentDto;
import de.ukowohl.audibene.controller.dto.ImmutableAppointmentUpdaterDto;
import de.ukowohl.audibene.domain.enumeration.Rating;
import de.ukowohl.audibene.service.AppointmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static de.ukowohl.audibene.controller.dto.AppointmentDto.FROM_ENTITY;
import static de.ukowohl.audibene.utils.web.ContentTypes.JSON;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {


    public static final OffsetDateTime BEGIN = OffsetDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    public static final OffsetDateTime END = BEGIN.plusHours(1);
    public static final String APPOINTMENT_ID = "appointment-id";
    public static final String CUSTOMER_ID = "customer-id";
    public static final String EMPLOYEE_ID = "employee-id";
    public static final Rating RATING = Rating.ONE_STAR;
    public static final String CUSTOMER_COMMENT = "customer-comment";
    public static final Pageable PAGEABLE = new PageRequest(0, 10);
    private final static AppointmentDto APPOINTMENT_DTO = ImmutableAppointmentDto.builder()
            .id(APPOINTMENT_ID)
            .customerId(CUSTOMER_ID)
            .employeeId(EMPLOYEE_ID)
            .begin(BEGIN)
            .end(END)
            .rating(RATING)
            .customerComment(CUSTOMER_COMMENT)
            .build();
    public static final Page<AppointmentDto> PAGE = new PageImpl<>(singletonList(APPOINTMENT_DTO), PAGEABLE, 1);

    @MockBean
    private AppointmentService service;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldFindAll() throws Exception {
        when(service.findAll(PAGEABLE, FROM_ENTITY)).thenReturn(PAGE);

        mvc.perform(get("/api/appointments")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id", is(APPOINTMENT_ID)))
                .andExpect(jsonPath("content[0].customerId", is(CUSTOMER_ID)))
                .andExpect(jsonPath("content[0].employeeId", is(EMPLOYEE_ID)))
                .andExpect(jsonPath("content[0].begin", is("2017-01-01T00:00:00Z")))
                .andExpect(jsonPath("content[0].end", is("2017-01-01T01:00:00Z")))
                .andExpect(jsonPath("content[0].rating", is(RATING.toString())))
                .andExpect(jsonPath("content[0].customerComment", is(CUSTOMER_COMMENT)))
        ;
    }

    @Test
    public void shouldFindOne() throws Exception {
        when(service.getById(APPOINTMENT_ID, FROM_ENTITY)).thenReturn(APPOINTMENT_DTO);

        mvc.perform(get("/api/appointments/{appointment-id}", APPOINTMENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(APPOINTMENT_ID)))
                .andExpect(jsonPath("customerId", is(CUSTOMER_ID)))
                .andExpect(jsonPath("employeeId", is(EMPLOYEE_ID)))
                .andExpect(jsonPath("begin", is("2017-01-01T00:00:00Z")))
                .andExpect(jsonPath("end", is("2017-01-01T01:00:00Z")))
                .andExpect(jsonPath("rating", is(RATING.toString())))
                .andExpect(jsonPath("customerComment", is(CUSTOMER_COMMENT)))
        ;
    }

    @Test
    public void shouldDelete() throws Exception {
        mvc.perform(delete("/api/appointments/{appointment-id}", APPOINTMENT_ID))
                .andExpect(status().isOk())
        ;
        verify(service).delete(APPOINTMENT_ID);
    }

    @Test
    public void shouldUpdate() throws Exception {
        mvc.perform(put("/api/appointments/{appointment-id}", APPOINTMENT_ID)
                .content("{}")
                .contentType(JSON))
                .andExpect(status().isOk())
        ;

        verify(service).update(APPOINTMENT_ID, ImmutableAppointmentUpdaterDto.builder().build(), FROM_ENTITY);
    }

    @Test
    public void name() throws Exception {
        when(service.create(any(), any())).thenReturn(APPOINTMENT_DTO);

        mvc.perform(put("/api/appointments")
                .contentType(JSON)
                .content("{\"customerId\":\"customer-id\",\"employeeId\":\"employee-id\",\"begin\":\"2017-01-01T00:00:00Z\",\"end\":\"2017-01-01T01:00:00Z\"}"))
                .andExpect(jsonPath("id", is(APPOINTMENT_ID)))
                .andExpect(jsonPath("customerId", is(CUSTOMER_ID)))
                .andExpect(jsonPath("employeeId", is(EMPLOYEE_ID)))
                .andExpect(jsonPath("begin", is("2017-01-01T00:00:00Z")))
                .andExpect(jsonPath("end", is("2017-01-01T01:00:00Z")))
                .andExpect(jsonPath("rating", is(RATING.toString())))
                .andExpect(jsonPath("customerComment", is(CUSTOMER_COMMENT)))
        ;

        AppointmentDto expected = ImmutableAppointmentDto.builder()
                .customerId(CUSTOMER_ID)
                .employeeId(EMPLOYEE_ID)
                .begin(BEGIN)
                .end(END)
                .build();

        verify(service).create(expected, FROM_ENTITY);
    }
}