package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@SuperBuilder
public class ProceduresArchiveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codFisc;
    private Long prproId;
    private String prproCodPro;
    private LocalDate dataAvvio;
    private LocalDate dataFine;
    private String fase;
    private String stato;
    private String tipoProc;
    private String autore;

}
