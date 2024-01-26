package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeclarationDTO implements Serializable {

    private static final long serialVersionUID = 4503676206412730436L;

    private Long id;
    private String codice;
    private String descrizione;
    private Boolean flagAutomatico = false;
    private String codTipo;
    private String descrTipo;
    private LocalDate dataIniz;
    private LocalDate dataFine;

}
