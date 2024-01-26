package it.almaviva.difesa.cessazione.procedure.model.dto.difesa_sipad_main_be_centrale_api;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcedimentoQueue {

    private String codiceFiscaleAutoreProcedimento;
    private String codiceFiscaleUltimoAggiornamento;
    private String codiceFiscaleUltimoInserimento;
    private String codiceFiscaleUtente;
    private String codiceProcedimento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD)
    private LocalDate dataAvvioProcedimento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD)
    private LocalDate dataFineProcedimento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD)
    private LocalDate dataInserimento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD)
    private LocalDate dataUltimaModifica;

    private String faseProcedimento;
    private Long idProcedimento;
    private String nomeVerticale;
    private String statoProcedimento;
    private String tipoProcedimento;
    private Long idProcedimentoPadre;

}
