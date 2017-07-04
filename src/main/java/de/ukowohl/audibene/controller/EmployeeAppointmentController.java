package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.controller.dto.AppointmentDto;
import de.ukowohl.audibene.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/employees/{employee-id}/appointments", produces = "application/json")
public class EmployeeAppointmentController {

    public static final OffsetDateTime MIN = OffsetDateTime.parse("1970-01-01T00:00:00Z");
    public static final OffsetDateTime MAX = OffsetDateTime.parse("2200-01-01T00:00:00Z");
    private final AppointmentService appointmentService;

    @Autowired
    public EmployeeAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    private static OffsetDateTime VALUE_OR_DEFAULT(OffsetDateTime value, OffsetDateTime defaultValue) {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    @GetMapping
    public Page<AppointmentDto> find(
            @PathVariable("employee-id") String employeeId,
            @RequestParam(value = "start", required = false) OffsetDateTime start,
            @RequestParam(value = "end", required = false) OffsetDateTime end,
            Pageable pageable
    ) {
        return appointmentService.findInRange(
                pageable,
                AppointmentDto.FROM_ENTITY,
                VALUE_OR_DEFAULT(start, MIN),
                VALUE_OR_DEFAULT(end, MAX),
                employeeId
        );
    }

}
