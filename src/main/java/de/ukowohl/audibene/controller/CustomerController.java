package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.controller.dto.CustomerDto;
import de.ukowohl.audibene.controller.dto.CustomerUpdateDataDto;
import de.ukowohl.audibene.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static de.ukowohl.audibene.controller.dto.CustomerDto.FROM_ENTITY;
import static de.ukowohl.audibene.utils.web.ContentTypes.JSON;

@RestController
@RequestMapping(value = "/api/customers", produces = JSON)
public class CustomerController {

    private final CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public Page<CustomerDto> findAll(Pageable pageable) {
        return service.findAll(pageable, FROM_ENTITY);
    }

    @GetMapping("/{customer-id}")
    public CustomerDto findOne(@PathVariable("customer-id") String customerId) {
        return service.getById(customerId, FROM_ENTITY);
    }

    @PostMapping(consumes = JSON)
    public CustomerDto create(@Valid @RequestBody CustomerDto request) {
        return service.create(request, FROM_ENTITY);
    }

    @DeleteMapping("/{customer-id}")
    public void deleteOne(@PathVariable("customer-id") String customerId) {
        service.delete(customerId);
    }

    @PutMapping(value = "/{customer-id}", consumes = JSON)
    public CustomerDto update(
            @PathVariable("customer-id") String customerId,
            @Valid @RequestBody CustomerUpdateDataDto updater) {
        return service.update(customerId, updater, FROM_ENTITY);
    }
}
