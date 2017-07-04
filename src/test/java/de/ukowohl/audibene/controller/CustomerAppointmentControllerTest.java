package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.controller.dto.AppointmentDto;
import de.ukowohl.audibene.controller.dto.ImmutableAppointmentDto;
import de.ukowohl.audibene.controller.dto.ImmutableRatingDto;
import de.ukowohl.audibene.domain.enumeration.Rating;
import de.ukowohl.audibene.service.AppointmentService;
import de.ukowohl.audibene.utils.web.ContentTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static de.ukowohl.audibene.controller.dto.AppointmentDto.FROM_ENTITY;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerAppointmentController.class)
public class CustomerAppointmentControllerTest {

    public static final OffsetDateTime BEGIN = OffsetDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    public static final OffsetDateTime END = BEGIN.plusHours(1);
    public static final String APPOINTMENT_ID = "appointment-id";
    public static final String CUSTOMER_ID = "customer-id";
    public static final String EMPLOYEE_ID = "employee-id";
    public static final Rating RATING = Rating.ONE_STAR;
    public static final String CUSTOMER_COMMENT = "customer-comment";
    private final static AppointmentDto APPOINTMENT_DTO = ImmutableAppointmentDto.builder()
            .id(APPOINTMENT_ID)
            .customerId(CUSTOMER_ID)
            .employeeId(EMPLOYEE_ID)
            .begin(BEGIN)
            .end(END)
            .rating(RATING)
            .customerComment(CUSTOMER_COMMENT)
            .build();

    @MockBean
    private AppointmentService service;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldGetLast() throws Exception {
        when(service.getLastByCustomerId(any(), any())).thenReturn(APPOINTMENT_DTO);

        mvc.perform(get("/api/customers/{customer-id}/appointments/last", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(APPOINTMENT_ID)))
                .andExpect(jsonPath("customerId", is(CUSTOMER_ID)))
                .andExpect(jsonPath("employeeId", is(EMPLOYEE_ID)))
                .andExpect(jsonPath("rating", is(RATING.toString())))
                .andExpect(jsonPath("customerComment", is(CUSTOMER_COMMENT)))
                .andExpect(jsonPath("begin", is("2017-01-01T00:00:00Z")))
                .andExpect(jsonPath("end", is("2017-01-01T01:00:00Z")))
        ;

        verify(service).getLastByCustomerId(CUSTOMER_ID, FROM_ENTITY);
    }

    @Test
    public void shouldGetNext() throws Exception {
        when(service.getNextByCustomerId(any(), any())).thenReturn(APPOINTMENT_DTO);

        mvc.perform(get("/api/customers/{customer-id}/appointments/next", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(APPOINTMENT_ID)))
                .andExpect(jsonPath("customerId", is(CUSTOMER_ID)))
                .andExpect(jsonPath("employeeId", is(EMPLOYEE_ID)))
                .andExpect(jsonPath("rating", is(RATING.toString())))
                .andExpect(jsonPath("customerComment", is(CUSTOMER_COMMENT)))
                .andExpect(jsonPath("begin", is("2017-01-01T00:00:00Z")))
                .andExpect(jsonPath("end", is("2017-01-01T01:00:00Z")))
        ;
        verify(service).getNextByCustomerId(CUSTOMER_ID, FROM_ENTITY);
    }

    @Test
    public void shouldRate422() throws Exception {
        mvc.perform(put("/api/customers/{customer-id}/appointments/{appointment-id}/rating", CUSTOMER_ID, APPOINTMENT_ID)
                .contentType(ContentTypes.JSON)
                .content("{}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("violations[0].field", is("ratingDto.rating")))
                .andExpect(jsonPath("violations[0].message", is("may not be null")))
        ;
    }

    @Test
    public void shouldRate() throws Exception {
        mvc.perform(put("/api/customers/{customer-id}/appointments/{appointment-id}/rating", CUSTOMER_ID, APPOINTMENT_ID)
                .contentType(ContentTypes.JSON)
                .content("{\"rating\":\"ONE_STAR\", \"comment\":\"everything was perfect\"}"))
                .andExpect(status().isOk())
        ;
        verify(service).updateRating(CUSTOMER_ID, APPOINTMENT_ID, ImmutableRatingDto.of(RATING, "everything was perfect"));
    }
}
