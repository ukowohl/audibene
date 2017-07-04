package de.ukowohl.audibene.exception;

public class CustomerNotFoundException extends NotFoundException {

    private CustomerNotFoundException(String customerId) {
        super(customerId, "customer");
    }

    public static CustomerNotFoundException fromId(String customerId) {
        return new CustomerNotFoundException(customerId);
    }

}
