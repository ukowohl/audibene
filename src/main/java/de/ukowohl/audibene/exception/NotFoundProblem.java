package de.ukowohl.audibene.exception;

import org.zalando.problem.AbstractThrowableProblem;

import javax.annotation.concurrent.Immutable;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Immutable
public class NotFoundProblem extends AbstractThrowableProblem {

    private static final String TYPE = "https://httpstatuses.com/404";

    private final String id;

    public NotFoundProblem(final String id, final String ressourceType) {
        super(
                URI.create(TYPE),
                String.format("%s not found", ressourceType),
                NOT_FOUND,
                String.format("%s %s not found", ressourceType, id)
        );
        this.id = id;
    }


}
