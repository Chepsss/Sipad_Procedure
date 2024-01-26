package it.almaviva.difesa.cessazione.procedure.model.dto.sipad;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TpSgctpCatpersonaleDTO implements Serializable {

    private static final long serialVersionUID = 6300470392383292632L;

    private Long id;
    private String sgctpCodCatpers;
    private String sgctpDescrCatpers;
    private String sgctpAcrCatpers;
    private LocalDate sgctpDataIniz;
    private LocalDate sgctpDataFine;
    private Character sgctpFlagEsposiz;
    private String sgctpCodUid;
    private Long sgctpNumOrd;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TpAntprPersonaleDTO sgctpAntpr;

}
