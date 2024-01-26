package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.Declaration;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.DeclarationProcedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureHistory;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcParereRag;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcPensione;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcTransito;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCegmlGiudMedLegale;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCenorNormativa;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCetipCessazione;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.DeclarationDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureHistoryDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.StateProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TbCeProcParereRagDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TbCeProcPensioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCegmlGiudMedLegaleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCenorNormativaDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCetipCessazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.DeclarationProcedureDTOResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.TbCeProcTransitoDTOResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ProcedureHistoryMapper {

    public ProcedureHistory toEntity(ProcedureHistoryDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        ProcedureHistory procedureHistory = new ProcedureHistory();
        procedureHistory.setId(dto.getId());
        procedureHistory.setAssignmentDate(dto.getAssignmentDate());
        procedureHistory.setProcedure(procedureDTOToProcedure(dto.getProcedure()));
        procedureHistory.setStateProcedure(stateProcedureDTOToStateProcedure(dto.getStateProcedure()));
        procedureHistory.setRoleCode(dto.getRoleCode());
        procedureHistory.setFlagAttuale(dto.getFlagAttuale());
        return procedureHistory;
    }

    public List<ProcedureHistory> toEntity(List<ProcedureHistoryDTO> dtoList) {
        if (Objects.isNull(dtoList)) {
            return null;
        }
        List<ProcedureHistory> list = new ArrayList<>(dtoList.size());
        for (ProcedureHistoryDTO procedureHistoryDTO : dtoList) {
            list.add(toEntity(procedureHistoryDTO));
        }
        return list;
    }

    public List<ProcedureHistoryDTO> toDto(List<ProcedureHistory> entityList) {
        if (Objects.isNull(entityList)) {
            return null;
        }
        List<ProcedureHistoryDTO> list = new ArrayList<>(entityList.size());
        for (ProcedureHistory procedureHistory : entityList) {
            list.add(toDto(procedureHistory));
        }
        return list;
    }

    public ProcedureHistoryDTO toDto(ProcedureHistory procedureHistory) {
        if (Objects.isNull(procedureHistory)) {
            return null;
        }
        ProcedureDTO procedureDTO = toDtoProcedureId(procedureHistory.getProcedure());
        Long id = procedureHistory.getId();
        LocalDateTime assignmentDate = procedureHistory.getAssignmentDate();
        StateProcedureDTO stateProcedure = toDtoStateProcedureId(procedureHistory.getStateProcedure());
        String roleCode = procedureHistory.getRoleCode();
        Boolean flagAttuale = procedureHistory.getFlagAttuale();
        ProcedureHistoryDTO procedureHistoryDTO = new ProcedureHistoryDTO();
        procedureHistoryDTO.setId(id);
        procedureHistoryDTO.setAssignmentDate(assignmentDate);
        procedureHistoryDTO.setProcedure(procedureDTO);
        procedureHistoryDTO.setStateProcedure(stateProcedure);
        procedureHistoryDTO.setRoleCode(roleCode);
        procedureHistoryDTO.setFlagAttuale(flagAttuale);
        return procedureHistoryDTO;
    }

    public ProcedureDTO toDtoProcedureId(Procedure procedure) {
        if (Objects.isNull(procedure)) {
            return null;
        }
        ProcedureDTO procedureDTO = new ProcedureDTO();
        procedureDTO.setId(procedure.getId());
        return procedureDTO;
    }

    public StateProcedureDTO toDtoStateProcedureId(StateProcedure stateProcedure) {
        if (Objects.isNull(stateProcedure)) {
            return null;
        }
        StateProcedureDTO stateProcedureDTO = new StateProcedureDTO();
        stateProcedureDTO.setId(stateProcedure.getId());
        return stateProcedureDTO;
    }

    protected Declaration declarationDTOToDeclaration(DeclarationDTO declarationDTO) {
        if (Objects.isNull(declarationDTO)) {
            return null;
        }
        Declaration.DeclarationBuilder declaration = Declaration.builder();
        declaration.id(declarationDTO.getId());
        declaration.codice(declarationDTO.getCodice());
        declaration.descrizione(declarationDTO.getDescrizione());
        declaration.flagAutomatico(declarationDTO.getFlagAutomatico());
        declaration.codTipo(declarationDTO.getCodTipo());
        declaration.descrTipo(declarationDTO.getDescrTipo());
        declaration.dataIniz(declarationDTO.getDataIniz());
        declaration.dataFine(declarationDTO.getDataFine());
        return declaration.build();
    }

    protected DeclarationProcedure declarationProcedureDTOResponseToDeclarationProcedure(DeclarationProcedureDTOResponse declarationProcedureDTOResponse) {
        if (Objects.isNull(declarationProcedureDTOResponse)) {
            return null;
        }
        DeclarationProcedure.DeclarationProcedureBuilder declarationProcedure = DeclarationProcedure.builder();
        declarationProcedure.id(declarationProcedureDTOResponse.getId());
        declarationProcedure.idDich(declarationDTOToDeclaration(declarationProcedureDTOResponse.getIdDich()));
        declarationProcedure.flagDich(declarationProcedureDTOResponse.getFlagDich());
        if (Objects.nonNull(declarationProcedureDTOResponse.getDataDich())) {
            declarationProcedure.dataDich(LocalDateTime.ofInstant(declarationProcedureDTOResponse.getDataDich().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        return declarationProcedure.build();
    }

    protected List<DeclarationProcedure> declarationProcedureDTOResponseListToDeclarationProcedureList(List<DeclarationProcedureDTOResponse> list) {
        if (Objects.isNull(list)) {
            return null;
        }
        List<DeclarationProcedure> list1 = new ArrayList<>(list.size());
        for (DeclarationProcedureDTOResponse declarationProcedureDTOResponse : list) {
            list1.add(declarationProcedureDTOResponseToDeclarationProcedure(declarationProcedureDTOResponse));
        }
        return list1;
    }

    protected StateProcedure stateProcedureDTOToStateProcedure(StateProcedureDTO stateProcedureDTO) {
        if (Objects.isNull(stateProcedureDTO)) {
            return null;
        }
        StateProcedure stateProcedure = new StateProcedure();
        stateProcedure.setId(stateProcedureDTO.getId());
        stateProcedure.setCodeState(stateProcedureDTO.getCodeState());
        stateProcedure.setDescState(stateProcedureDTO.getDescState());
        return stateProcedure;
    }

    protected TpCenorNormativa tpCenorNormativaDTOToTpCenorNormativa(TpCenorNormativaDTO tpCenorNormativaDTO) {
        if (Objects.isNull(tpCenorNormativaDTO)) {
            return null;
        }
        TpCenorNormativa.TpCenorNormativaBuilder tpCenorNormativa = TpCenorNormativa.builder();
        tpCenorNormativa.id(tpCenorNormativaDTO.getId());
        tpCenorNormativa.cenorDescrNormativa(tpCenorNormativaDTO.getCenorDescrNormativa());
        tpCenorNormativa.cenorNotaNormativa(tpCenorNormativaDTO.getCenorNotaNormativa());
        tpCenorNormativa.cenorDataIniz(tpCenorNormativaDTO.getCenorDataIniz());
        if (Objects.nonNull(tpCenorNormativaDTO.getCenorDataFine())) {
            tpCenorNormativa.cenorDataFine(LocalDate.parse(tpCenorNormativaDTO.getCenorDataFine()));
        }
        return tpCenorNormativa.build();
    }

    protected TpCetipCessazione tpCetipCessazioneDTOToTpCetipCessazione(TpCetipCessazioneDTO tpCetipCessazioneDTO) {
        if (Objects.isNull(tpCetipCessazioneDTO)) {
            return null;
        }
        TpCetipCessazione.TpCetipCessazioneBuilder tpCetipCessazione = TpCetipCessazione.builder();
        tpCetipCessazione.id(tpCetipCessazioneDTO.getId());
        if (Objects.nonNull(tpCetipCessazioneDTO.getIdCetipSgctpSeqPk())) {
            tpCetipCessazione.cetipSgctpSeqPk(tpCetipCessazioneDTO.getIdCetipSgctpSeqPk());
        }
        if (Objects.nonNull(tpCetipCessazioneDTO.getIdCetipPrtpoSeqPk())) {

            tpCetipCessazione.cetipPrtpoSeqPk(tpCetipCessazioneDTO.getIdCetipPrtpoSeqPk());
        }
        tpCetipCessazione.cetipMotivoCessazione(tpCetipCessazioneDTO.getCetipMotivoCessazione());
        tpCetipCessazione.cetipCenorSeqPk(tpCenorNormativaDTOToTpCenorNormativa(tpCetipCessazioneDTO.getCetipCenorSeqPk()));
        tpCetipCessazione.cetipSgtpoSeqPk(tpCetipCessazioneDTO.getCetipSgtpoSeqPk());
        tpCetipCessazione.cetipAcrTiv(tpCetipCessazioneDTO.getCetipAcrTiv());
        tpCetipCessazione.idMotivo(tpCetipCessazioneDTO.getIdMotivo());
        return tpCetipCessazione.build();
    }

    protected TpCegmlGiudMedLegale tpCegmlGiudMedLegaleDTOToTpCegmlGiudMedLegale(TpCegmlGiudMedLegaleDTO tpCegmlGiudMedLegaleDTO) {
        if (Objects.isNull(tpCegmlGiudMedLegaleDTO)) {
            return null;
        }
        TpCegmlGiudMedLegale.TpCegmlGiudMedLegaleBuilder tpCegmlGiudMedLegale = TpCegmlGiudMedLegale.builder();
        tpCegmlGiudMedLegale.id(tpCegmlGiudMedLegaleDTO.getId());
        tpCegmlGiudMedLegale.cegmlDescrizAbbrGml(tpCegmlGiudMedLegaleDTO.getCegmlDescrizAbbrGml());
        tpCegmlGiudMedLegale.cegmlDescrizioneGml(tpCegmlGiudMedLegaleDTO.getCegmlDescrizioneGml());
        tpCegmlGiudMedLegale.cegmlDataIniz(tpCegmlGiudMedLegaleDTO.getCegmlDataIniz());
        tpCegmlGiudMedLegale.cegmlDataFine(tpCegmlGiudMedLegaleDTO.getCegmlDataFine());
        tpCegmlGiudMedLegale.cegmlCodAsp(tpCegmlGiudMedLegaleDTO.getCegmlCodAsp());
        tpCegmlGiudMedLegale.cegmlNumIstanza(tpCegmlGiudMedLegaleDTO.getCegmlNumIstanza());
        return tpCegmlGiudMedLegale.build();
    }

    protected TbCeProcTransito tbCeProcTransitoDTOResponseToTbCeProcTransito(TbCeProcTransitoDTOResponse tbCeProcTransitoDTOResponse) {
        if (Objects.isNull(tbCeProcTransitoDTOResponse)) {
            return null;
        }
        TbCeProcTransito.TbCeProcTransitoBuilder tbCeProcTransito = TbCeProcTransito.builder();
        tbCeProcTransito.flagIdoneoTransito(tbCeProcTransitoDTOResponse.getFlagIdoneoTransito());
        tbCeProcTransito.flagPresentataIstanza(tbCeProcTransitoDTOResponse.getFlagPresentataIstanza());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataIstanza())) {
            tbCeProcTransito.dataIstanza(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataIstanza().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.protIstanza(tbCeProcTransitoDTOResponse.getProtIstanza());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataProtIstanza())) {
            tbCeProcTransito.dataProtIstanza(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataProtIstanza().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.protDomandaPersomil(tbCeProcTransitoDTOResponse.getProtDomandaPersomil());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataPrDomPersomil())) {
            tbCeProcTransito.dataPrDomPersomil(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataPrDomPersomil().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.flagPresRinuncia(tbCeProcTransitoDTOResponse.getFlagPresRinuncia());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataPresRinuncia())) {
            tbCeProcTransito.dataPresRinuncia(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataPresRinuncia().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.protRinunciaCmd(tbCeProcTransitoDTOResponse.getProtRinunciaCmd());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataRinunciaCmd())) {
            tbCeProcTransito.dataRinunciaCmd(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataRinunciaCmd().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.protRinunciaPersomil(tbCeProcTransitoDTOResponse.getProtRinunciaPersomil());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataRinunciaPersomil())) {
            tbCeProcTransito.dataRinunciaPersomil(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataRinunciaPersomil().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.flagNoautoriz(tbCeProcTransitoDTOResponse.getFlagNoautoriz());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataNoautPersociv())) {
            tbCeProcTransito.dataNoautPersociv(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataNoautPersociv().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.protNoautPersociv(tbCeProcTransitoDTOResponse.getProtNoautPersociv());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataPrNoautPersociv())) {
            tbCeProcTransito.dataPrNoautPersociv(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataPrNoautPersociv().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.protNoautPersomil(tbCeProcTransitoDTOResponse.getProtNoautPersomil());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataPrNoautPersomil())) {
            tbCeProcTransito.dataPrNoautPersomil(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataPrNoautPersomil().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataNotifica())) {
            tbCeProcTransito.dataNotifica(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataNotifica().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.flagFirmaContratto(tbCeProcTransitoDTOResponse.getFlagFirmaContratto());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataFirmaContr())) {
            tbCeProcTransito.dataFirmaContr(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataFirmaContr().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.protFirmaPersociv(tbCeProcTransitoDTOResponse.getProtFirmaPersociv());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataPrFirmaPersociv())) {
            tbCeProcTransito.dataPrFirmaPersociv(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataPrFirmaPersociv().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.protFirmaPersomil(tbCeProcTransitoDTOResponse.getProtFirmaPersomil());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataPrFirmaPersomil())) {
            tbCeProcTransito.dataPrFirmaPersomil(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataPrFirmaPersomil().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.flagNonfirmaSalute(tbCeProcTransitoDTOResponse.getFlagNonfirmaSalute());
        if (Objects.nonNull(tbCeProcTransitoDTOResponse.getDataGiorno151())) {
            tbCeProcTransito.dataGiorno151(LocalDateTime.ofInstant(tbCeProcTransitoDTOResponse.getDataGiorno151().toInstant(), ZoneOffset.UTC).toLocalDate());
        }
        tbCeProcTransito.contrDataMax266(tbCeProcTransitoDTOResponse.getContrDataMax266());
        tbCeProcTransito.numGiorniErogati266(tbCeProcTransitoDTOResponse.getNumGiorniErogati266());
        return tbCeProcTransito.build();
    }

    protected TbCeProcPensione tbCeProcPensioneDTOToTbCeProcPensione(TbCeProcPensioneDTO tbCeProcPensioneDTO) {
        if (Objects.isNull(tbCeProcPensioneDTO)) {
            return null;
        }
        TbCeProcPensione.TbCeProcPensioneBuilder tbCeProcPensione = TbCeProcPensione.builder();
        tbCeProcPensione.modCompilazione(tbCeProcPensioneDTO.getModCompilazione());
        tbCeProcPensione.protAttoPersomil(tbCeProcPensioneDTO.getProtAttoPersomil());
        tbCeProcPensione.anniServizioEff(tbCeProcPensioneDTO.getAnniServizioEff());
        tbCeProcPensione.mesiServizioEff(tbCeProcPensioneDTO.getMesiServizioEff());
        tbCeProcPensione.giorniServizioEff(tbCeProcPensioneDTO.getGiorniServizioEff());
        tbCeProcPensione.etaDipendente(tbCeProcPensioneDTO.getEtaDipendente());
        tbCeProcPensione.anniAnzContr(tbCeProcPensioneDTO.getAnniAnzContr());
        tbCeProcPensione.mesiAnzContr(tbCeProcPensioneDTO.getMesiAnzContr());
        tbCeProcPensione.giorniAnzContr(tbCeProcPensioneDTO.getGiorniAnzContr());
        tbCeProcPensione.dataAggAnzContr(tbCeProcPensioneDTO.getDataAggAnzContr());
        tbCeProcPensione.dataMatReqMinimo(tbCeProcPensioneDTO.getDataMatReqMinimo());
        tbCeProcPensione.mesiIncrSperanzaV(tbCeProcPensioneDTO.getMesiIncrSperanzaV());
        tbCeProcPensione.mesiFinMobile(tbCeProcPensioneDTO.getMesiFinMobile());
        tbCeProcPensione.dataDirittoTratt(tbCeProcPensioneDTO.getDataDirittoTratt());
        return tbCeProcPensione.build();
    }

    protected TbCeProcParereRag tbCeProcParereRagDTOToTbCeProcParereRag(TbCeProcParereRagDTO tbCeProcParereRagDTO) {
        if (Objects.isNull(tbCeProcParereRagDTO)) {
            return null;
        }
        TbCeProcParereRag.TbCeProcParereRagBuilder tbCeProcParereRag = TbCeProcParereRag.builder();
        tbCeProcParereRag.esito(tbCeProcParereRagDTO.getEsito());
        tbCeProcParereRag.numRegistrazione(tbCeProcParereRagDTO.getNumRegistrazione());
        tbCeProcParereRag.dataEsito(tbCeProcParereRagDTO.getDataEsito());
        return tbCeProcParereRag.build();
    }

    protected Procedure procedureDTOToProcedure(ProcedureDTO procedureDTO) {
        if (Objects.isNull(procedureDTO)) {
            return null;
        }
        Procedure procedure = new Procedure();
        procedure.setLastModifiedDate(procedureDTO.getLastModifiedDate());
        procedure.setTpCeDichProcs(declarationProcedureDTOResponseListToDeclarationProcedureList(procedureDTO.getTpCeDichProcs()));
        procedure.setId(procedureDTO.getId());
        procedure.setCodeProcess(procedureDTO.getCodeProcess());
        procedure.setBpmnProcessId(procedureDTO.getBpmnProcessId());
        procedure.setIdAuthor(procedureDTO.getIdAuthor());
        procedure.setAuthor(procedureDTO.getAuthor());
        procedure.setAssignmentDate(procedureDTO.getAssignmentDate());
        procedure.setEmployeeId(procedureDTO.getEmployeeId());
        procedure.setStateProcedure(stateProcedureDTOToStateProcedure(procedureDTO.getStateProcedure()));
        procedure.setOpeningCessation((procedureDTO.getOpeningCessationId()));
        procedure.setTypeCessation((procedureDTO.getTypeCessationId()));
        procedure.setReasonCessation(tpCetipCessazioneDTOToTpCetipCessazione(procedureDTO.getReasonCessation()));
        procedure.setCategLeaveReq(procedureDTO.getCategLeaveReqId());
        procedure.setIdCatPersSpettante(procedureDTO.getIdCatPersSpettante());
        if (Objects.nonNull(procedureDTO.getCatMilitareId())) {
            procedure.setIdCatMilitare(procedureDTO.getCatMilitareId());
        }
        procedure.setCatDocumento(procedureDTO.getCatDocumentoId());
        procedure.setDataDocRichiesta(procedureDTO.getDataDocRichiesta());
        procedure.setDataPresDocRich(procedureDTO.getDataPresDocRich());
        procedure.setDataDecorrenza(procedureDTO.getDataDecorrenza());
        procedure.setFlVistoRagioneria(procedureDTO.getFlVistoRagioneria());
        procedure.setDataRaggEta(procedureDTO.getDataRaggEta());
        procedure.setDataGml(procedureDTO.getDataGml());
        procedure.setOrganoSanita(procedureDTO.getOrganoSanita());
        procedure.setModVerbaleGml(procedureDTO.getModVerbaleGml());
        procedure.setNumVerbGml(procedureDTO.getNumVerbGml());
        procedure.setTpCegmlGiudMedLegale(tpCegmlGiudMedLegaleDTOToTpCegmlGiudMedLegale(procedureDTO.getTpCegmlGiudMedLegale()));
        procedure.setProtIstanza(procedureDTO.getProtIstanza());
        procedure.setDataProtIstanza(procedureDTO.getDataProtIstanza());
        procedure.setProtIstanzaPec(procedureDTO.getProtIstanzaPec());
        procedure.setDataProtIstanzaPec(procedureDTO.getDataProtIstanzaPec());
        procedure.setFlImpugnaGml(procedureDTO.getFlImpugnaGml());
        procedure.setFlGmlConcordi(procedureDTO.getFlGmlConcordi());
        procedure.setDataGmlAppello(procedureDTO.getDataGmlAppello());
        procedure.setNVerbGmlAppello(procedureDTO.getNVerbGmlAppello());
        procedure.setProtVerbGmlAppello(procedureDTO.getProtVerbGmlAppello());
        procedure.setDataProtVerbGmlAppello(procedureDTO.getDataProtVerbGmlAppello());
        procedure.setProtVerbGmlAppPec(procedureDTO.getProtVerbGmlAppPec());
        procedure.setDataProtVerbGmlAppPec(procedureDTO.getDataProtVerbGmlAppPec());
        procedure.setTpCegmlGiudMedLegale2(tpCegmlGiudMedLegaleDTOToTpCegmlGiudMedLegale(procedureDTO.getTpCegmlGiudMedLegale2()));
        if (Objects.nonNull(procedureDTO.getTpStproProvinciaId())) {
            procedure.setIdProvincia(procedureDTO.getTpStproProvinciaId());
        }
        if (Objects.nonNull(procedureDTO.getIdComune())) {
            procedure.setIdComune(procedureDTO.getIdComune());
        }
        procedure.setCapRes(procedureDTO.getCapRes());
        procedure.setIndirizzoRes(procedureDTO.getIndirizzoRes());
        if (Objects.nonNull(procedureDTO.getIdNazione())) {
            procedure.setIdNazione(procedureDTO.getIdNazione());
        }
        procedure.setNumVerbCommAv(procedureDTO.getNumVerbCommAv());
        procedure.setTbCeProcTransito(tbCeProcTransitoDTOResponseToTbCeProcTransito(procedureDTO.getTbCeProcTransito()));
        procedure.setTbCeProcPensione(tbCeProcPensioneDTOToTbCeProcPensione(procedureDTO.getTbCeProcPensione()));
        procedure.setTbCeProcParereRag(tbCeProcParereRagDTOToTbCeProcParereRag(procedureDTO.getTbCeProcParereRag()));
        procedure.setDataAvvio(procedureDTO.getDataAvvio());
        procedure.setDataScadLav(procedureDTO.getDataScadLav());
        procedure.setNote(procedureDTO.getNote());
        return procedure;
    }

}
