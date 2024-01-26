package it.almaviva.difesa.cessazione.procedure.model.dto.sipad;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TpStregRegioneDTO implements Serializable {

    private static final long serialVersionUID = 5414882880298811567L;

    private Long id;
    private String stregCodRegione;
    private String stregCodIstat;
    private String stregDescrRegione;
    private String stregAcrRegione;
    private LocalDate stregDataIniz;
    private LocalDate stregDataFine;

}
