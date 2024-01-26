package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.ChangeProcedureStateErrorMessagesConst;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.DocumentStateConst;
import it.almaviva.difesa.cessazione.procedure.constant.ProcedureStateConst;
import it.almaviva.difesa.cessazione.procedure.constant.TipoDocumento;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureHistory;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCetipCessazione;
import it.almaviva.difesa.cessazione.procedure.exception.CamundaException;
import it.almaviva.difesa.cessazione.procedure.exception.ChangeStateException;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.CustomUserDetail;
import it.almaviva.difesa.cessazione.procedure.model.common.CustomUserDetailDTO;
import it.almaviva.difesa.cessazione.procedure.model.common.GenericResponse;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ChangeStateProcRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.EmployeeResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.RoleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.StgceCessazioneReqDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.UserDetailResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpDostaStatoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrtpoTprocedimentoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgctpCatpersonaleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgtpoPosizioneStatoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpStfaaForzaArmataDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TsStgceCessazioneDTO;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureHistoryRepository;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureRepository;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.StateProcedureRepository;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import it.almaviva.difesa.cessazione.procedure.service.rest.AuthServiceClient;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.cessazione.procedure.validation.ValidationProcedure;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbDocumento;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbPredisposizione;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbSegnatura;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.TbDocumentoList;
import it.almaviva.difesa.documenti.document.model.dto.response.tipidocumenti.TpDotipTDocumentoDto;
import it.almaviva.difesa.documenti.document.service.TbDocumentoService;
import it.almaviva.difesa.documenti.document.service.rest.MsSipadApiClient;
import it.almaviva.difesa.queuehelper.report.ProcedimentoQueueDTO;
import it.almaviva.difesa.queuehelper.report.QueueReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ChangeProcedureStateService extends ValidationProcedure {

    private static final String PROCEDURE_NOT_CLOSED = "Il procedimento non è chiuso";
    private static final String PROCEDURE_CLOSED = "Il procedimento è chiuso corretamente";
    private static final String SUCCESS = "S";
    private static final String ABORTED = "A"; //Valore interno quando la chamata a callStoredProcedureCessazione viene abortita o va in errore

    private final CamundaService camundaService;
    private final MessageSource messageSource;
    private final ProcedureRepository procedureRepository;
    private final ProcedureService procedureService;
    private final ProcedureHistoryRepository procedureHistoryRepository;
    private final ProcedureHistoryService procedureHistoryService;
    private final StateProcedureRepository stateProcedureRepository;
    private final AuthServiceClient authServiceClient;
    private final SecurityService securityService;
    private final CongedoSpettanteProcedureService congedoSpettanteProcedureService;
    private final TpCetipCessazioneService tpCetipCessazioneService;
    private final TbDocumentoService documentoService;

    @Autowired
    MsSipadApiClient sipadApiClient;

    @Autowired
    SipadClient sipadClient;

    @Autowired
    QueueReportService reportProcedimento;

    @Value("${queue:#{true}}")
    private Boolean queueEnable;

    public ChangeProcedureStateService(CamundaService camundaService,
                                       MessageSource messageSource,
                                       ProcedureRepository procedureRepository,
                                       ProcedureService procedureService,
                                       ProcedureHistoryRepository procedureHistoryRepository,
                                       ProcedureHistoryService procedureHistoryService,
                                       StateProcedureRepository stateProcedureRepository,
                                       AuthServiceClient authServiceClient,
                                       SecurityService securityService,
                                       CongedoSpettanteProcedureService congedoSpettanteProcedureService,
                                       TpCetipCessazioneService tpCetipCessazioneService,
                                       TpPrattAttivazioneService tpPrattAttivazioneService,
                                       TpPrtpoTprocedimentoService tpPrtpoTprocedimentoService,
                                       TpSgtpoPosizioneStatoService tpSgtpoPosizioneStatoService,
                                       TbDocumentoService documentoService) {
        super(camundaService, messageSource, tpCetipCessazioneService, tpPrattAttivazioneService, tpPrtpoTprocedimentoService, tpSgtpoPosizioneStatoService);
        this.camundaService = camundaService;
        this.messageSource = messageSource;
        this.procedureRepository = procedureRepository;
        this.procedureService = procedureService;
        this.procedureHistoryRepository = procedureHistoryRepository;
        this.procedureHistoryService = procedureHistoryService;
        this.stateProcedureRepository = stateProcedureRepository;
        this.authServiceClient = authServiceClient;
        this.securityService = securityService;
        this.congedoSpettanteProcedureService = congedoSpettanteProcedureService;
        this.tpCetipCessazioneService = tpCetipCessazioneService;
        this.documentoService = documentoService;
    }

    @Transactional(rollbackFor = Exception.class)
    public RestApiResponse changeProcedureState(ChangeStateProcRequest request, CustomUserDetail userLogged) {
        RestApiResponse response = new RestApiResponse();
        String jwtToken = userLogged.getToken();
        try {
            Procedure procedure = procedureService.getProcedure(request.getIdProc());
            String currentState = procedure.getStateProcedure().getCodeState();
            String nextStateRequest = request.getCodeState();
            Long assigneeId = request.getAssigneeId();
            Set<String> assigneeUserRoles = new LinkedHashSet<>();
            Set<String> loggedUserRoles = new LinkedHashSet<>();
            boolean chiuso = nextStateRequest.equalsIgnoreCase(ProcedureStateConst.CHIUSO)
                    || nextStateRequest.equalsIgnoreCase(ProcedureStateConst.CHIUSO_ANT);
            if (chiuso) {
                loggedUserRoles = securityService.getUserRoles();
            } else {
                UserDetailResponseDTO assigneeUser = authServiceClient.getUserByEmployeeId(assigneeId, jwtToken);
                List<RoleDTO> assigneeUserRolesList = assigneeUser.getRoles();
                assigneeUserRolesList.forEach(roleDTO -> assigneeUserRoles.add(roleDTO.getRoleCode()));
            }
            boolean isInstructor = isInstructor(!assigneeUserRoles.isEmpty() ? assigneeUserRoles : loggedUserRoles, nextStateRequest);
            boolean isManager = isManagerForClosePA(!assigneeUserRoles.isEmpty() ? assigneeUserRoles : loggedUserRoles, nextStateRequest, currentState);

            // Error user unauthorized
            if ((!isInstructor && nextStateRequest.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE))
                    || (!isManager && (chiuso))) {
                throw new ChangeStateException(ChangeProcedureStateErrorMessagesConst.UNAUTHORIZED, HttpStatus.FORBIDDEN);
            }

            // Error procedure already exists
            if (existSameProcedure(procedure)) {
                String fiscalCode = getEmployeeFiscalCode(procedure.getEmployeeId(), jwtToken);
                return setRestApiResponse(response, ChangeProcedureStateErrorMessagesConst.PROCEDURE_ALREADY_EXISTS, fiscalCode);
            }

            StateProcedure nextStateFromDB = stateProcedureRepository.findByCodeState(nextStateRequest)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            String.format(ChangeProcedureStateErrorMessagesConst.STATE_NOT_FOUND, request.getCodeState())));
            String nextState = nextStateFromDB.getCodeState();

            assertChangeState(currentState, nextState);

            // Check required fields of opening data only when next state is "LAVORAZIONE"
            if (nextState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)) {
                Map<String, String> errors = checkRequiredFieldOfOpeningData(procedure, nextState);
                if (!errors.isEmpty()) {
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setErrors(errors);
                    return response;
                }
            }

            // Check reason of "Divenuto permanentemente non idoneo al servizio incondizionato - BD169"
            TpCetipCessazione reason = procedure.getReasonCessation();
            if (Objects.nonNull(reason) && reason.getCetipAcrTiv().equalsIgnoreCase(Constant.CETIP_PERMANENTLY_UNSUITABLE)) {
                Map<String, String> resultFormCamunda = getGmlVisibilityOnCamunda(procedure);
                if (resultFormCamunda.containsKey(GML_ERROR_OUTPUT)) {
                    log.debug("Response OutPut >>>>>>>>>>>>>: {}", resultFormCamunda.get(GML_RESPONSE_OUTPUT));
                    String error = resultFormCamunda.get(GML_ERROR_OUTPUT);
                    return setRestApiResponse(response, error);
                }
            }

            // Check for change document states
            TbDocumentoList documents = changeDocumentStates(procedure, currentState, nextState);

            checkForQuartaVia(currentState, nextState, documents);

            checkForCloseProcedure(response, jwtToken, procedure, currentState, nextState, documents);

            setDataScadLavAndCodeProcess(jwtToken, procedure, nextState);
            setPriorita(request, procedure);
            procedure.setAssignmentDate(LocalDateTime.now());

            if (!nextState.equalsIgnoreCase(ProcedureStateConst.CHIUSO) && !nextState.equalsIgnoreCase(ProcedureStateConst.CHIUSO_ANT)) {
                createProcedureHistory(procedure, nextStateFromDB, assigneeId);
            }
            procedure.setStateProcedure(nextStateFromDB);
            Procedure savedProcedure = procedureRepository.save(procedure);

            if (currentState.equalsIgnoreCase(ProcedureStateConst.BOZZA) && nextState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)) {
                congedoSpettanteProcedureService.setCongedoSpettante(savedProcedure, savedProcedure.getTbCeProcPensione());
            }

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage(String.format(Constant.CHANGE_STATE_OK, nextState));
            log.info("Call Queue sendReportProcedimento");
            sendReportProcedimento(savedProcedure, jwtToken);

            log.info(">>>>>>>> Change State on Camunda");
            if (!currentState.equalsIgnoreCase(nextState)) {
                changeProcedureStateOnCamunda(savedProcedure.getBpmnProcessId(), nextState, isManager);
                endProcessOnCamunda(savedProcedure.getBpmnProcessId(), nextState);
            }

        } catch (Exception e) {
            log.error("ERROR in change State ==> ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return response;
    }


    public RestApiResponse retryCloseProceduresOperations(Long idProcedure, CustomUserDetail userLogged) {
        RestApiResponse response = new RestApiResponse();
        Procedure procedure = procedureService.getProcedure(idProcedure);
        String currentState = procedure.getStateProcedure().getCodeState();
        if (!ProcedureStateConst.CHIUSO.equals(currentState)) {
            response.setMessage(PROCEDURE_NOT_CLOSED);
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        if (SUCCESS.equalsIgnoreCase(procedure.getStgceFlagElaborato())) {
            response.setMessage(PROCEDURE_CLOSED);
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        TbDocumento decreto = recuperaDecreto(procedure);
        String jwtToken = userLogged.getToken();
        storedProcedureCessazione(response, jwtToken, procedure, decreto);
        return response;
    }

    private void checkForQuartaVia(String currentState, String nextState, TbDocumentoList documents) {
        if (currentState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DECRETO) && nextState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)) {
            Long statoInApprovazioneAdHocId = sipadClient.dostaStatoByAcr(DocumentStateConst.IN_APPROVAZIONE_ADHOC).getId();
            Optional<TbDocumento> letterInApprovazioneAdHoc = documents.getTbDocumentos().stream()
                    .filter(doc -> doc.getIdTipo().equalsIgnoreCase(TipoDocumento.LETTERA_TRASMISSIONE)
                            && doc.getIdStato().equals(statoInApprovazioneAdHocId)).findFirst();
            if (letterInApprovazioneAdHoc.isPresent()) {
                throw new ChangeStateException(ChangeProcedureStateErrorMessagesConst.GO_TO_LAVORAZIONE_NOT_POSSIBLE, HttpStatus.FORBIDDEN);
            }
        }
    }

    private void checkForCloseProcedure(RestApiResponse response, String jwtToken, Procedure procedure, String currentState, String nextState, TbDocumentoList documents) {
        if ((currentState.equalsIgnoreCase(ProcedureStateConst.IN_CHIUSURA)
                || currentState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DECRETO))
                && (nextState.equalsIgnoreCase(ProcedureStateConst.CHIUSO) || nextState.equalsIgnoreCase(ProcedureStateConst.CHIUSO_ANT))) {
            Long statoProtocollatoId = sipadClient.dostaStatoByAcr(DocumentStateConst.PROTOCOLLATO).getId();
            Optional<TbDocumento> letter = documents.getTbDocumentos().stream()
                    .filter(doc -> doc.getIdTipo().equalsIgnoreCase(TipoDocumento.LETTERA_TRASMISSIONE)
                            && doc.getIdStato().equals(statoProtocollatoId)).findFirst();

            // Error lettera trasmissione non inviata ad AdHoc
            if (letter.isEmpty()) {
                throw new ChangeStateException(ChangeProcedureStateErrorMessagesConst.LETTER_NOT_SENT_TO_ADHOC, HttpStatus.FORBIDDEN);
            }
            if (Boolean.TRUE.equals(procedure.getFlVistoRagioneria())) {
                // Error parere ragioneria: campi obbligatori
                if (Objects.isNull(procedure.getTbCeProcParereRag())) {
                    throw new ChangeStateException(ChangeProcedureStateErrorMessagesConst.ACCOUNTING_REQUIRED, HttpStatus.FORBIDDEN);
                } else {
                    boolean checkParereRag = Objects.nonNull(procedure.getTbCeProcParereRag().getEsito())
                            && Objects.nonNull(procedure.getTbCeProcParereRag().getNumRegistrazione())
                            && Objects.nonNull(procedure.getTbCeProcParereRag().getDataEsito());

                    // Error parere ragioneria: campi obbligatori
                    if (Boolean.FALSE.equals(checkParereRag)) {
                        throw new ChangeStateException(ChangeProcedureStateErrorMessagesConst.ACCOUNTING_REQUIRED, HttpStatus.FORBIDDEN);
                    }
                }
            }
            if (Boolean.FALSE.equals(procedure.getFlVistoRagioneria())
                    || (Objects.nonNull(procedure.getTbCeProcParereRag()) && procedure.getTbCeProcParereRag().getEsito())) {
                TbDocumento decreto = filtraDecretoDaiDocumenti(documents);
                storedProcedureCessazione(response, jwtToken, procedure, decreto);
            }
        }
    }

    private void storedProcedureCessazione(RestApiResponse response, String jwtToken, Procedure procedure, TbDocumento decreto) {
        TsStgceCessazioneDTO requestDTO = setStgceCessazioneRequestDTO(procedure, decreto, jwtToken);
        TsStgceCessazioneDTO stgceCessazioneDTO = insertTsStgceCessazione(requestDTO);
        callStoredProcedureCessazione(procedure, stgceCessazioneDTO, decreto, response);
    }

    private TbDocumento recuperaDecreto(Procedure procedure) {
        TbDocumentoList documents = documentoService.tbDocumentoList(procedure.getId());
        return filtraDecretoDaiDocumenti(documents);
    }

    private TbDocumento filtraDecretoDaiDocumenti(TbDocumentoList documents) {
        Long statoSterilizzatoId = sipadClient.dostaStatoByAcr(DocumentStateConst.STERILIZZATO).getId();
        return documents.getTbDocumentos().stream()
                .filter(doc -> doc.getIdTipo().equalsIgnoreCase(TipoDocumento.DECRETO)
                        && !doc.getIdStato().equals(statoSterilizzatoId)).findFirst().orElse(new TbDocumento());
    }

    private static void setPriorita(ChangeStateProcRequest request, Procedure procedure) {
        if (Objects.nonNull(request.getPriorita())) {
            procedure.setPriorita(request.getPriorita());
        }
    }

    private void setDataScadLavAndCodeProcess(String jwtToken, Procedure procedure, String nextState) {
        if (nextState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)) {
            String fiscalCode = getEmployeeFiscalCode(procedure.getEmployeeId(), jwtToken);
            LocalDateTime dataAvvio = procedure.getDataAvvio();
            TpPrtpoTprocedimentoDTO typeCessation = sipadClient.tipoProcedimentoById(procedure.getTypeCessation());
            Long numGgLav = typeCessation.getPrtpoNumGgDurlav();
            procedure.setDataScadLav(dataAvvio.plusDays(numGgLav));
            procedure.setCodeProcess(getCodeProccess(dataAvvio.toLocalDate(), typeCessation.getPrtpoAcrProc(), fiscalCode));
        }
    }

    private void checkDecreeSignatureAndLetterPredisposition(String currentState, String nextState, TbDocumentoList documents) {
        Long statoSterilizzatoId = sipadClient.dostaStatoByAcr(DocumentStateConst.STERILIZZATO).getId();
        if (currentState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)
                && (nextState.equalsIgnoreCase(ProcedureStateConst.APPR_FIRMA) || nextState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DECRETO))) {
            if (!documents.getTbDocumentos().isEmpty()) {
                List<TbDocumento> decreti = documents.getTbDocumentos().stream()
                        .filter(tbDocumento -> tbDocumento.getIdTipo().equalsIgnoreCase(TipoDocumento.DECRETO)
                                && !tbDocumento.getIdStato().equals(statoSterilizzatoId)).collect(Collectors.toList());
                // Errore documento inesistente
                if (decreti.isEmpty()) {
                    throw new ChangeStateException(ChangeProcedureStateErrorMessagesConst.DECRETO_NOT_FOUND, HttpStatus.FORBIDDEN);
                } else {
                    TbDocumento decreto = decreti.stream().findFirst().orElse(new TbDocumento());
                    TbSegnatura segnatura = decreto.getSegnatura();
                    // Errore segnatura non presente
                    if (Objects.isNull(segnatura) || Objects.isNull(segnatura.getDataReg()) || Objects.isNull(segnatura.getCodRuolo())) {
                        throw new ChangeStateException(ChangeProcedureStateErrorMessagesConst.SEGNATURA_NOT_FOUND, HttpStatus.FORBIDDEN);
                    }
                    List<TbDocumento> lettere = documents.getTbDocumentos().stream()
                            .filter(tbDocumento -> tbDocumento.getIdTipo().equalsIgnoreCase(TipoDocumento.LETTERA_TRASMISSIONE)
                                    && !tbDocumento.getIdStato().equals(statoSterilizzatoId)).collect(Collectors.toList());
                    TbDocumento letteraTrasmissione = lettere.stream().findFirst().orElse(null);
                    if (Objects.nonNull(letteraTrasmissione)) {
                        checkEmptyPredisposition(letteraTrasmissione);
                    } else {
                        throw new ChangeStateException(ChangeProcedureStateErrorMessagesConst.LETTER_NOT_FOUND, HttpStatus.FORBIDDEN);
                    }
                }
            } else {
                // Errore documento inesistente
                throw new ChangeStateException(ChangeProcedureStateErrorMessagesConst.DECRETO_NOT_FOUND, HttpStatus.FORBIDDEN);
            }
        }
    }

    private void checkEmptyPredisposition(TbDocumento letteraTrasmissione) {
        TbPredisposizione predisposizione = letteraTrasmissione.getPredisposizione();
        // Errore predisposizione non presente
        if (Objects.isNull(predisposizione) || Objects.isNull(predisposizione.getUoMittente()) || Objects.isNull(predisposizione.getAssegnatario())
                || Objects.isNull(predisposizione.getDestinatari())) {
            throw new ChangeStateException(ChangeProcedureStateErrorMessagesConst.PREDISPOSIZIONE_NOT_FOUND, HttpStatus.FORBIDDEN);
        }
    }

    private void callStoredProcedureCessazione(Procedure procedure, TsStgceCessazioneDTO stgceCessazioneDTO, TbDocumento decreto, RestApiResponse response) {
        try {
            log.info("Call callStoredProcedureCessazione on SIPAD");

            StgceCessazioneReqDTO req = new StgceCessazioneReqDTO();
            req.setCodiceFiscale(stgceCessazioneDTO.getStgceCodiceFiscale());
            req.setNumeroDecreto(stgceCessazioneDTO.getStgceNumeroDecreto());

            GenericResponse resp = sipadClient.callStoredProcedureCessazione(req);
            log.info("End callStoredProcedureCessazione on SIPAD");

            // Error chiamata procedura su AASTG
            if (Objects.nonNull(resp.getError()) && !resp.getError().isEmpty()) {
                log.info("Error callStoredProcedureCessazione on SIPAD : {}", resp.getError());
                response.setWarning(ChangeProcedureStateErrorMessagesConst.ERROR_PROCEDURE_CALL);
                procedure.setStgceFlagElaborato(ABORTED);
            } else {
                stgceCessazioneDTO = sipadClient.getStgceCessazioneByCodFiscaleAndNumDecreto(req);
                procedure.setStgceFlagElaborato(stgceCessazioneDTO.getStgceFlagElaborato());
                procedure.setStgceElabMsg(stgceCessazioneDTO.getStgceElabMsg());
                if (Objects.nonNull(decreto)) {
                    decreto.setDodocId(stgceCessazioneDTO.getStgceDodocSeq());
                    documentoService.save(decreto);
                }
            }
        } catch (Exception e) {
            log.error("Error callStoredProcedureCessazione on SIPAD : ", e);
            response.setWarning(ChangeProcedureStateErrorMessagesConst.ERROR_PROCEDURE_CALL);
            procedure.setStgceFlagElaborato(ABORTED);
        }
    }

    private void sendReportProcedimento(Procedure savedProcedure, String jwtToken) {
        ProcedimentoQueueDTO procedimentoOutput = new ProcedimentoQueueDTO();
        procedimentoOutput.setIdProcedimento(savedProcedure.getId());
        EmployeeResponseDTO employeeResponseDTO = authServiceClient.getEmployeeById(savedProcedure.getEmployeeId(), jwtToken);
        procedimentoOutput.setCodiceFiscaleUtente(employeeResponseDTO.getFiscalCode());
        procedimentoOutput.setCodiceProcedimento(savedProcedure.getCodeProcess());
        procedimentoOutput.setDataAvvioProcedimento(Date.from(savedProcedure.getDataAvvio().atZone(ZoneId.systemDefault()).toInstant()));
        procedimentoOutput.setDataFineProcedimento(Date.from(savedProcedure.getDataScadLav().atZone(ZoneId.systemDefault()).toInstant()));
        procedimentoOutput.setFaseProcedimento(savedProcedure.getStateProcedure().getFaseProcedure().getCodeFase());
        procedimentoOutput.setStatoProcedimento(savedProcedure.getStateProcedure().getCodeStateCentral());
        TpPrtpoTprocedimentoDTO typeCessation = sipadClient.tipoProcedimentoById(savedProcedure.getTypeCessation());
        procedimentoOutput.setTipoProcedimento(typeCessation.getPrtpoDescrProc());
        procedimentoOutput.setCodiceFiscaleAutoreProcedimento(savedProcedure.getCreatedBy());
        procedimentoOutput.setNomeVerticale(Constant.CESSAZIONE);
        procedimentoOutput.setCodiceFiscaleUltimoInserimento(savedProcedure.getCreatedBy());
        procedimentoOutput.setCodiceFiscaleUltimoAggiornamento(savedProcedure.getLastModifiedBy());
        procedimentoOutput.setDataInserimento(Date.from(savedProcedure.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant()));
        procedimentoOutput.setDataUltimaModifica(Date.from(savedProcedure.getLastModifiedDate().atZone(ZoneId.systemDefault()).toInstant()));
        if (Boolean.TRUE.equals(queueEnable)) {
            reportProcedimento.sendReportProcedimento(procedimentoOutput);
        }
    }

    private TsStgceCessazioneDTO insertTsStgceCessazione(TsStgceCessazioneDTO requestDTO) {
        TsStgceCessazioneDTO stgceCessazioneDTO;
        try {
            log.info("Call insertStcgeCessazione on SIPAD");
            stgceCessazioneDTO = sipadClient.insertStgceCessazione(requestDTO);
        } catch (Exception e) {
            // Error aggiornamento stato giuridico. changeState annullato
            log.error("ERROR in insert stgceCessazione on change State ==> ", e);
            throw new ServiceException(ChangeProcedureStateErrorMessagesConst.ERROR_INSERT_STGCE_CESSAZIONE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return stgceCessazioneDTO;
    }

    private boolean isInstructor(Set<String> userRoles, String nextStateRequest) {
        boolean hasInstructorRole = userRoles.stream()
                .anyMatch(role -> role.startsWith(Constant.INSTRUCTOR_ROLE_PREFIX));
        return hasInstructorRole
                && nextStateRequest.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE);
    }

    private boolean isManagerForClosePA(Set<String> userRoles, String nextStateRequest, String currentState) {
        boolean hasRDPRole = userRoles.stream()
                .anyMatch(role -> role.startsWith(Constant.MANAGER_ROLE_PREFIX));
        return (nextStateRequest.equalsIgnoreCase(ProcedureStateConst.CHIUSO) || nextStateRequest.equalsIgnoreCase(ProcedureStateConst.CHIUSO_ANT))
                && (currentState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DECRETO)
                || currentState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DINIEGO)
                || currentState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_RDP)
                || currentState.equalsIgnoreCase(ProcedureStateConst.IN_CHIUSURA))
                && hasRDPRole;
    }

    private void assertChangeState(String currentState, String nextState) {
        if (!currentState.equalsIgnoreCase(nextState)) {
            List<String> nextStates = getNextStateOnCamunda(currentState);
            nextStates.stream()
                    .filter(state -> state.equalsIgnoreCase(nextState))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ChangeProcedureStateErrorMessagesConst.CHANGE_STATE_NOT_POSSIBLE));
        }
    }

    private boolean existSameProcedure(Procedure procedure) {
        return procedureRepository.findProcedureByEmployeeId(procedure.getEmployeeId()).stream()
                .filter(proc -> !proc.getStateProcedure().getCodeState().equalsIgnoreCase(ProcedureStateConst.BOZZA))
                .filter(proc -> !Objects.equals(proc.getId(), procedure.getId()))
                .anyMatch(proc -> proc.getReasonCessation().getCetipMotivoCessazione().equals(procedure.getReasonCessation().getCetipMotivoCessazione())
                        && proc.getDataDecorrenza().equals(procedure.getDataDecorrenza())
                );
    }

    private String getEmployeeFiscalCode(Long employeeId, String jwtToken) {
        EmployeeResponseDTO employeeResponseDTO = this.authServiceClient
                .getEmployeeById(employeeId, jwtToken);
        return employeeResponseDTO.getFiscalCode();
    }

    private RestApiResponse setRestApiResponse(RestApiResponse response, String error) {
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setGlobalError(error);
        return response;
    }

    private RestApiResponse setRestApiResponse(RestApiResponse response, String error, String fiscalCode) {
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setGlobalError(String.format(error, fiscalCode));
        return response;
    }

    private TbDocumentoList changeDocumentStates(Procedure procedure, String currentState, String nextState) {
        TbDocumentoList documents = documentoService.tbDocumentoList(procedure.getId());
        if (!documents.getTbDocumentos().isEmpty()) {
            // Verifica se presente segnatura del decreto e predisposizione della lettera di trasmissione (nextState = APPR_FIRMA)
            checkDecreeSignatureAndLetterPredisposition(currentState, nextState, documents);
            documents.getTbDocumentos().forEach(doc -> {
                if (currentState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)
                        && nextState.equalsIgnoreCase(ProcedureStateConst.APPR_DOC)) {
                    changeDocumentStateForLetter(doc, DocumentStateConst.IN_LAVORAZIONE, DocumentStateConst.IN_APPROVAZIONE);
                }
                if (currentState.equalsIgnoreCase(ProcedureStateConst.APPR_DOC)
                        && nextState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)) {
                    changeDocumentStateForLetter(doc, DocumentStateConst.IN_APPROVAZIONE, DocumentStateConst.IN_LAVORAZIONE);
                }
                if (currentState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)
                        && nextState.equalsIgnoreCase(ProcedureStateConst.APPR_FIRMA)) {
                    changeDocumentStateForDecree(doc, DocumentStateConst.IN_LAVORAZIONE, DocumentStateConst.IN_APPROVAZIONE, null);
                }
                if (currentState.equalsIgnoreCase(ProcedureStateConst.APPR_FIRMA)
                        && nextState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)) {
                    changeDocumentStateForDecree(doc, DocumentStateConst.IN_APPROVAZIONE, DocumentStateConst.IN_LAVORAZIONE, null);
                }
                if (currentState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)
                        && nextState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DECRETO)) {
                    changeDocumentStateForDecree(doc, DocumentStateConst.IN_LAVORAZIONE, DocumentStateConst.IN_APPROVAZIONE, ProcedureStateConst.CHIUSURA_PA_DECRETO);
                }
                if (currentState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DECRETO)
                        && nextState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)) {
                    changeDocumentStateForDecree(doc, DocumentStateConst.IN_APPROVAZIONE, DocumentStateConst.IN_LAVORAZIONE, ProcedureStateConst.LAVORAZIONE);
                }
            });
        }
        return documents;
    }

    private void changeDocumentStateForLetter(TbDocumento doc, String currentDocumentState, String nextDocumentState) {
        TpDotipTDocumentoDto dotip = sipadApiClient.getDotipDocumentoByCode(doc.getIdTipo());
        TpDostaStatoDTO statoDoc = sipadClient.dostaStatoById(doc.getIdStato());
        TpDostaStatoDTO nextState = sipadClient.dostaStatoByAcr(nextDocumentState);
        if (dotip.getDocatId() == 3L
                && !doc.getIdTipo().equalsIgnoreCase(TipoDocumento.LETTERA_TRASMISSIONE)
                && statoDoc.getAcrSta().equalsIgnoreCase(currentDocumentState)) {
            doc.setIdStato(nextState.getId());
            documentoService.save(doc);
        }
    }

    private void changeDocumentStateForDecree(TbDocumento doc, String currentDocumentState, String nextDocumentState, String nextProcState) {
        TpDostaStatoDTO currentState = sipadClient.dostaStatoById(doc.getIdStato());
        TpDostaStatoDTO nextState = sipadClient.dostaStatoByAcr(nextDocumentState);
        if ((doc.getIdTipo().equalsIgnoreCase(TipoDocumento.LETTERA_TRASMISSIONE)
                || doc.getIdTipo().equalsIgnoreCase(TipoDocumento.DECRETO))
                && currentState.getAcrSta().equalsIgnoreCase(currentDocumentState)) {
            doc.setIdStato(nextState.getId());
            if (Objects.nonNull(nextProcState)) {
                if (nextProcState.equalsIgnoreCase(ProcedureStateConst.CHIUSURA_PA_DECRETO)) {
                    doc.setSubStato(Constant.CICLO_ADHOC_4_VIA);
                }
                if (nextProcState.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)) {
                    doc.setSubStato(null);
                }
            }
            documentoService.save(doc);
        }
    }

    private TsStgceCessazioneDTO setStgceCessazioneRequestDTO(Procedure procedure, TbDocumento decreto, String jwtToken) {
        try {
            TsStgceCessazioneDTO dto = new TsStgceCessazioneDTO();
            dto.setStgceCodiceFiscale(getEmployeeFiscalCode(procedure.getEmployeeId(), jwtToken));
            dto.setStgceDataDecorrenza(procedure.getDataDecorrenza().toLocalDate());
            dto.setStgceCodUidTt01(getStfaaUid(procedure));
            dto.setStgceCodUidTt03(getSgctpUid(procedure));
            dto.setStgceCodUidTt05(Constant.MILITARE_IN_CONGEDO);
            dto.setStgceCodUidTt06(Constant.CONGEDO);
            TpSgtpoPosizioneStatoDTO posizioneStatoDTO = sipadClient.categoryOfLeaveById(procedure.getCategLeaveReq());
            dto.setStgceCodUidTt07(posizioneStatoDTO.getSgtpoCodUid());
            dto.setStgceCodUidTt51(procedure.getReasonCessation().getCetipAcrTiv());
            dto.setStgceNumeroDecreto(String.valueOf(decreto.getNumAtto()));
            dto.setStgceDataDecreto(decreto.getDataIns().toLocalDateTime().toLocalDate());
            dto.setStgceOggettoDecreto(decreto.getOggetto());
            CustomUserDetailDTO userLogged = securityService.getUserDetails();
            dto.setStgceCodUltAgg(userLogged.getUsername());
            if (Objects.nonNull(procedure.getPromTitOnor())) {
                dto.setStgcePromTitOnor(procedure.getPromTitOnor() ? "S" : "N");
            }
            return dto;
        } catch (Exception e) {
            log.error("ERROR in set stgceCessazioneRequestDTO ==> ", e);
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createProcedureHistory(Procedure procedure, StateProcedure nextState, Long idAssignedTo) {
        String nextStateCode = nextState.getCodeState();
        String nextRoleCode = procedureHistoryService.getRoleOfCurrentUserAssigned(nextStateCode, procedure.getIdCatMilitare());
        addNewHistory(procedure, nextState, idAssignedTo, nextRoleCode);
    }

    private void addNewHistory(Procedure procedure, StateProcedure nextState, Long idAssignedTo, String nextRoleCode) {

        procedureHistoryService.updateAllOldHistoryOfProcedure(procedure.getId());

        ProcedureHistory procedureHistoryNew = new ProcedureHistory();
        procedureHistoryNew.setRoleCode(nextRoleCode);
        procedureHistoryNew.setAssignmentDate(procedure.getAssignmentDate());
        procedureHistoryNew.setFlagAttuale(true);
        procedureHistoryNew.setProcedure(procedure);
        procedureHistoryNew.setStateProcedure(nextState);
        procedureHistoryRepository.save(procedureHistoryNew);
        procedureService.createProcedureAssignment(procedureHistoryNew, idAssignedTo);
    }

    private void changeProcedureStateOnCamunda(String bpmnProcessId, String nextState, boolean isManager) {
        try {
            if (isManager) {
                camundaService.changeState(bpmnProcessId, nextState, Constant.MANAGER_ROLE_PREFIX);
            } else {
                camundaService.changeState(bpmnProcessId, nextState, null);
            }
        } catch (Exception e) {
            log.error("Error changing state on camunda: ", e);
            throw new CamundaException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void endProcessOnCamunda(String bpmnProcessId, String nextState) {
        try {
            if (nextState.equalsIgnoreCase(ProcedureStateConst.CHIUSO) || nextState.equalsIgnoreCase(ProcedureStateConst.CHIUSO_ANT))
                camundaService.endProcessInstance(bpmnProcessId);
        } catch (Exception e) {
            log.error("Error ending process on camunda: ", e);
            throw new CamundaException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getStfaaUid(Procedure procedure) {
        String stfaa = procedure.getReasonCessation().getCetipStfaaSeqPk();
        TpStfaaForzaArmataDTO stfaaForzaArmataDTO = sipadClient.stfaaForzaArmataById(stfaa);
        return stfaaForzaArmataDTO.getStfaaCodUid();
    }

    private String getSgctpUid(Procedure procedure) {
        Long sgctp = procedure.getReasonCessation().getCetipSgctpSeqPk();
        TpSgctpCatpersonaleDTO sgctpCatpersonaleDTO = sipadClient.catMilitareById(sgctp);
        return sgctpCatpersonaleDTO.getSgctpCodUid();
    }

}
