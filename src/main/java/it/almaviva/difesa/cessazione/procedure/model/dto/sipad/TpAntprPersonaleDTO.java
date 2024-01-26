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
public class TpAntprPersonaleDTO implements Serializable {

    private static final long serialVersionUID = -4057627759592890972L;

    private Long id;
    private String antprDescrTpr;
    private String antprAcrTpr;
    private LocalDate antprDataIniz;
    private LocalDate antprDataFine;
    private Character antprFlagEsposiz;

}
