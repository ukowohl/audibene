package de.ukowohl.audibene.repository;

import de.ukowohl.audibene.configuration.JpaConfiguration;
import de.ukowohl.audibene.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@Import(JpaConfiguration.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    public void shouldPersistAndLoadEntity() throws Exception {
        Customer customer = repository.saveAndFlush(Customer.builder()
                .name("name")
                .build());

        List<Customer> result = repository.findAll();

        assertThat(result, contains(customer));
    }
}