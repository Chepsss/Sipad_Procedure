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
public class TpStnazNazioneDTO implements Serializable {

    private static final long serialVersionUID = 6165809639860808801L;

    private Long id;
    private String stnazCodNazione;
    private String stnazCodIstat;
    private String stnazAcrNazione;
    private String stnazDescrNazione;
    private LocalDate stnazDataIniz;
    private LocalDate stnazDataFine;

}
