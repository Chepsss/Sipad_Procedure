package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.DeclarationProcedureDTOResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.TbCeProcTransitoDTOResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.RoleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.UserDetailResponseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * A DTO for the {@link Procedure} entity.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProcedureDTO implements Serializable {

    private static long serialVersionUID = 6628683102848684876L;

    private Long id;
    private String codeProcess;
    private String bpmnProcessId;

    @NotNull
    private Long idAuthor;
    private String author;

    @NotNull
    private Long idAssignedTo;

    private Long numAttoSipad;

    private Integer annoAttoSipad;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime assignmentDate;

    @NotNull
    private Long employeeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime lastModifiedDate;

    private ProcedureEmployeeDTO employee;

    private UserDetailResponseDTO assignedTo;//chi avvia il procedimento

    private StateProcedureDTO stateProcedure;
    private FaseProcedureDTO faseProcedure;

    private TpCetipCessazioneDTO reasonCessation; // Motivo cessazione
    private Long openingCessationId;
    private Long typeCessationId;
    private String categLeaveReqId; // Categoria congedo richiesta/destinazione
    private Long catMilitareId; // Categoria personale militare

    private String authorityId;// id ente passato come corpo

    private String authorityId_cc1;// id ente cc1 passato come corpo
    private String authorityId_cc2;// id ente cc1 passato come corpo
    private String authorityId_cc3;// id ente cc1 passato come corpo


    private Long catDocumentoId;

    private String idCatPersSpettante;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime dataDocRichiesta;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime dataPresDocRich;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime dataDecorrenza;
    @NotNull
    private Boolean flVistoRagioneria;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime dataRaggEta;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime dataGml;
    private String organoSanita;
    private String modVerbaleGml;
    private String numVerbGml;

    private TpCegmlGiudMedLegaleDTO tpCegmlGiudMedLegale; // Tipologia GML di 1 istanza

    private String protIstanza;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime dataProtIstanza;
    private String protIstanzaPec;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime dataProtIstanzaPec;
    @NotNull
    private Boolean flImpugnaGml;
    @NotNull
    private Boolean flGmlConcordi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime dataGmlAppello;
    private String nVerbGmlAppello;
    private String protVerbGmlAppello;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime dataProtVerbGmlAppello;
    private String protVerbGmlAppPec;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime dataProtVerbGmlAppPec;

    private TpCegmlGiudMedLegaleDTO tpCegmlGiudMedLegale2; // Tipologia GML di 2 istanza

    private Long tpStproProvinciaId;
    private Long idComune;
    private String capRes;
    private String indirizzoRes;
    private Long idNazione;

    private String numVerbCommAv;

    private List<DeclarationProcedureDTOResponse> tpCeDichProcs;
    private TbCeProcTransitoDTOResponse tbCeProcTransito;
    private TbCeProcPensioneDTO tbCeProcPensione;
    private TbCeProcParereRagDTO tbCeProcParereRag;

    private LocalDateTime dataAvvio;
    private LocalDateTime dataScadLav;

    private String note;

    private Boolean canEdit;
    private Boolean canDelete;
    private String visibility;

    private Map<String, String> warningMessages;
    private List<RoleDTO> roles;

    //stato giuridico
    private String stgceFlagElaborato;
    private String stgceElabMsg;

    private Integer priorita;
    private Boolean canModifyPriority = false;

    private Boolean promTitOnor;

}
