package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

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
public class TpCegmlGiudMedLegaleDTO implements Serializable {

    private static final long serialVersionUID = 1937473603320639132L;

    private String id;
    private String cegmlDescrizAbbrGml;
    private String cegmlDescrizioneGml;
    private LocalDate cegmlDataIniz;
    private LocalDate cegmlDataFine;
    private String cegmlCodAsp;
    private String cegmlNumIstanza;

}
