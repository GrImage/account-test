package it.grimage.accounttest.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.grimage.accounttest.client.fabrick.FabrickError;
import it.grimage.accounttest.client.fabrick.FabrickResponse;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AccountServiceExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> handleFabrickException(FabrickException ex, WebRequest request) {
        FabrickResponse response = ex.getResponse();
        if (response != null) {
            List<FabrickError> errors = response.getErrors();
            log.error("Error response received while calling {}, errors {}", ex.getCallId(), errors);
        } else {
            log.error("Unknown error response from call {}", ex.getCallId());
        }

        return handleExceptionInternal(ex, response, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
