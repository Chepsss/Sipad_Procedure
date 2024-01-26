package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.model.common.GenericRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TbCeProcPensioneDTO extends GenericRequest implements Serializable {

    private static final long serialVersionUID = -7838672393128608504L;

    private String modCompilazione = Constant.PENSION_MOD_COMPILAZIONE_MANUALE;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protAttoPersomil;
    private Integer anniServizioEff;
    private Integer mesiServizioEff;
    private Integer giorniServizioEff;
    private Integer etaDipendente;
    private Integer anniAnzContr;
    private Integer mesiAnzContr;
    private Integer giorniAnzContr;
    private LocalDate dataAggAnzContr;
    private LocalDate dataMatReqMinimo;
    private Integer mesiIncrSperanzaV;
    private Integer mesiFinMobile;
    private LocalDate dataDirittoTratt;

}
