package de.ukowohl.audibene.service.impl;

import de.ukowohl.audibene.domain.Employee;
import de.ukowohl.audibene.exception.EmployeeNotFoundException;
import de.ukowohl.audibene.repository.EmployeeRepository;
import de.ukowohl.audibene.service.EmployeeService;
import de.ukowohl.audibene.service.support.CrudServiceSupport;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.function.Supplier;

import static lombok.AccessLevel.PROTECTED;

@Service
@Transactional
public class EmployeeServiceImpl extends CrudServiceSupport<Employee, String> implements EmployeeService {

    @Getter(PROTECTED)
    protected final EmployeeRepository repository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Supplier<EntityNotFoundException> notFoundById(String s) {
        return () -> EmployeeNotFoundException.fromId(s);
    }
}
