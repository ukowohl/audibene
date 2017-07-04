package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.controller.dto.AppointmentDto;
import de.ukowohl.audibene.controller.dto.AppointmentUpdaterDto;
import de.ukowohl.audibene.service.AppointmentService;
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

import static de.ukowohl.audibene.controller.dto.AppointmentDto.FROM_ENTITY;
import static de.ukowohl.audibene.utils.web.ContentTypes.JSON;

@RestController
@RequestMapping(value = "/api/appointments", produces = JSON)
public class AppointmentController {

    private final AppointmentService service;

    @Autowired
    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping
    public Page<AppointmentDto> findAll(Pageable pageable) {
        return service.findAll(pageable, FROM_ENTITY);
    }

    @GetMapping("/{appointment-id}")
    public AppointmentDto findOne(@PathVariable("appointment-id") String appointmentId) {
        return service.getById(appointmentId, FROM_ENTITY);
    }

    @PutMapping(consumes = JSON)
    public AppointmentDto create(@Valid @RequestBody AppointmentDto request) {
        return service.create(request, FROM_ENTITY);
    }

    @DeleteMapping("/{appointment-id}")
    public void deleteOne(@PathVariable("appointment-id") String appointmentId) {
        service.delete(appointmentId);
    }

    @PutMapping(value = "/{appointment-id}", consumes = JSON)
    public AppointmentDto update(
            @PathVariable("appointment-id") String appointmentId,
            @Valid @RequestBody AppointmentUpdaterDto updater
    ) {
        return service.update(appointmentId, updater, FROM_ENTITY);
    }

}
