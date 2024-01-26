package it.almaviva.difesa.cessazione.procedure.model.dto.sipad;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TpDocatCdocumentoDTO implements Serializable {

    private static final long serialVersionUID = 4270486760217605345L;

    private Long id;
    private String docatDescrCat;
    private String docatAcrCat;
    private LocalDate docatDataIniz;
    private LocalDate docatDataFine;

}
