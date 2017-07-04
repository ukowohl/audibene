package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.configuration.WebMvcConfiguration;
import de.ukowohl.audibene.controller.dto.AppointmentDto;
import de.ukowohl.audibene.controller.dto.ImmutableAppointmentDto;
import de.ukowohl.audibene.domain.enumeration.Rating;
import de.ukowohl.audibene.service.AppointmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeAppointmentController.class)
@Import(WebMvcConfiguration.class)
public class EmployeeAppointmentControllerTest {

    public static final OffsetDateTime TIME = OffsetDateTime.parse("2017-07-03T21:11:27.166Z");
    public static final String ID = "id";
    public static final String CUSTOMER_ID = "customer-id";
    public static final String EMPLOYEE_ID = "employee-id";
    public static final String COMMENT = "comment";
    public static final Rating RATING = Rating.ONE_STAR;
    public static final ImmutableAppointmentDto APPOINTMENT_DTO = ImmutableAppointmentDto.builder()
            .id(ID)
            .customerId(CUSTOMER_ID)
            .employeeId(EMPLOYEE_ID)
            .begin(TIME)
            .end(TIME)
            .rating(RATING)
            .customerComment(COMMENT)
            .build();
    public static final PageRequest PAGEABLE = new PageRequest(0, 10);
    public static final PageImpl<Object> PAGE = new PageImpl<>(Collections.singletonList(APPOINTMENT_DTO), PAGEABLE, 1);

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        when(appointmentService.findInRange(any(), any(), any(), any(), any())).thenReturn(PAGE);
    }

    @Test
    public void shouldLookForEmployeeTourAppointments() throws Exception {
        mvc.perform(get("/api/employees/{employee-id}/appointments", EMPLOYEE_ID)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id", is(ID)))
                .andExpect(jsonPath("content[0].customerId", is(CUSTOMER_ID)))
                .andExpect(jsonPath("content[0].employeeId", is(EMPLOYEE_ID)))
                .andExpect(jsonPath("content[0].begin", is(TIME.toString())))
                .andExpect(jsonPath("content[0].end", is(TIME.toString())))
                .andExpect(jsonPath("content[0].rating", is("ONE_STAR")))
                .andExpect(jsonPath("content[0].customerComment", is(COMMENT)))
        ;

        verify(appointmentService).findInRange(PAGEABLE, AppointmentDto.FROM_ENTITY, OffsetDateTime.parse("1970-01-01T00:00Z"), OffsetDateTime.parse("2200-01-01T00:00Z"), EMPLOYEE_ID);
    }

    @Test
    public void shouldLookForEmployeeTourAppointmentsInRange() throws Exception {
        mvc.perform(get("/api/employees/{employee-id}/appointments", EMPLOYEE_ID)
                .param("page", "0")
                .param("size", "10")
                .param("start", TIME.toString())
                .param("end", TIME.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id", is(ID)))
                .andExpect(jsonPath("content[0].customerId", is(CUSTOMER_ID)))
                .andExpect(jsonPath("content[0].employeeId", is(EMPLOYEE_ID)))
                .andExpect(jsonPath("content[0].begin", is(TIME.toString())))
                .andExpect(jsonPath("content[0].end", is(TIME.toString())))
                .andExpect(jsonPath("content[0].rating", is("ONE_STAR")))
                .andExpect(jsonPath("content[0].customerComment", is(COMMENT)))
        ;

        verify(appointmentService).findInRange(PAGEABLE, AppointmentDto.FROM_ENTITY, TIME, TIME, EMPLOYEE_ID);
    }
}