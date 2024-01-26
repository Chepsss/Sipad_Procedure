package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.constant.ProcedureStateConst;
import it.almaviva.difesa.cessazione.procedure.converter.ProcedureEmployeeConverter;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureAssignment;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.ProcedureHistory;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.StateProcedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcParereRag;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcPensione;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcTransito;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.common.CustomUserDetailDTO;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.FaseProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureCompleteDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureEmployeeDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureOpeningDataDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.StateProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.AddressElectedRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.DeclarationProcWrapper;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.NoteRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ParereRagRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.PensionRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.PriorityRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ProcedureSearchRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.TbCeProcTransitoDTORequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.DeclarationProcedureDTOResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.EmployeeResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.UserDetailResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrattAttivazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrtpoTprocedimentoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgctpCatpersonaleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgtpoPosizioneStatoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwSg155StgiuridicoDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.ProcedureCompleteMapper;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.ProcedureOpeningDataMapper;
import it.almaviva.difesa.cessazione.procedure.model.mapper.sipad.EmployeeResponseMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureAssignmentRepository;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureHistoryRepository;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureRepository;
import it.almaviva.difesa.cessazione.procedure.repository.specification.ProcedureFiltersSpecification;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import it.almaviva.difesa.cessazione.procedure.service.rest.AuthServiceClient;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.cessazione.procedure.util.CommonUtilsMethod;
import it.almaviva.difesa.cessazione.procedure.util.Utils;
import it.almaviva.difesa.cessazione.procedure.validation.ValidationProcedure;
import static it.almaviva.difesa.cessazione.procedure.util.Utils.confrontaStringhe;//metodo cr16
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing {@link Procedure}.
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ProcedureService extends ValidationProcedure {

    private final ProcedureRepository procedureRepository;
    private final StateProcedureService stateProcedureService;
    private final SecurityService securityService;
    private final ProcedureOpeningDataMapper procedureOpeningDataMapper;
    private final ProcedureFiltersSpecification procedureFiltersSpecification;
    private final AuthServiceClient authServiceClient;
    private final ProcedureEmployeeConverter procedureEmployeeConverter;
    private final CamundaService camundaService;
    private final MessageSource messageSource;
    private final TbCeProcTransitoService tbCeProcTransitoService;
    private final DeclarationProcedureService declarationProcedureService;
    private final TbCeProcPensioneService tbCeProcPensioneService;
    private final TbCeProcParereRagService tbCeProcParereRagService;
    private final CongedoSpettanteProcedureService congedoSpettanteProcedureService;
    private final TpSgtpoPosizioneStatoService sgtpoPosizioneStatoService;
    private final EmployeeResponseMapper employeeResponseMapper;
    private final ProcedureHistoryService procedureHistoryService;
    private final ProcedureHistoryRepository procedureHistoryRepository;
    private final ProcedureAssignmentRepository procedureAssignmentRepository;
    private final ProcedureAssignmentService procedureAssignmentService;
    private final ProcedureCompleteMapper procedureCompleteMapper;
    private final SipadClient sipadClient;
    private final TpSgctpCatPersonaleService tpSgctpCatPersonaleService;

    public ProcedureService(ProcedureRepository procedureRepository,
                            StateProcedureService stateProcedureService,
                            SecurityService securityService,
                            ProcedureOpeningDataMapper procedureOpeningDataMapper,
                            ProcedureFiltersSpecification procedureFiltersSpecification,
                            AuthServiceClient authServiceClient,
                            ProcedureEmployeeConverter procedureEmployeeConverter,
                            CamundaService camundaService,
                            MessageSource messageSource,
                            TbCeProcTransitoService tbCeProcTransitoService,
                            DeclarationProcedureService declarationProcedureService,
                            TbCeProcPensioneService tbCeProcPensioneService,
                            TbCeProcParereRagService tbCeProcParereRagService,
                            CongedoSpettanteProcedureService congedoSpettanteProcedureService,
                            TpCetipCessazioneService tpCetipCessazioneService,
                            TpPrattAttivazioneService tpPrattAttivazioneService,
                            TpPrtpoTprocedimentoService tpPrtpoTprocedimentoService,
                            TpSgtpoPosizioneStatoService sgtpoPosizioneStatoService,
                            EmployeeResponseMapper employeeResponseMapper,
                            ProcedureHistoryService procedureHistoryService,
                            ProcedureHistoryRepository procedureHistoryRepository,
                            ProcedureAssignmentRepository procedureAssignmentRepository,
                            ProcedureAssignmentService procedureAssignmentService,
                            ProcedureCompleteMapper procedureCompleteMapper,
                            SipadClient sipadClient,
                            TpSgctpCatPersonaleService tpSgctpCatPersonaleService) {
        super(camundaService, messageSource, tpCetipCessazioneService, tpPrattAttivazioneService, tpPrtpoTprocedimentoService, sgtpoPosizioneStatoService);
        this.procedureRepository = procedureRepository;
        this.stateProcedureService = stateProcedureService;
        this.securityService = securityService;
        this.procedureOpeningDataMapper = procedureOpeningDataMapper;
        this.procedureFiltersSpecification = procedureFiltersSpecification;
        this.authServiceClient = authServiceClient;
        this.procedureEmployeeConverter = procedureEmployeeConverter;
        this.camundaService = camundaService;
        this.messageSource = messageSource;
        this.tbCeProcTransitoService = tbCeProcTransitoService;
        this.declarationProcedureService = declarationProcedureService;
        this.tbCeProcPensioneService = tbCeProcPensioneService;
        this.tbCeProcParereRagService = tbCeProcParereRagService;
        this.congedoSpettanteProcedureService = congedoSpettanteProcedureService;
        this.sgtpoPosizioneStatoService = sgtpoPosizioneStatoService;
        this.employeeResponseMapper = employeeResponseMapper;
        this.procedureHistoryService = procedureHistoryService;
        this.procedureHistoryRepository = procedureHistoryRepository;
        this.procedureAssignmentRepository = procedureAssignmentRepository;
        this.procedureAssignmentService = procedureAssignmentService;
        this.procedureCompleteMapper = procedureCompleteMapper;
        this.sipadClient = sipadClient;
        this.tpSgctpCatPersonaleService = tpSgctpCatPersonaleService;
    }

    /**
     * Get all the procedures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProcedureCompleteDTO> findAll(ProcedureSearchRequest procedureSearchRequest, Pageable pageable) {
        log.debug("Request to get all Procedures");
        CustomUserDetailDTO userLogged = securityService.getUserDetails();
        String visibility = procedureSearchRequest.getVisibility();
        String jwtToken = securityService.getUserToken();

        Specification<Procedure> specification = procedureFiltersSpecification.getSpecification(procedureSearchRequest, pageable);
        Page<Procedure> procedurePage = procedureRepository.findAll(specification, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        Page<ProcedureCompleteDTO> procedureDTOS = procedurePage.map(procedureCompleteMapper::toDto);
        procedureDTOS.getContent().parallelStream().forEach(procedure -> mapOtherFields(procedure, visibility, jwtToken, userLogged.getEmployeeId()));

        return sortProcedures(procedureDTOS, pageable);
    }

    /**
     * Get one procedure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public ProcedureDTO findOne(Long id) {
        log.debug("Request to get Procedure : {}", id);
        String jwtToken = securityService.getUserToken();
        Procedure procedure = procedureRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Long idAssignedTo = getIdAssignedTo(procedure.getId());
        ProcedureDTO procedureDTO = procedureCompleteMapper.toDto(procedure);
        procedureDTO.setEmployee(getEmployeeDetail(procedureDTO.getEmployeeId()));
        procedureDTO.setAssignedTo(getUserDetail(idAssignedTo, jwtToken));
        procedureDTO.setIdAssignedTo(idAssignedTo);
        Long userLoggedEmployeeId = securityService.getUserDetails().getEmployeeId();
        Optional<StateProcedure> stateProcedure = stateProcedureService.findStateById(procedure.getStateProcedure().getId());
        if (stateProcedure.isPresent()) {
            String codeState = stateProcedure.get().getCodeState();
            procedureDTO.setCanModifyPriority(userLoggedEmployeeId.equals(idAssignedTo)
                    && !codeState.equalsIgnoreCase(ProcedureStateConst.BOZZA)
                    && !codeState.equalsIgnoreCase(ProcedureStateConst.CHIUSO)
                    && !codeState.equalsIgnoreCase(ProcedureStateConst.CHIUSO_ANT));
        }
        return procedureDTO;
    }

    @Transactional(readOnly = true)
    public Procedure getProcedure(Long idProc) {
        return procedureRepository.findProcedureById(idProc);
    }

    /**
     * Delete the procedure by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Procedure : {}", id);
        Procedure procedure = getProcedure(id);
        procedureRepository.deleteById(id);
        camundaService.deleteProcessInstance(procedure.getBpmnProcessId());
    }

    public ProcedureDTO createOrUpdateProcedureOpeningData(ProcedureOpeningDataDTO procedureOpeningDataDTO) {
        log.debug("Request to create or update Procedure : {}", procedureOpeningDataDTO);
        Procedure procedure;
        StateProcedure currentState;
        if (Objects.isNull(procedureOpeningDataDTO.getId())) {
            procedure = procedureOpeningDataMapper.toEntity(procedureOpeningDataDTO);
            procedure.setAssignmentDate(LocalDateTime.now());
            CustomUserDetailDTO userLogged = securityService.getUserDetails();
            procedure.setAuthor(userLogged.getFirstName() + " " + userLogged.getLastName());
            if (Objects.nonNull(procedureOpeningDataDTO.getDataProtIstanza())) {
                procedure.setDataAvvio(procedureOpeningDataDTO.getDataProtIstanza());
            }
            String bpmnProcessId = camundaService.createProcedure();


            if (Objects.nonNull(bpmnProcessId)) {
                procedure.setBpmnProcessId(bpmnProcessId);
            }
            currentState = stateProcedureService.findByCode(ProcedureStateConst.BOZZA)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorsConst.BOZZA_NOT_FOUND));
        } else {
            procedure = procedureRepository.findById(procedureOpeningDataDTO.getId()).orElseThrow(() -> {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(ErrorsConst.PROCEDURE_NOT_FOUND, procedureOpeningDataDTO.getId()));
            });
            currentState = stateProcedureService.findStateById(procedure.getStateProcedure().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorsConst.BOZZA_NOT_FOUND));
            String state = currentState.getCodeState();

            procedureOpeningDataDTO.setStateProcedureId(currentState.getId());
            checkCategPers(procedureOpeningDataDTO, procedure, state);
            generateCodeProcess(procedure, state);
            declarationProcedureService.automaticControls(procedure);
            if (Objects.nonNull(procedure.getReasonCessation())
                    && Objects.nonNull(procedure.getCategLeaveReq())
                    && procedure.getReasonCessation().getCetipCenorSeqPk().getId() != 50L) {
                congedoSpettanteProcedureService.setCongedoSpettante(procedure, procedure.getTbCeProcPensione());
            }
            procedureOpeningDataDTO.setIdCatPersSpettante(procedure.getIdCatPersSpettante());
            procedureOpeningDataMapper.updateProcedureFromDTO(procedure, procedureOpeningDataDTO);
            setStartAndEndDates(procedure, state, procedureOpeningDataDTO);
        }
        if (currentState.getCodeState().equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)) {
            validazionePromozioneATitoloOnorifico(procedureOpeningDataDTO);
        }
        Procedure procedureSaved = procedureRepository.save(procedure);
        saveStoricoProcedimento(procedureSaved, currentState, procedureOpeningDataDTO.getIdAssignedTo());
        ProcedureDTO procedureDTO = procedureCompleteMapper.toDto(procedureSaved);
        procedureDTO.setIdAssignedTo(procedureOpeningDataDTO.getIdAssignedTo());
        Optional<StateProcedure> stateProcedure = stateProcedureService.findStateById(procedureSaved.getStateProcedure().getId());
        if (stateProcedure.isPresent() && stateProcedure.get().getCodeState().equalsIgnoreCase(ProcedureStateConst.BOZZA)) {
            Map<String, String> validationWarnings = checkRequiredFieldOfOpeningData(procedureSaved, ProcedureStateConst.LAVORAZIONE);
            procedureDTO.setWarningMessages(validationWarnings);
        }

        //cr16

        confrontaStringhe(procedure.getIdEnte(),
                procedure.getIdEnte_cc1(),
                procedure.getIdEnte_cc2(),
                procedure.getIdEnte_cc3());

       //fine cr16






        return procedureDTO;
    }

    private void validazionePromozioneATitoloOnorifico(ProcedureOpeningDataDTO procedureOpeningDataDTO) {
        TpPrtpoTprocedimentoDTO tpPrtpoTprocedimentoDTO = sipadClient.tipoProcedimentoById(procedureOpeningDataDTO.getIdTipoProcedimento());
        if (Objects.nonNull(tpPrtpoTprocedimentoDTO)) {
            String acronimo = tpPrtpoTprocedimentoDTO.getPrtpoAcrProc();
            if (Utils.TIPI_PROCEDIMENTO.contains(acronimo)) {
                if (Objects.isNull(procedureOpeningDataDTO.getPromTitOnor())) {
                    throw new ServiceException(ErrorsConst.PROM_TIT_ONOR_REQUIRED, HttpStatus.BAD_REQUEST);
                }
                VwSg155StgiuridicoDTO sg155StgiuridicoDTO = sipadClient.getEmployeeById(procedureOpeningDataDTO.getEmployeeId());
                Short sg155ValGerarchia = sg155StgiuridicoDTO.getSg155ValGerarchia();
                if (Utils.GERARCHIE.contains(sg155ValGerarchia)) {
                    if (procedureOpeningDataDTO.getPromTitOnor()) {
                        throw new ServiceException(ErrorsConst.PROM_TIT_ONOR_NOT_VALID, HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
    }

    private void saveStoricoProcedimento(Procedure procedureSaved, StateProcedure currentState, Long idAssignedTo) {
        String roleCode = procedureHistoryService.getRoleOfCurrentUserAssigned(currentState.getCodeState(), procedureSaved.getIdCatMilitare());
        ProcedureHistory procedureHistory = procedureHistoryService.getCurrentHistory(procedureSaved.getId(), currentState.getId(), roleCode);
        if (Objects.isNull(procedureHistory)) {
            procedureHistory = new ProcedureHistory();
            procedureHistory.setAssignmentDate(procedureSaved.getAssignmentDate());
            procedureHistory.setRoleCode(roleCode);
            procedureHistory.setProcedure(procedureSaved);
            procedureHistory.setStateProcedure(currentState);
            procedureHistory.setFlagAttuale(true);
            ProcedureHistory procedureHistorySaved = procedureHistoryRepository.save(procedureHistory);
            createProcedureAssignment(procedureHistorySaved, idAssignedTo);
        }
    }

    public void createProcedureAssignment(ProcedureHistory procedureHistory, Long idAssignedTo) {
        ProcedureAssignment procedureAssignment = new ProcedureAssignment();
        procedureAssignment.setIdAssignedTo(idAssignedTo);
        procedureAssignment.setProcedureHistory(procedureHistory);
        procedureAssignment.setFlagLavorato(true);
        procedureAssignment.setDefinitive(true);
        procedureAssignment.setStartDate(procedureHistory.getAssignmentDate());
        procedureAssignment.setEndDate(LocalDateTime.of(9999, 12, 31, 0, 0, 0));
        procedureAssignmentRepository.save(procedureAssignment);
    }

    private void setStartAndEndDates(Procedure procedure, String state, ProcedureOpeningDataDTO procedureOpeningDataDTO) {
        if (Objects.nonNull(procedure.getDataProtIstanza()) &&
                (Objects.nonNull(state) && (state.equalsIgnoreCase(ProcedureStateConst.BOZZA) || state.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)))
        ) {
            procedure.setDataAvvio(procedureOpeningDataDTO.getDataProtIstanza());
            if (Objects.nonNull(procedure.getTypeCessation())) {
                long numGgLav = getNumGgLavByIdTypeCessation(procedure, procedureOpeningDataDTO);
                procedure.setDataScadLav(procedure.getDataAvvio().plusDays(numGgLav));
            }
        }
    }

    public ProcedureDTO saveOrUpdateAddressElected(AddressElectedRequest request) {
        log.debug("Request to save or update Address : {}", request);
        Procedure procedure = getProcedure(request.getIdProc(), request.getEmployeeId(), request.getIdCatPers());
        if (!procedure.getStateProcedure().getCodeState().equalsIgnoreCase(ProcedureStateConst.BOZZA)
                && Objects.nonNull(request.getIdProvincia()) && request.getIdProvincia() == 0 && Objects.isNull(request.getIdNazione())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorsConst.NATION_REQUIRED);
        }
        declarationProcedureService.automaticControls(procedure);
        ProcedureDTO procedureDTO = procedureCompleteMapper.toDto(saveAddressOfProcedure(procedure, request));
        setIdAssignedToOnProcedureDTO(procedureDTO);
        return procedureDTO;
    }

    public ProcedureDTO saveOrUpdateDeclarations(DeclarationProcWrapper request) {
        log.debug("Request to Save or Update declarations : {}", request);
        Procedure procedure = procedureRepository.save(getProcedure(request.getIdProc(), request.getEmployeeId(), request.getIdCatPers()));
        List<DeclarationProcedureDTOResponse> declarationProcedureDTOResponseSet;
        if (Objects.isNull(request.getIdProc())) {
            declarationProcedureDTOResponseSet = declarationProcedureService.saveDeclarationsOfProcedure(procedure, request);
        } else {
            declarationProcedureDTOResponseSet = declarationProcedureService.updateDeclarationsOfProcedure(procedure, request);
        }
        congedoSpettanteProcedureService.setCongedoSpettante(procedure, procedure.getTbCeProcPensione());
        ProcedureDTO dto = procedureCompleteMapper.toDto(procedure);
        dto.setTpCeDichProcs(declarationProcedureDTOResponseSet);
        setIdAssignedToOnProcedureDTO(dto);
        return dto;
    }

    public ProcedureDTO saveOrUpdateTransition(TbCeProcTransitoDTORequest request) {
        log.debug("Request to Save or Update transition : {}", request);
        Procedure procedure = getProcedure(request.getIdProc(), request.getEmployeeId(), request.getIdCatPers());
        procedure = procedureRepository.save(procedure);
        TbCeProcTransito transito = tbCeProcTransitoService.saveOrUpdateTransition(procedure, request);
        procedure.setLastModifiedDate(transito.getLastModifiedDate());
        procedure.setTbCeProcTransito(transito);
        declarationProcedureService.automaticControls(procedure);
        ProcedureDTO dto = procedureCompleteMapper.toDto(procedure);
        setIdAssignedToOnProcedureDTO(dto);
        return dto;
    }

    public ProcedureDTO saveOrUpdatePensione(PensionRequest request) {
        log.debug("Request to Save or Update pensione : {}", request);
        Procedure procedure = getProcedure(request.getIdProc(), request.getEmployeeId(), request.getIdCatPers());
        TbCeProcPensione pensione = tbCeProcPensioneService.saveOrUpdatePensione(procedure, request);
        procedure.setLastModifiedDate(pensione.getLastModifiedDate());
        procedure.setTbCeProcPensione(pensione);
        procedure = procedureRepository.save(procedure);
        declarationProcedureService.automaticControls(procedure);
        congedoSpettanteProcedureService.setCongedoSpettante(procedure, pensione);
        ProcedureDTO dto = procedureCompleteMapper.toDto(procedure);
        setIdAssignedToOnProcedureDTO(dto);
        return dto;
    }

    public ProcedureDTO saveOrUpdateParereRag(ParereRagRequest request) {
        log.debug("Request to Save or Update parere ragioneria : {}", request);
        Procedure procedure = getProcedure(request.getIdProc(), request.getEmployeeId(), request.getIdCatPers());
        TbCeProcParereRag parere = tbCeProcParereRagService.saveOrUpdateParereRag(procedure, request);
        procedure.setLastModifiedDate(parere.getLastModifiedDate());
        procedure.setTbCeProcParereRag(parere);
        procedure = procedureRepository.save(procedure);
        declarationProcedureService.automaticControls(procedure);
        ProcedureDTO dto = procedureCompleteMapper.toDto(procedure);
        setIdAssignedToOnProcedureDTO(dto);
        return dto;
    }

    public boolean checkEmployeeStatusOfCessation(Long employeeId) {
        return procedureRepository.existsByEmployeeId(employeeId);
    }

    private Procedure getProcedure(Long idProc, Long employeeId, Long idCatPers) {
        if (Objects.isNull(idProc)) {
            Procedure procedure = new Procedure();
            procedure.setAssignmentDate(LocalDateTime.now());
            procedure.setEmployeeId(employeeId);
            StateProcedure stateProcedure = stateProcedureService.findByCode(ProcedureStateConst.BOZZA)
                    .orElseThrow(() -> new ServiceException(ErrorsConst.BOZZA_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR));
            procedure.setStateProcedure(stateProcedure);
            CustomUserDetailDTO userLogged = securityService.getUserDetails();
            procedure.setIdAuthor(userLogged.getEmployeeId());
            procedure.setAuthor(userLogged.getFirstName() + " " + userLogged.getLastName());
            procedure.setIdCatMilitare(idCatPers);
            String bpmnProcessId = camundaService.createProcedure();
            if (Objects.nonNull(bpmnProcessId)) {
                procedure.setBpmnProcessId(bpmnProcessId);
            }
            Procedure procedureSaved = procedureRepository.save(procedure);
            saveStoricoProcedimento(procedureSaved, stateProcedure, userLogged.getEmployeeId());
            return procedureSaved;
        } else {
            return procedureRepository.getById(idProc);
        }
    }

    public ProcedureEmployeeDTO getEmployeeDetail(Long employeeId) {
        try {
            VwSg155StgiuridicoDTO sg155StgiuridicoDTO = sipadClient.getEmployeeById(employeeId);
            EmployeeResponseDTO employeeResponseDTO = employeeResponseMapper.copyProperties(sg155StgiuridicoDTO);
            return procedureEmployeeConverter.convert(employeeResponseDTO);
        } catch (ServiceException e) {
            log.error("ERROR => ", e);
            return null;
        }
    }

    public UserDetailResponseDTO getUserDetail(Long idAssignedTo, String jwtToken) {
        try {
            return authServiceClient.getUserByEmployeeId(idAssignedTo, jwtToken);
        } catch (ServiceException e) {
            log.error("ERROR => ", e);
            return null;
        }
    }

    private Procedure saveAddressOfProcedure(Procedure procedure, AddressElectedRequest request) {
        try {
            if (Objects.nonNull(request.getIdProvincia()) && request.getIdProvincia() != 0) {
                procedure.setIdProvincia(request.getIdProvincia());
            } else {
                procedure.setIdProvincia(null);
            }
            if (Objects.nonNull(request.getIdComune())) {
                procedure.setIdComune(request.getIdComune());
            } else {
                procedure.setIdComune(null);
            }
            if (Objects.nonNull(request.getIdNazione())) {
                procedure.setIdNazione(request.getIdNazione());
            } else {
                procedure.setIdNazione(null);
            }
            procedure.setCapRes(request.getCap());
            procedure.setIndirizzoRes(request.getIndirizzo());
            return procedureRepository.save(procedure);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void generateCodeProcess(Procedure procedure, String state) {
        if (Objects.nonNull(state) && state.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)) {
            ProcedureEmployeeDTO employee = getEmployeeDetail(procedure.getEmployeeId());
            String fiscalCode = (Objects.nonNull(employee)) ? employee.getFiscalCode() : "";
            LocalDate dataAvvio = procedure.getDataAvvio().toLocalDate();
            TpPrtpoTprocedimentoDTO typeCessation = sipadClient.tipoProcedimentoById(procedure.getTypeCessation());
            procedure.setCodeProcess(getCodeProccess(dataAvvio, typeCessation.getPrtpoAcrProc(), fiscalCode));
        }
    }

    private void checkCategPers(ProcedureOpeningDataDTO req, Procedure procedure, String state) {
        if (Objects.nonNull(procedure.getCategLeaveReq()) &&
                (Objects.nonNull(state) && (state.equalsIgnoreCase(ProcedureStateConst.BOZZA) || state.equalsIgnoreCase(ProcedureStateConst.LAVORAZIONE)))
        ) {
            TpSgtpoPosizioneStatoDTO posizioneStatoDTO = sgtpoPosizioneStatoService.getCategLeaveReqById(procedure.getCategLeaveReq());
            if (Objects.nonNull(posizioneStatoDTO) && !posizioneStatoDTO.getId().equalsIgnoreCase(req.getIdCatPersRichiesta())) {
                declarationProcedureService.deleteDeclarationsByIdProcedure(procedure.getId());
            }
        }
    }

    public RestApiResponse saveNote(NoteRequest noteRequest) {
        RestApiResponse response = new RestApiResponse();
        Procedure procedure = getProcedure(noteRequest.getProcedureId());
        if (Objects.nonNull(procedure)) {
            String note = noteRequest.getAuthor().concat(Constant.NOTE_SEPARATOR)
                    .concat(noteRequest.getDate().toString()
                            .concat(Constant.NOTE_SEPARATOR)
                            .concat(noteRequest.getNote()));
            if (Objects.nonNull(procedure.getNote())) {
                procedure.setNote(note.concat(Constant.NOTES_SEPARATOR).concat(procedure.getNote()));
            } else {
                procedure.setNote(note);
            }
            procedureRepository.save(procedure);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage(Constant.NOTE_SAVED);
        } else {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(ErrorsConst.NOTE_UNSAVED);
        }
        return response;
    }

    public void deleteNote(Long procedureId) {
        Procedure procedure = getProcedure(procedureId);
        if (Objects.nonNull(procedure)) {
            procedure.setNote(null);
            procedureRepository.save(procedure);
        }
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public List<ProcedureDTO> getProceduresNotClosedByIdAssignedTo(Long idAssignedTo) {
        List<ProcedureDTO> procedureDTOS = procedureRepository.getProceduresNotClosedByIdAssignedTo(idAssignedTo, List.of(ProcedureStateConst.CHIUSO, ProcedureStateConst.CHIUSO_ANT))
                .stream()
                .map(procedureCompleteMapper::toDto)
                .collect(Collectors.toList());
        procedureDTOS.forEach(proc -> {
            VwSg155StgiuridicoDTO sg155StgiuridicoDTO = sipadClient.getEmployeeById(proc.getEmployeeId());
            TpSgctpCatpersonaleDTO catMilitare = tpSgctpCatPersonaleService.getCatMilitareById(sg155StgiuridicoDTO.getSg155IdCatmil().longValue());
            proc.setRoles(stateProcedureService.listRolesByStatus(proc.getStateProcedure().getCodeState(), catMilitare.getSgctpCodCatpers()));
        });
        return procedureDTOS;
    }

    public synchronized void mapOtherFields(ProcedureCompleteDTO procedure, String visibility, String jwtToken, Long userLoggedId) {
        log.debug("<================ Start Mapping =============> {}", procedure.getStateProcedure());
        String state = procedure.getStateProcedure().getCodeState();
        boolean canDelete = state.equalsIgnoreCase(ProcedureStateConst.BOZZA);
        procedure.setCanDelete(canDelete);
        Long idAssignedTo = getIdAssignedTo(procedure.getId());
        assert idAssignedTo != null;
        boolean canEdit = idAssignedTo.equals(userLoggedId)
                && !procedure.getStateProcedure().getCodeState().equalsIgnoreCase(ProcedureStateConst.CHIUSO)
                && !procedure.getStateProcedure().getCodeState().equalsIgnoreCase(ProcedureStateConst.CHIUSO_ANT);

        procedure.setCanEdit(canEdit);
        procedure.setEmployee(getEmployeeDetail(procedure.getEmployeeId()));
        procedure.setIdAssignedTo(idAssignedTo);
        procedure.setAssignedTo(getUserDetail(idAssignedTo, jwtToken));
        procedure.setVisibility(CommonUtilsMethod.isNotBlankString(visibility) ? visibility : null);
        procedure.setCanModifyPriority(userLoggedId.equals(idAssignedTo)
                && !state.equalsIgnoreCase(ProcedureStateConst.BOZZA)
                && !state.equalsIgnoreCase(ProcedureStateConst.CHIUSO)
                && !state.equalsIgnoreCase(ProcedureStateConst.CHIUSO_ANT));
        log.debug("<================ END Mapping =============>");
    }

    public Page<ProcedureCompleteDTO> sortProcedures(Page<ProcedureCompleteDTO> procedureDTOS, Pageable pageable) {
        String property = pageable.getSort().stream().map(Sort.Order::getProperty).findFirst().orElse("");
        Sort.Direction direction = pageable.getSort().stream().map(Sort.Order::getDirection).findFirst().orElse(null);
        List<ProcedureCompleteDTO> newListSorted = getListOrdered(procedureDTOS.getContent(), direction, List.of(property.split("[.]")));
        return new PageImpl<>(newListSorted, pageable, procedureDTOS.getTotalElements());
    }

    private List<ProcedureCompleteDTO> getListOrdered(List<ProcedureCompleteDTO> procedures, Sort.Direction direction, List<String> properties) {
        if (Objects.nonNull(direction) && !properties.isEmpty()) {
            Comparator<ProcedureCompleteDTO> comparator = (o1, o2) -> 0;
            if (properties.contains(Constant.LAST_NAME.toLowerCase())) {
                comparator = Comparator.comparing(ProcedureCompleteDTO::getEmployee, Comparator.comparing(ProcedureEmployeeDTO::getLastname));
            } else if (properties.contains(Constant.FIRST_NAME.toLowerCase())) {
                comparator = Comparator.comparing(ProcedureCompleteDTO::getEmployee, Comparator.comparing(ProcedureEmployeeDTO::getFirstname));
            } else if (properties.contains(Constant.OPENING_CESSATION)) {
                comparator = Comparator.comparing(ProcedureCompleteDTO::getOpeningCessation, Comparator.nullsFirst(Comparator.comparing(TpPrattAttivazioneDTO::getPrattDescrAtt)));
            } else if (properties.contains(Constant.TYPE_CESSATION)) {
                comparator = Comparator.comparing(ProcedureCompleteDTO::getTypeCessation, Comparator.nullsFirst(Comparator.comparing(TpPrtpoTprocedimentoDTO::getPrtpoDescrProc)));
            } else if (properties.contains(Constant.FASE_PROCEDURE)) {
                comparator = Comparator.comparing(ProcedureCompleteDTO::getFaseProcedure, Comparator.nullsFirst(Comparator.comparing(FaseProcedureDTO::getDescFase)));
            } else if (properties.contains(Constant.STATE_PROCEDURE)) {
                comparator = Comparator.comparing(ProcedureCompleteDTO::getStateProcedure, Comparator.nullsFirst(Comparator.comparing(StateProcedureDTO::getDescState)));
            }
            return Utils.sortList(procedures, direction, comparator);
        }
        return procedures;
    }

    private Long getNumGgLavByIdTypeCessation(Procedure procedure, ProcedureOpeningDataDTO procedureOpeningDataDTO) {
        long numGgLav = 0L;
        TpPrtpoTprocedimentoDTO typeCessation = sipadClient.tipoProcedimentoById(procedure.getTypeCessation());
        if (Objects.nonNull(typeCessation)) {
            if (Objects.isNull(typeCessation.getPrtpoNumGgDurlav()) && Objects.nonNull(procedureOpeningDataDTO.getIdTipoProcedimento())) {
                TpPrtpoTprocedimentoDTO dtoCessation = sipadClient.tipoProcedimentoById(procedureOpeningDataDTO.getIdTipoProcedimento());
                numGgLav = Objects.nonNull(dtoCessation.getPrtpoNumGgDurlav()) ? dtoCessation.getPrtpoNumGgDurlav() : 0L;
            } else {
                numGgLav = Objects.nonNull(typeCessation.getPrtpoNumGgDurlav()) ? typeCessation.getPrtpoNumGgDurlav() : 0L;
            }
        }
        return numGgLav;
    }

    public ProcedureDTO updatePriority(PriorityRequest request) {
        log.debug("Request to Update procedure priority : {}", request);
        Procedure procedure = procedureRepository.findProcedureById(request.getProcedureId());
        if (Boolean.FALSE.equals(checkPriority(request))) {
            throw new ServiceException(ErrorsConst.PRIORITY_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        procedure.setPriorita(request.getPriority());
        procedure.setLastModifiedDate(LocalDateTime.now());
        procedure = procedureRepository.save(procedure);
        return procedureCompleteMapper.toDto(procedure);
    }

    private Boolean checkPriority(PriorityRequest request) {
        return Objects.nonNull(request.getPriority()) && request.getPriority() > 0 && request.getPriority() < 6;
    }

    private Long getIdAssignedTo(Long procId) {
        ProcedureAssignment assignment = procedureAssignmentService.findCurrentProcedureAssignedTo(procId);
        return (assignment != null) ? assignment.getIdAssignedTo() : null;
    }

    private void setIdAssignedToOnProcedureDTO(ProcedureDTO procedureDTO) {
        CustomUserDetailDTO userLogged = securityService.getUserDetails();
        procedureDTO.setIdAssignedTo(userLogged.getEmployeeId());
    }

}
