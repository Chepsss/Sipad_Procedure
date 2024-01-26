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
public class TpPrattAttivazioneDTO implements Serializable {

    private static final long serialVersionUID = 5711442162850896685L;

    private Long id;
    private String prattDescrAtt;
    private String prattAcrAtt;
    private LocalDate prattDataIniz;
    private LocalDate prattDataFine;

}
