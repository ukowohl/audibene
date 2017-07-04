package de.ukowohl.audibene.controller;

import de.ukowohl.audibene.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
public class DefaultControllerAdvice implements ProblemHandling {

    @ExceptionHandler
    public ResponseEntity<Problem> entityNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(e.getProblem(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

}
