package de.ukowohl.audibene.repository;

import de.ukowohl.audibene.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Transactional(propagation = MANDATORY)
public interface CustomerRepository extends JpaRepository<Customer, String> {
}
