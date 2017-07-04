package de.ukowohl.audibene.repository;

import de.ukowohl.audibene.configuration.JpaConfiguration;
import de.ukowohl.audibene.domain.Appointment;
import de.ukowohl.audibene.domain.Customer;
import de.ukowohl.audibene.domain.Employee;
import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static de.ukowohl.audibene.domain.enumeration.Rating.ONE_STAR;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import(JpaConfiguration.class)
public class AppointmentRepositoryTest {

    public static final OffsetDateTime BEGINS = OffsetDateTime.now();
    public static final OffsetDateTime ENDS = BEGINS.plusHours(1);
    public static final PageRequest PAGE_REQUEST = new PageRequest(0, 10);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Customer customer;
    private Employee employee;

    private AppointmentFactory factory;

    @Before
    public void setUp() throws Exception {
        customer = customerRepository.saveAndFlush(Customer.builder()
                .name("name")
                .build());
        employee = employeeRepository.saveAndFlush(Employee.builder()
                .name("name")
                .build());

        factory = new AppointmentFactory(customer, employee);
    }

    @Test
    public void shouldPersistAndLoadEntity() throws Exception {
        Appointment appointment = factory.produce(BEGINS, ENDS);

        List<Appointment> result = appointmentRepository.findAll();

        assertThat(result, contains(appointment));
    }

    @Test
    public void shouldFindInDateRange() throws Exception {
        Appointment appointment = factory.produce(BEGINS, ENDS);
        PageRequest pageable = new PageRequest(0, 10);
        Page<Appointment> expected = new PageImpl<>(Collections.singletonList(appointment), pageable, 1);

        Page<Appointment> result = appointmentRepository.findAppointmentByBeginsAfterAndEndsBeforeAndEmployeeId(BEGINS.minusDays(1), ENDS.plusDays(7), employee.getId(), pageable);

        assertThat(result, is(expected));
    }

    @Test
    public void shouldFindInDateRangeNoEmployee() throws Exception {
        factory.produce(BEGINS, ENDS);
        Page<Appointment> expected = new PageImpl<>(Collections.emptyList(), PAGE_REQUEST, 0);

        Page<Appointment> result = appointmentRepository.findAppointmentByBeginsAfterAndEndsBeforeAndEmployeeId(BEGINS.minusDays(1), ENDS.plusDays(7), "different-id", PAGE_REQUEST);

        assertThat(result, is(expected));
    }

    @Test
    public void shouldFindNothingInRange() throws Exception {
        factory.produce(BEGINS, ENDS);
        Page<Appointment> expected = new PageImpl<>(Collections.emptyList(), PAGE_REQUEST, 0);

        Page<Appointment> result = appointmentRepository.findAppointmentByBeginsAfterAndEndsBeforeAndEmployeeId(BEGINS.minusDays(7), ENDS.minusDays(1), employee.getId(), PAGE_REQUEST);

        assertThat(result, is(expected));
    }

    @Test
    public void shouldFindLastAppointment() throws Exception {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime oldestAppointmentStart = now.minusDays(2).minusHours(1);
        OffsetDateTime oldestAppointmentEnd = now.minusDays(1);
        OffsetDateTime newerAppointmentStart = now.minusDays(1).minusHours(1);
        OffsetDateTime newerAppointmentEnd = now.minusDays(1);

        Appointment expected = factory.produce(newerAppointmentStart, newerAppointmentEnd);
        factory.produce(oldestAppointmentStart, oldestAppointmentEnd);

        Appointment result = appointmentRepository.findFirstByCustomerIdAndEndsBeforeOrderByEndsDesc(customer.getId(), now);
        assertThat(result, is(expected));
    }

    @Test
    public void shouldFindNextAppointment() throws Exception {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime farerAppointmentStart = now.plusDays(2).minusHours(1);
        OffsetDateTime farerAppointmentEnd = now.plusDays(1);
        OffsetDateTime earlierAppointmentStart = now.plusDays(1).minusHours(1);
        OffsetDateTime earlierAppointmentEnd = now.plusDays(1);

        Appointment expected = factory.produce(earlierAppointmentStart, earlierAppointmentEnd);
        factory.produce(farerAppointmentStart, farerAppointmentEnd);

        Appointment result = appointmentRepository.findFirstByCustomerIdAndBeginsAfterOrderByBeginsAsc(customer.getId(), now);
        assertThat(result, is(expected));
    }

    @Test
    public void shouldFindByIdAndCustomerId() throws Exception {
        Appointment expected = factory.produce(BEGINS, ENDS);

        Appointment result = appointmentRepository.findByIdAndCustomerId(expected.getId(), customer.getId());

        assertThat(result, is(expected));
    }

    @AllArgsConstructor
    class AppointmentFactory {

        private Customer customer;
        private Employee employee;

        Appointment produce(OffsetDateTime start, OffsetDateTime end) {
            return appointmentRepository.saveAndFlush(Appointment.builder()
                    .customerId(customer.getId())
                    .employeeId(employee.getId())
                    .begins(start)
                    .ends(end)
                    .rating(ONE_STAR)
                    .build());
        }
    }
}