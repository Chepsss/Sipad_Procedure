package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class DocGenerationOutput implements Serializable {

    private static final long serialVersionUID = 1292034795729390310L;

    private Boolean error = false;
    private String name;
    private ArrayList<String> placeholderErrors;

}
