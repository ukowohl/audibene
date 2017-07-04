package de.ukowohl.audibene.exception;

public class AppointmentNotFoundException extends NotFoundException {

    private AppointmentNotFoundException(String appointmentId) {
        super(appointmentId, "appointment");
    }

    public static AppointmentNotFoundException fromId(String appointmentId) {
        return new AppointmentNotFoundException(appointmentId);
    }

}
