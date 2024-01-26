package it.almaviva.difesa.cessazione.procedure.model.dto.sipad;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class VwSg122StgiuridicoDsDTO implements Serializable {

    private static final long serialVersionUID = -5614247758024048312L;

    private Long idDip;
    private LocalDate dataArruolamento;
    private String idForzaArmata;
    private String descrForzaArmata;
    private String codGrado;
    private String descrGrado;
    private LocalDate dataDecGiuGrado;
    private String descrStatoGrado;
    private String descrCategoria;
    private String codRuolo;
    private String descrRuolo;
    private LocalDate dataDecGiuRuolo;
    private String descrStatoRuolo;
    private String codPosizione;
    private LocalDate dataDecGiuPosizione;
    private String descrPosizione;
    private String descrStatoPosizione;
    private String codTipArmaCorpo;
    private String descrTipoArmaCorpo;
    private LocalDate dataDecGiuImm;
    private LocalDate dataImm;
    private Long idCatPersonale;
    private Long idCatPosStato;
    private String descrCatoPosStato;
    private LocalDate dataIniTipPosStato;
    private LocalDate decorEconRuolo;
    private LocalDate decorEconGrado;
    private LocalDate dataDescrPosser;
    private String idPosser;
    private String descrPosser;
    private Long idCtpCatPersonale;
    private LocalDate decgiuCatoPosStato;

}
