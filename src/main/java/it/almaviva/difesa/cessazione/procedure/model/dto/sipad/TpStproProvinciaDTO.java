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
public class TpStproProvinciaDTO implements Serializable {

    private static final long serialVersionUID = -3964464367190495897L;

    private Long id;
    private String stproCodIstat;
    private String stproCodSigla;
    private String stproCDescrProvincia;
    private String stproAcrProvincia;
    private LocalDate stproDataIniz;
    private LocalDate stproDataFine;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TpStregRegioneDTO stproStreg;

}
