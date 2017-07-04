package de.ukowohl.audibene.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.ukowohl.audibene.domain.Customer;
import de.ukowohl.audibene.utils.domain.EntityUpdater;
import org.hibernate.validator.constraints.NotEmpty;
import org.immutables.value.Value.Parameter;

import static org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableCustomerUpdateDataDto.class)
@JsonDeserialize(as = ImmutableCustomerUpdateDataDto.class)
public interface CustomerUpdateDataDto extends EntityUpdater<Customer> {

    @NotEmpty
    @Parameter
    String getName();

    @Override
    default void accept(Customer customer) {
        customer.setName(getName());
    }
}
