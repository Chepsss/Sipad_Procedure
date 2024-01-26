package it.almaviva.difesa.cessazione.procedure.model.dto.sipad.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class VwDo007ProcedimentiCriteria implements Serializable {

    private static final long serialVersionUID = 4336224688157913183L;

    private String do007CodFisc;
    private String do007PrproId;
    private String do007PrproCodPro;
    private LocalDate do007DataAvvio;
    private LocalDate do007DataFine;
    private String do007Fase;
    private String do007Stato;
    private String do007TipoProc;
    private String do007Autore;
    private Boolean isClosed;

}
