package plazadecomidas.tracing.configuration.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import plazadecomidas.tracing.adapters.driven.mongo.exception.DocumentNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleDocumentNotFoundException(DocumentNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND.toString(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
