package de.ukowohl.audibene.repository;

import de.ukowohl.audibene.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Transactional(propagation = MANDATORY)
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    Page<Appointment> findAppointmentByBeginsAfterAndEndsBeforeAndEmployeeId(OffsetDateTime begin, OffsetDateTime end, String employeeId, Pageable pageable);

    Appointment findByIdAndCustomerId(String id, String customerId);

    Appointment findFirstByCustomerIdAndEndsBeforeOrderByEndsDesc(String customerId, OffsetDateTime now);

    Appointment findFirstByCustomerIdAndBeginsAfterOrderByBeginsAsc(String customerId, OffsetDateTime now);
}
