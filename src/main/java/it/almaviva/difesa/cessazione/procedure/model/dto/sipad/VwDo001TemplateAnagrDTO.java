package it.almaviva.difesa.cessazione.procedure.model.dto.sipad;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class VwDo001TemplateAnagrDTO implements Serializable {

    private static final long serialVersionUID = -3263886529088454781L;

    private Long idDip;
    private String descrCogn;
    private String descrNome;
    private LocalDate dataNasc;
    private String codFsc;
    private Long idNasc;
    private String descrComuneNasc;
    private String siglaProv;
    private String codCatPers;
    private String descrCatPers;
    private String descrPosServ;
    private Long codStatoGiu;
    private String descrStatoGiu;
    private String descrGrado;
    private String descrRuolo;
    private String codFfaa;
    private String codArmaCorpo;
    private String descrArmaCorpo;
    private String codMatricola;
    private LocalDate dataArr;

}
