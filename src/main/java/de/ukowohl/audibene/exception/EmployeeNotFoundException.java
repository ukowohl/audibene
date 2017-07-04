package de.ukowohl.audibene.exception;

public class EmployeeNotFoundException extends NotFoundException {

    private EmployeeNotFoundException(String employeeId) {
        super(employeeId, "employee");
    }

    public static EmployeeNotFoundException fromId(String employeeId) {
        return new EmployeeNotFoundException(employeeId);
    }

}
