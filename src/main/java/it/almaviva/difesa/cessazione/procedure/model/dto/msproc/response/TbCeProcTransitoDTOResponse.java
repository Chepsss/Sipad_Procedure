package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.common.GenericRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TbCeProcTransitoDTOResponse extends GenericRequest implements Serializable {

    private static final long serialVersionUID = -6488322440711789972L;

    private Boolean flagIdoneoTransito;
    private Boolean flagPresentataIstanza;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataIstanza;
    private String protIstanza;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataProtIstanza;
    private String protDomandaPersomil;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataPrDomPersomil;
    private Boolean flagPresRinuncia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataPresRinuncia;
    private String protRinunciaCmd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataRinunciaCmd;
    private String protRinunciaPersomil;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataRinunciaPersomil;
    private Boolean flagNoautoriz;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataNoautPersociv;
    private String protNoautPersociv;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataPrNoautPersociv;
    private String protNoautPersomil;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataPrNoautPersomil;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataNotifica;
    private Boolean flagFirmaContratto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataFirmaContr;
    private String protFirmaPersociv;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataPrFirmaPersociv;
    private String protFirmaPersomil;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataPrFirmaPersomil;
    private Boolean flagNonfirmaSalute;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataGiorno151;
    private String contrDataMax266;
    private Long numGiorniErogati266;

}
