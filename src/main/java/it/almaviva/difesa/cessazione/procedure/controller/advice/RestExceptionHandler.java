package it.almaviva.difesa.cessazione.procedure.controller.advice;

import it.almaviva.difesa.cessazione.procedure.exception.InvalidTokenException;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.common.CustomError;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String message = "Method in Argument is not valid";
        List<CustomError> globalErrors = new ArrayList<>();
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String field = error.getField();
            errors.put(messageSource.getMessage(field, null, field, Locale.getDefault()), error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            CustomError err = new CustomError();
            err.setObjectName(error.getObjectName());
            err.setMessageError(error.getDefaultMessage());

            globalErrors.add(err);

        }
        RestApiError apiError = new RestApiError(HttpStatus.BAD_REQUEST, message, errors, globalErrors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        CustomError error = new CustomError();
        error.setMessageError(ex.getParameterName() + " parameter is missing");

        RestApiError apiError = new RestApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), List.of(error));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        List<CustomError> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            CustomError error = new CustomError();
            error.setObjectName(violation.getRootBeanClass().getName());
            error.setPropertyPath(violation.getPropertyPath());
            error.setMessageError(violation.getMessage());

            errors.add(error);
        }

        RestApiError apiError = new RestApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        CustomError error = new CustomError();
        error.setMessageError(ex.getName() + " should be of type " + ex.getRequiredType().getName());

        RestApiError apiError = new RestApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), List.of(error));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex) {
        String error = "Authentication error";
        CustomError err = new CustomError();
        err.setMessageError(String.join(",", ex.getErrors()));
        RestApiError apiError = new RestApiError(HttpStatus.UNAUTHORIZED, error, err, ex);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        String error = ex.getLocalizedMessage();
        CustomError err = new CustomError();
        err.setMessageError(String.join(",", ex.getLocalizedMessage()));
        RestApiError apiError = new RestApiError(HttpStatus.NOT_FOUND, error, err, ex);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<Object> handleServiceException(ServiceException ex) {
        String message = new String(ex.getLocalizedMessage().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        RestApiError apiError = new RestApiError(ex.getStatus(), message);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /*@ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<RestApiError> handleWebClientException(WebClientResponseException e) {
        CustomError err = new CustomError();
        var message = e.getResponseBodyAsString();
        int startIndex = StringUtils.ordinalIndexOf(message, "\"", 7) + 1;
        int endIndex = StringUtils.ordinalIndexOf(message, ",", 2) - 1;
        err.setMessageError(message.substring(startIndex, endIndex));
        RestApiError apiError = new RestApiError(e.getStatusCode(), message, err, e);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }*/

}
