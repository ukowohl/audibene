package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.controller.dto.AppointmentDto;
import de.ukowohl.audibene.controller.dto.RatingDto;
import de.ukowohl.audibene.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static de.ukowohl.audibene.controller.dto.AppointmentDto.FROM_ENTITY;

@RestController
@RequestMapping("/api/customers/{customer-id}/appointments")
public class CustomerAppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public CustomerAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/last")
    public AppointmentDto getLast(@PathVariable("customer-id") String customerId) {
        return appointmentService.getLastByCustomerId(customerId, FROM_ENTITY);
    }

    @GetMapping("/next")
    public AppointmentDto getNext(@PathVariable("customer-id") String customerId) {
        return appointmentService.getNextByCustomerId(customerId, FROM_ENTITY);

    }

    @PutMapping("/{appointment-id}/rating")
    public void rate(
            @PathVariable("customer-id") String customerId,
            @PathVariable("appointment-id") String appointmentId,
            @Valid @RequestBody RatingDto request
    ) {
        appointmentService.updateRating(customerId, appointmentId, request);
    }
}
