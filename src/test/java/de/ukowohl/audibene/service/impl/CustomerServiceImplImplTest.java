package de.ukowohl.audibene.service.impl;

import de.ukowohl.audibene.domain.Customer;
import de.ukowohl.audibene.repository.CustomerRepository;
import lombok.Getter;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static lombok.AccessLevel.PROTECTED;

@Getter(PROTECTED)
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplImplTest extends CrudServiceSupportTest<CustomerServiceImpl, CustomerRepository, Customer, String> {

    protected final String id = "customer-id";
    @Mock
    protected CustomerRepository repository;
    @InjectMocks
    protected CustomerServiceImpl service;
}