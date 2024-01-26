package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import it.almaviva.difesa.documenti.document.model.dto.response.titolario.TitolarioDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrecompilazioneTitolarioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private TitolarioDto titolario;
    private LocalDate dataCompilazione;

}
