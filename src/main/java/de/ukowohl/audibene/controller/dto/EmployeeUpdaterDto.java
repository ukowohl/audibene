package de.ukowohl.audibene.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.ukowohl.audibene.domain.Employee;
import de.ukowohl.audibene.utils.domain.EntityUpdater;
import org.hibernate.validator.constraints.NotEmpty;
import org.immutables.value.Value.Parameter;

import static org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = ImmutableEmployeeUpdaterDto.class)
@JsonDeserialize(as = ImmutableEmployeeUpdaterDto.class)
public interface EmployeeUpdaterDto extends EntityUpdater<Employee> {

    @NotEmpty
    @Parameter
    String getName();

    @Override
    default void accept(Employee employee) {
        employee.setName(getName());
    }
}
