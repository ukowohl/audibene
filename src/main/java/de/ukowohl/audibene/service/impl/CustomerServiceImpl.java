package de.ukowohl.audibene.service.impl;

import de.ukowohl.audibene.domain.Customer;
import de.ukowohl.audibene.exception.CustomerNotFoundException;
import de.ukowohl.audibene.repository.CustomerRepository;
import de.ukowohl.audibene.service.CustomerService;
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
public class CustomerServiceImpl extends CrudServiceSupport<Customer, String> implements CustomerService {

    @Getter(PROTECTED)
    protected final CustomerRepository repository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Supplier<EntityNotFoundException> notFoundById(String s) {
        return () -> CustomerNotFoundException.fromId(s);
    }
}
