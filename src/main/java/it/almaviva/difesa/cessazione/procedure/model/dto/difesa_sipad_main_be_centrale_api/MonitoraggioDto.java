package it.almaviva.difesa.cessazione.procedure.model.dto.difesa_sipad_main_be_centrale_api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitoraggioDto {

    private String classeProcedimento;
    private String codiceFiscale;
    private String faseProcedimento;
    private Long idProcedimento;
    private Long idProcedimentoEscluso;
    private Long idProcedimentoPadre;
    private Long idVerticale;
    private Long idVerticaleEscluso;
    private String statoLavorazione;
    private String statoProcedimento;

}
