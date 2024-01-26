package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TbStentEnteDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpDocatCdocumentoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrattAttivazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrtpoTprocedimentoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgctpCatpersonaleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgtpoPosizioneStatoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpStComComuneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpStnazNazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpStproProvinciaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProcedureCompleteDTO extends ProcedureDTO {

    private static final long serialVersionUID = -862670668032220767L;

    private TpPrattAttivazioneDTO openingCessation; // Modalità apertura cessazione
    private TpPrtpoTprocedimentoDTO typeCessation; // Tipologia accesso cessazione

    private TpSgtpoPosizioneStatoDTO categLeaveReq;
    private TpSgctpCatpersonaleDTO catMilitare;
    private TbStentEnteDTO authority;
    private TbStentEnteDTO authority_cc1;
    private TbStentEnteDTO authority_cc2;
    private TbStentEnteDTO authority_cc3;
    private TpDocatCdocumentoDTO catDocumento;
    private TpStproProvinciaDTO tpStproProvincia;
    private TpStComComuneDTO tpStcomComune;
    private TpStnazNazioneDTO tpStnazNazione;


    public ProcedureCompleteDTO(ProcedureDTO procedureDTO) {
        super();
        this.setId(procedureDTO.getId());
        this.setCodeProcess(procedureDTO.getCodeProcess());
        this.setBpmnProcessId(procedureDTO.getBpmnProcessId());
        this.setIdAuthor(procedureDTO.getIdAuthor());
        this.setAuthor(procedureDTO.getAuthor());
        this.setIdAssignedTo(procedureDTO.getIdAssignedTo());
        this.setNumAttoSipad(procedureDTO.getNumAttoSipad());
        this.setAnnoAttoSipad(procedureDTO.getAnnoAttoSipad());
        this.setAssignmentDate(procedureDTO.getAssignmentDate());
        this.setEmployeeId(procedureDTO.getEmployeeId());
        this.setLastModifiedDate(procedureDTO.getLastModifiedDate());
        this.setEmployee(procedureDTO.getEmployee());
        this.setAssignedTo(procedureDTO.getAssignedTo());
        this.setStateProcedure(procedureDTO.getStateProcedure());
        this.setFaseProcedure(procedureDTO.getFaseProcedure());
        this.setReasonCessation(procedureDTO.getReasonCessation());
        this.setDataDocRichiesta(procedureDTO.getDataDocRichiesta());
        this.setDataPresDocRich(procedureDTO.getDataPresDocRich());
        this.setDataDecorrenza(procedureDTO.getDataDecorrenza());
        this.setProtIstanza(procedureDTO.getProtIstanza());
        this.setDataProtIstanza(procedureDTO.getDataProtIstanza());
        this.setProtIstanzaPec(procedureDTO.getProtIstanzaPec());
        this.setDataProtIstanzaPec(procedureDTO.getDataProtIstanzaPec());
        this.setDataAvvio(procedureDTO.getDataAvvio());
        this.setDataScadLav(procedureDTO.getDataScadLav());
        this.setCatDocumentoId(procedureDTO.getCatDocumentoId());
        this.setTpStproProvinciaId(procedureDTO.getTpStproProvinciaId());
        this.setIdComune(procedureDTO.getIdComune());
        this.setIdNazione(procedureDTO.getIdNazione());
        this.setCapRes(procedureDTO.getCapRes());
        this.setIndirizzoRes(procedureDTO.getIndirizzoRes());
        this.setIdCatPersSpettante(procedureDTO.getIdCatPersSpettante());
        this.setFlVistoRagioneria(procedureDTO.getFlVistoRagioneria());
        this.setDataRaggEta(procedureDTO.getDataRaggEta());
        this.setDataGml(procedureDTO.getDataGml());
        this.setOrganoSanita(procedureDTO.getOrganoSanita());
        this.setModVerbaleGml(procedureDTO.getModVerbaleGml());
        this.setNumVerbGml(procedureDTO.getNumVerbGml());
        this.setTpCegmlGiudMedLegale(procedureDTO.getTpCegmlGiudMedLegale());
        this.setFlImpugnaGml(procedureDTO.getFlImpugnaGml());
        this.setFlGmlConcordi(procedureDTO.getFlGmlConcordi());
        this.setTpCegmlGiudMedLegale2(procedureDTO.getTpCegmlGiudMedLegale2());
        this.setTpCeDichProcs(procedureDTO.getTpCeDichProcs());
        this.setTbCeProcTransito(procedureDTO.getTbCeProcTransito());
        this.setTbCeProcPensione(procedureDTO.getTbCeProcPensione());
        this.setTbCeProcParereRag(procedureDTO.getTbCeProcParereRag());
        this.setRoles(procedureDTO.getRoles());
        this.setDataGmlAppello(procedureDTO.getDataGmlAppello());
        this.setNVerbGmlAppello(procedureDTO.getNVerbGmlAppello());
        this.setProtVerbGmlAppello(procedureDTO.getProtVerbGmlAppello());
        this.setDataProtVerbGmlAppello(procedureDTO.getDataProtVerbGmlAppello());
        this.setProtVerbGmlAppPec(procedureDTO.getProtVerbGmlAppPec());
        this.setDataProtVerbGmlAppPec(procedureDTO.getDataProtVerbGmlAppPec());
        this.setNumVerbCommAv(procedureDTO.getNumVerbCommAv());
        this.setNote(procedureDTO.getNote());
        this.setCanEdit(procedureDTO.getCanEdit());
        this.setCanDelete(procedureDTO.getCanDelete());
        this.setVisibility(procedureDTO.getVisibility());
        this.setWarningMessages(procedureDTO.getWarningMessages());
        this.setStgceFlagElaborato(procedureDTO.getStgceFlagElaborato());
        this.setStgceElabMsg(procedureDTO.getStgceElabMsg());
        this.setPriorita(procedureDTO.getPriorita());
        this.setCanModifyPriority(procedureDTO.getCanModifyPriority());
        this.setPromTitOnor(procedureDTO.getPromTitOnor());
    }

}
