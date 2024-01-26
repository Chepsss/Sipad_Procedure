package it.almaviva.difesa.cessazione.procedure.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestApiResponse extends RestApiErrorResponse {

    private int statusCode;
    private String message;
    private String warning;

}
