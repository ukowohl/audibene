package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.controller.dto.EmployeeDto;
import de.ukowohl.audibene.controller.dto.ImmutableEmployeeDto;
import de.ukowohl.audibene.exception.EmployeeNotFoundException;
import de.ukowohl.audibene.service.EmployeeService;
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

import java.util.Collections;

import static de.ukowohl.audibene.controller.dto.EmployeeDto.FROM_ENTITY;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    public static final String EMPLOYEE_ID = "123";
    public static final String EMPLOYEE_NAME = "name";
    public static final ImmutableEmployeeDto EMPLOYEE_DTO = ImmutableEmployeeDto.of(EMPLOYEE_ID, EMPLOYEE_NAME);
    public static final ImmutableEmployeeDto EMPLOYEE_CREATE = ImmutableEmployeeDto.builder().name(EMPLOYEE_NAME).build();

    @MockBean
    private EmployeeService service;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldNotFoundById() throws Exception {
        when(service.getById(any(), any())).thenThrow(EmployeeNotFoundException.fromId(EMPLOYEE_ID));

        mvc.perform(get("/api/employees/{employee-id}", EMPLOYEE_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.type", is("https://httpstatuses.com/404")))
                .andExpect(jsonPath("$.title", is("employee not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("employee 123 not found")))
        ;
        verify(service).getById(EMPLOYEE_ID, FROM_ENTITY);
    }

    @Test
    public void shouldGetById() throws Exception {
        when(service.getById(any(), any())).thenReturn(EMPLOYEE_DTO);

        mvc.perform(get("/api/employees/{employee-id}", EMPLOYEE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("id", is(EMPLOYEE_ID)))
        ;
    }

    @Test
    public void shouldDelete() throws Exception {
        mvc.perform(delete("/api/employees/{employee-id}", EMPLOYEE_ID))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]));

        verify(service).delete(EMPLOYEE_ID);
    }

    @Test
    public void shouldFindAll() throws Exception {
        Pageable pageable = new PageRequest(0, 10);
        Page<EmployeeDto> page = new PageImpl<>(Collections.singletonList(EMPLOYEE_DTO), pageable, 1);

        when(service.findAll(pageable, FROM_ENTITY)).thenReturn(page);

        mvc.perform(get("/api/employees")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("content[0].id", is(EMPLOYEE_ID)))
                .andExpect(jsonPath("totalElements", is(1)))
                .andExpect(jsonPath("last", is(true)))
                .andExpect(jsonPath("totalPages", is(1)))
                .andExpect(jsonPath("size", is(10)))
                .andExpect(jsonPath("number", is(0)))
                .andExpect(jsonPath("first", is(true)))
                .andExpect(jsonPath("numberOfElements", is(1)))
        ;
    }

    @Test
    public void shouldCreate() throws Exception {
        when(service.create(any(), any())).thenReturn(EMPLOYEE_DTO);

        mvc.perform(put("/api/employees")
                .content("{\"name\":\"name\"}")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("id", is(EMPLOYEE_ID)))
        ;

        verify(service).create(EMPLOYEE_CREATE, FROM_ENTITY);
    }
}