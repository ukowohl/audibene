package de.ukowohl.audibene.exception;

import lombok.AllArgsConstructor;

import javax.persistence.EntityNotFoundException;

@AllArgsConstructor
public abstract class NotFoundException extends EntityNotFoundException {

    private String id;
    private String resourceType;

    public NotFoundProblem getProblem() {
        return new NotFoundProblem(id, resourceType);
    }

}
