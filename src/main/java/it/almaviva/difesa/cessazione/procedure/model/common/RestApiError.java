package it.almaviva.difesa.cessazione.procedure.model.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestApiError {

    private HttpStatus status;
    private int statusCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DD_MM_YYYY_HH_MM_SS)
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private Map<String, String> errors;
    private List<CustomError> globalErrors;

    private RestApiError() {
        timestamp = LocalDateTime.now();
    }

    public RestApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public RestApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public RestApiError(HttpStatus status, String message) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
    }

    public RestApiError(HttpStatus status, String message, List<CustomError> globalErrors, Throwable ex) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
        this.globalErrors = globalErrors;
    }

    public RestApiError(HttpStatus status, String message, List<CustomError> globalErrors) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.globalErrors = globalErrors;
    }

    public RestApiError(HttpStatus status, String message, Map<String, String> errors) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.errors = errors;
    }

    public RestApiError(HttpStatus status, String message, Map<String, String> errors, List<CustomError> globalErrors) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.errors = errors;
        this.globalErrors = globalErrors;
    }

    public RestApiError(HttpStatus status, String message, CustomError error, Throwable ex) {
        this();
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
        this.globalErrors = List.of(error);
    }

    public String convertToJson() throws JsonProcessingException {
        if (this == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper.writeValueAsString(this);
    }

}