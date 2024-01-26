package it.almaviva.difesa.cessazione.procedure.model.common;

import lombok.Data;

import java.util.Map;

@Data
public class RestApiErrorResponse {

    private String globalError;
    private Map<String, String> errors;

}
