package de.ukowohl.audibene.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.ukowohl.audibene.domain.Employee;
import de.ukowohl.audibene.utils.domain.EntitySupplier;
import de.ukowohl.audibene.utils.web.Json;
import org.hibernate.validator.constraints.NotEmpty;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.util.function.Function;

@Json
@Immutable
@JsonSerialize(as = ImmutableEmployeeDto.class)
@JsonDeserialize(as = ImmutableEmployeeDto.class)
public interface EmployeeDto extends EntitySupplier<Employee> {

    Function<Employee, EmployeeDto> FROM_ENTITY = employee -> ImmutableEmployeeDto.of(employee.getId(), employee.getName());

    @Parameter
    String getId();

    @NotEmpty
    @Parameter
    String getName();

    @Override
    default Employee get() {
        return Employee.builder()
                .id(getId())
                .name(getName())
                .build();
    }
}
