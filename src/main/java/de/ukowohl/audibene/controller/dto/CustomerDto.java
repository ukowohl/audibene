package de.ukowohl.audibene.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.ukowohl.audibene.domain.Customer;
import de.ukowohl.audibene.utils.domain.EntitySupplier;
import de.ukowohl.audibene.utils.web.Json;
import org.hibernate.validator.constraints.NotEmpty;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.util.function.Function;

@Json
@Immutable
@JsonSerialize(as = ImmutableCustomerDto.class)
@JsonDeserialize(as = ImmutableCustomerDto.class)
public interface CustomerDto extends EntitySupplier<Customer> {

    Function<Customer, CustomerDto> FROM_ENTITY = customer -> ImmutableCustomerDto.of(customer.getId(), customer.getName());

    @Parameter
    String getId();

    @NotEmpty
    @Parameter
    String getName();

    @Override
    default Customer get() {
        return Customer.builder()
                .id(getId())
                .name(getName())
                .build();
    }

}
