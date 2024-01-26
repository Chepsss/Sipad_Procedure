package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DocEsterniList implements Serializable {

    private static final long serialVersionUID = -1995498945859307133L;

    List<DocEsternoDTO> documenti = new ArrayList<>();

}
