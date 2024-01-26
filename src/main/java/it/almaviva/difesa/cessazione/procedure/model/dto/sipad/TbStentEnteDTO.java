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
public class TbStentEnteDTO implements Serializable {

    private static final long serialVersionUID = 5119316030311902645L;

    private String id;
    private String stentDenomEnte;
    private String stentDenomBreve;
    private String stentMail;
    private String stentIndDenom;
    private String stentIndCap;
    private String stentIndTelegrafico;
    private String stentTelEnte;
    private String stentAcrEnte;
    private LocalDate stentDataFirma;
    private LocalDate stentDataAttuazione;
    private String stentDescrLocalita;
    private TpStfaaForzaArmataDTO stentStfaa;
    private Long stentStcomId;
    private String stentCodPiva;
    private Long stentStnazId;
    private Long stentSttseId;
    private Long stentStorgId;
    private String stentStaooCod;
    private Long stentStoprId;
    private Long stentStoprIdOrd;
    private String stentCodIpa;
    private String stentMailPec;
    private LocalDate stentDataIniz;
    private LocalDate stentDataFine;

}
