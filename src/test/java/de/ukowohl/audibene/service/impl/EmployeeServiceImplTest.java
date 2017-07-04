package de.ukowohl.audibene.service.impl;

import de.ukowohl.audibene.domain.Employee;
import de.ukowohl.audibene.repository.EmployeeRepository;
import lombok.Getter;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static lombok.AccessLevel.PROTECTED;

@Getter(PROTECTED)
@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest extends CrudServiceSupportTest<EmployeeServiceImpl, EmployeeRepository, Employee, String> {

    @Mock
    protected EmployeeRepository repository;

    @InjectMocks
    protected EmployeeServiceImpl service;

    protected String id = "employee-id";

}