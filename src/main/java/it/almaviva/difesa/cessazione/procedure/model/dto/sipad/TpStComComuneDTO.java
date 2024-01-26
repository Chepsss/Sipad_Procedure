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
public class TpStComComuneDTO implements Serializable {

    private static final long serialVersionUID = 8259382486812839469L;

    private Long id;
    private String stcomCodComune;
    private String stcomCodIstat;
    private String stcomDescrComune;
    private String stcomAcrComune;
    private LocalDate stcomDataIniz;
    private LocalDate stcomDataFine;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TpStproProvinciaDTO stcomStpro;

}
