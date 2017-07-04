package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.controller.dto.CustomerDto;
import de.ukowohl.audibene.controller.dto.ImmutableCustomerDto;
import de.ukowohl.audibene.exception.CustomerNotFoundException;
import de.ukowohl.audibene.service.CustomerService;
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

import static de.ukowohl.audibene.controller.dto.CustomerDto.FROM_ENTITY;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    public static final String CUSTOMER_ID = "123";
    public static final String CUSTOMER_NAME = "customer-name";
    public static final ImmutableCustomerDto CUSTOMER_DTO = ImmutableCustomerDto.of(CUSTOMER_ID, CUSTOMER_NAME);
    public static final ImmutableCustomerDto CUSTOMER_CREATE = ImmutableCustomerDto.builder().name(CUSTOMER_NAME).build();

    @MockBean
    private CustomerService service;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldNotFoundById() throws Exception {
        when(service.getById(any(), any())).thenThrow(CustomerNotFoundException.fromId("123"));

        mvc.perform(get("/api/customers/{customer-id}", CUSTOMER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.type", is("https://httpstatuses.com/404")))
                .andExpect(jsonPath("$.title", is("customer not found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("customer 123 not found")))
        ;

        verify(service).getById(CUSTOMER_ID, FROM_ENTITY);
    }

    @Test
    public void shouldGetById() throws Exception {
        when(service.getById(any(), any())).thenReturn(CUSTOMER_DTO);

        mvc.perform(get("/api/customers/{customer-id}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("id", is(CUSTOMER_ID)))
                .andExpect(jsonPath("name", is(CUSTOMER_NAME)))
        ;

        verify(service).getById(CUSTOMER_ID, FROM_ENTITY);
    }

    @Test
    public void shouldDelete() throws Exception {
        mvc.perform(delete("/api/customers/{customer-id}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]));

        verify(service).delete(CUSTOMER_ID);
    }

    @Test
    public void shouldFindAll() throws Exception {
        Pageable pageable = new PageRequest(0, 10);
        Page<CustomerDto> page = new PageImpl<>(Collections.singletonList(CUSTOMER_DTO), pageable, 1);
        when(service.findAll(pageable, FROM_ENTITY)).thenReturn(page);

        mvc.perform(get("/api/customers")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("content[0].id", is(CUSTOMER_ID)))
                .andExpect(jsonPath("content[0].name", is(CUSTOMER_NAME)))
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
        when(service.create(any(), any())).thenReturn(CUSTOMER_DTO);

        mvc.perform(post("/api/customers")
                .content("{\"name\": \"customer-name\"}")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("id", is(CUSTOMER_ID)))
                .andExpect(jsonPath("name", is(CUSTOMER_NAME)))
        ;

        verify(service).create(CUSTOMER_CREATE, FROM_ENTITY);
    }

    @Test
    public void shouldCreate422() throws Exception {
        mvc.perform(post("/api/customers")
                .content("{}")
                .contentType("application/json"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("violations[0].field", is("customerDto.name")))
                .andExpect(jsonPath("violations[0].message", is("may not be empty")))
                .andExpect(jsonPath("title", is("Constraint Violation")))
        ;
    }
}