package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.model.common.GenericRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class TbCeProcTransitoDTORequest extends GenericRequest implements Serializable {

    private static final long serialVersionUID = -6488322440711789972L;

    private Long idProc;
    @NotNull
    private Long employeeId;
    private Long idCatPers;
    private Boolean flagIdoneoTransito;
    private Boolean flagPresentataIstanza;
    private LocalDateTime dataIstanza;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protIstanza;
    private LocalDateTime dataProtIstanza;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protDomandaPersomil;
    private LocalDateTime dataPrDomPersomil;
    private Boolean flagPresRinuncia;
    private LocalDateTime dataPresRinuncia;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protRinunciaCmd;
    private LocalDateTime dataRinunciaCmd;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protRinunciaPersomil;
    private LocalDateTime dataRinunciaPersomil;
    private Boolean flagNoautoriz;
    private LocalDateTime dataNoautPersociv;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protNoautPersociv;
    private LocalDateTime dataPrNoautPersociv;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protNoautPersomil;
    private LocalDateTime dataPrNoautPersomil;
    private LocalDateTime dataNotifica;
    private Boolean flagFirmaContratto;
    private LocalDateTime dataFirmaContr;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protFirmaPersociv;
    private LocalDateTime dataPrFirmaPersociv;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protFirmaPersomil;
    private LocalDateTime dataPrFirmaPersomil;
    private Boolean flagNonfirmaSalute;
    private LocalDateTime dataGiorno151;
    private String contrDataMax266;
    private Long numGiorniErogati266;

}
