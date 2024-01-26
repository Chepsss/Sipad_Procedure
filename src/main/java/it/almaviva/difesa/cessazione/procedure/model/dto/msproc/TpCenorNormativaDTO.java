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
public class TpCenorNormativaDTO implements Serializable {

    private static final long serialVersionUID = 3170717880591413954L;

    private Long id;
    private String cenorDescrNormativa;
    private String cenorNotaNormativa;
    private LocalDate cenorDataIniz;
    private String cenorDataFine;

}
