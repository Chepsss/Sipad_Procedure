package it.almaviva.difesa.cessazione.procedure.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.Path;
import java.io.Serializable;

@Data
public class CustomError implements Serializable {

    private static final long serialVersionUID = 6466697495333719130L;

    private String field;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String label;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String objectName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Path propertyPath;
    private String messageError;

}