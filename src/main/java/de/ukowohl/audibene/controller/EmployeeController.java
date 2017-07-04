package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.controller.dto.EmployeeDto;
import de.ukowohl.audibene.controller.dto.EmployeeUpdaterDto;
import de.ukowohl.audibene.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static de.ukowohl.audibene.controller.dto.EmployeeDto.FROM_ENTITY;
import static de.ukowohl.audibene.utils.web.ContentTypes.JSON;

@RestController
@RequestMapping(value = "/api/employees", produces = JSON)
public class EmployeeController {

    private final EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public Page<EmployeeDto> findAll(Pageable pageable) {
        return service.findAll(pageable, FROM_ENTITY);
    }

    @GetMapping(value = "/{employee-id}")
    public EmployeeDto findOne(@PathVariable("employee-id") String employeeId) {
        return service.getById(employeeId, FROM_ENTITY);
    }

    @PutMapping(consumes = JSON)
    public EmployeeDto create(@Valid @RequestBody EmployeeDto employeeDto) {
        return service.create(employeeDto, FROM_ENTITY);
    }

    @DeleteMapping(value = "/{employee-id}")
    public void deleteOne(@PathVariable("employee-id") String employeeId) {
        service.delete(employeeId);
    }

    @PutMapping(value = "/{employee-id}", consumes = JSON)
    public EmployeeDto update(
            @PathVariable("employee-id") String employeeId,
            @Valid @RequestBody EmployeeUpdaterDto updater
    ) {
        return service.update(employeeId, updater, FROM_ENTITY);
    }
}
