package it.grimage.accounttest.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.grimage.accounttest.client.fabrick.FabrickError;
import it.grimage.accounttest.client.fabrick.FabrickResponse;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AccountServiceExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> handleFabrickException(FabrickException ex, WebRequest request) {
        FabrickResponse<?> response = ex.getResponse();
        if (response != null) {
            List<FabrickError> errors = response.getErrors();
            log.error("Error response received while calling {}, errors {}", ex.getCallId(), errors);
        } else {
            log.error("Unknown error response from call {}", ex.getCallId());
        }

        return handleExceptionInternal(ex, response, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleAccountServiceException(AccountServiceException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getError(), null, ex.getHttpStatus(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleConversionExceptions(MethodArgumentTypeMismatchException ex, WebRequest request) {
        // for some reason spring does not handle these by default
        Map<String, String> failure = ImmutableMap.of(
            ex.getName(),
            "Value could not be converted to required type " + ex.getRequiredType().getSimpleName());
        return handleExceptionInternal(ex, failure, null, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
            // let's provide a little more informations that spring does by default
            AccountError failure = new AccountError(
                null, 
                "Missing required parameter " + ex.getParameterName());
            return handleExceptionInternal(ex, failure, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
                List<String> messages = new ArrayList<>();
                for (ObjectError err : ex.getBindingResult().getGlobalErrors()) {
                    messages.add(err.getDefaultMessage());
                }
                for (FieldError err : ex.getBindingResult().getFieldErrors()) {
                    messages.add(err.getField() + ": " + err.getDefaultMessage());
                }
                AccountError failure = new AccountError(
                    null, 
                    messages.stream().collect(Collectors.joining("; ")));
        return super.handleExceptionInternal(ex, failure, headers, status, request);
    }
}
