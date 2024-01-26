package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Declaration;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.DeclarationProcedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcPensione;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaVariable;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.DeclarationDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.DeclarationProcWrapper;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.DeclarationProcedureDTOResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.EmployeeResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.LimiteEtaRequestDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.DeclarationProcedureMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.DeclarationProcedureRepository;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.DeclarationRepository;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureRepository;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import it.almaviva.difesa.cessazione.procedure.service.rest.AuthServiceClient;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DeclarationProcedureService {

    private final DeclarationProcedureRepository declarationProcedureRepository;
    private final DeclarationRepository declarationRepository;
    private final DeclarationProcedureMapper declarationProcedureMapper;
    private final ProcedureRepository procedureRepository;
    private final SecurityService securityService;
    private final AuthServiceClient authServiceClient;
    private final TpCelceLimitiCszPerEtaService tpCelceLimitiCszPerEtaService;
    private final DeclarationService declarationService;
    private final SipadClient sipadClient;

    public List<DeclarationProcedureDTOResponse> getAllDeclarationsByIdProcedure(Long idProcedure) {
        Procedure procedure = procedureRepository.findById(idProcedure).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<DeclarationProcedure> declarationProcedures = declarationProcedureRepository.findDeclarationProcedureByIdProc(procedure);
        return declarationProcedureMapper.toDto(declarationProcedures);
    }

    public List<DeclarationProcedureDTOResponse> saveDeclarationsOfProcedure(Procedure procedure, DeclarationProcWrapper request) {
        try {
            List<DeclarationProcedure> declarationProcedures = new ArrayList<>();
            request.getDeclarations().forEach(decl -> {
                DeclarationProcedure declarationProcedure = new DeclarationProcedure();
                if (Objects.nonNull(decl.getIdDich())) {
                    Declaration declaration = declarationRepository.findById(decl.getIdDich())
                            .orElseThrow(() ->
                                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                                            String.format(ErrorsConst.DECLARATION_NOT_FOUND, decl.getIdDich())));
                    declarationProcedure.setIdProc(procedure);
                    declarationProcedure.setIdDich(declaration);
                    declarationProcedure.setFlagDich(decl.getFlagDich());
                    if (Objects.nonNull(decl.getDataDich())) {
                        declarationProcedure.setDataDich(decl.getDataDich().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    } else {
                        declarationProcedure.setDataDich(null);
                    }
                    declarationProcedures.add(declarationProcedure);
                } else {
                    if (Objects.nonNull(decl.getNumVerbCommAv())) {
                        procedure.setNumVerbCommAv(decl.getNumVerbCommAv());
                        procedureRepository.save(procedure);
                    }
                }
            });
            procedure.setTpCeDichProcs(declarationProcedureRepository.saveAll(declarationProcedures));
            if (!declarationProcedures.isEmpty()) {
                procedure.setLastModifiedDate(declarationProcedures.get(0).getLastModifiedDate());
                procedureRepository.save(procedure);
            }
            automaticControls(procedure);
            List<DeclarationProcedureDTOResponse> dtos = procedure.getTpCeDichProcs().stream()
                    .map(declarationProcedureMapper::toDto)
                    .collect(Collectors.toList());
            log.debug("Declaration proc DTO after save are: {}", dtos);
            return dtos;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    public List<DeclarationProcedureDTOResponse> updateDeclarationsOfProcedure(Procedure procedure, DeclarationProcWrapper request) {
        try {
            declarationProcedureRepository.deleteDeclarationProcedureByIdProcId(procedure.getId());
            return this.saveDeclarationsOfProcedure(procedure, request);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    public void automaticControls(Procedure procedure) {
        List<DeclarationDTO> declarationList = getDeclarationVisibility(procedure);
        if (!declarationList.isEmpty()) {
            TbCeProcPensione pensionData = procedure.getTbCeProcPensione();
            Optional<DeclarationProcedure> controlC06 = getControl(procedure, Constant.CONTROL_06);
            if (checkControlVisibility(declarationList, Constant.CONTROL_01)
                    && Objects.nonNull(procedure.getDataDecorrenza()) && Objects.nonNull(procedure.getDataPresDocRich())) {
                saveControl(procedure, Constant.CONTROL_01, checkDeclarationDeadline(procedure));
            }
            if (checkControlVisibility(declarationList, Constant.CONTROL_11)
                    && Objects.nonNull(pensionData) && Objects.nonNull(pensionData.getDataMatReqMinimo())
                    && Objects.nonNull(pensionData.getMesiIncrSperanzaV()) && Objects.nonNull(pensionData.getMesiFinMobile())
                    && Objects.nonNull(pensionData.getDataDirittoTratt())) {
                saveControl(procedure, Constant.CONTROL_11, checkPensionData(pensionData));
            }
            if (checkControlVisibility(declarationList, Constant.CONTROL_05)
                    && Objects.nonNull(pensionData) && Objects.nonNull(pensionData.getDataDirittoTratt())
                    && Objects.nonNull(procedure.getDataDecorrenza())) {
                saveControl(procedure, Constant.CONTROL_05, checkRightPension(procedure, pensionData));
            }
            if (checkControlVisibility(declarationList, Constant.CONTROL_13)
                    && Objects.nonNull(pensionData) && Objects.nonNull(pensionData.getAnniAnzContr())
                    && Objects.nonNull(pensionData.getEtaDipendente())) {
                saveControl(procedure, Constant.CONTROL_13, checkPensionMinimumRequirement(pensionData));
            }
            if (checkControlVisibility(declarationList, Constant.CONTROL_14)
                    && controlC06.isPresent() && checkControl06(procedure, controlC06.get())) {
                LocalDate controlC06Date = controlC06.get().getDataDich();
                saveControl(procedure, Constant.CONTROL_14, checkCessationImpediment(controlC06Date, procedure));
            }
            if (checkControlVisibility(declarationList, Constant.CONTROL_08) && Objects.nonNull(pensionData)) {
                saveControl(procedure, Constant.CONTROL_08, checkFiveYearsRequirement(procedure));
            }
        }
    }

    private List<DeclarationDTO> getDeclarationVisibility(Procedure procedure) {
        List<DeclarationDTO> declarationDTOS = new ArrayList<>();
        if (Objects.nonNull(procedure.getId())) {
            Map<String, CamundaVariable> variables = new HashMap<>();
            CamundaVariable faseCode = new CamundaVariable();
            if (Objects.nonNull(procedure.getStateProcedure())) {
                faseCode.setValue(procedure.getStateProcedure().getFaseProcedure().getCodeFase());
                faseCode.setType(Constant.STRING);
            }
            CamundaVariable tipoProcedimento = new CamundaVariable();
            if (Objects.nonNull(procedure.getTypeCessation())) {
                String value = sipadClient.tipoProcedimentoById(procedure.getTypeCessation()).getPrtpoAcrProc();
                tipoProcedimento.setValue(value);
                tipoProcedimento.setType(Constant.STRING);
            }
            variables.put(Constant.VARIABLE_FASE, faseCode);
            variables.put(Constant.VARIABLE_ID_TIPO_PROCEDIMENTO, tipoProcedimento);
            declarationDTOS = declarationService.findAllDeclarationsByCodeList(variables);
        }
        return declarationDTOS;
    }

    private boolean checkControl06(Procedure procedure, DeclarationProcedure controlC06) {
        return Objects.nonNull(controlC06.getFlagDich()) && controlC06.getFlagDich() && Objects.nonNull(controlC06.getDataDich())
                && Objects.nonNull(procedure.getDataDecorrenza());
    }

    public boolean checkControlVisibility(List<DeclarationDTO> declarationList, String automaticControl) {
        return declarationList.stream().anyMatch(control -> control.getCodice().equalsIgnoreCase(automaticControl));
    }

    public Optional<DeclarationProcedure> getControl(Procedure procedure, String controlCode) {
        return procedure.getTpCeDichProcs().stream()
                .filter(declarationProcedure -> declarationProcedure.getIdDich().getCodice().equalsIgnoreCase(controlCode))
                .findFirst();
    }

    private void saveControl(Procedure procedure, String controlCode, boolean checkControl) {
        Optional<DeclarationProcedure> declaration = getControl(procedure, controlCode);
        DeclarationProcedure declarationProcedure;
        if (declaration.isPresent()) {
            declarationProcedure = declaration.get();
            declarationProcedure.setFlagDich(checkControl);
        } else {
            Declaration declarationByCodice = declarationRepository.findDeclarationByCodice(controlCode);
            declarationProcedure = new DeclarationProcedure();
            declarationProcedure.setIdProc(procedure);
            declarationProcedure.setFlagDich(checkControl);
            declarationProcedure.setIdDich(declarationByCodice);
        }
        declarationProcedureRepository.save(declarationProcedure);
    }

    private boolean checkDeclarationDeadline(Procedure procedure) {
        return ChronoUnit.DAYS.between(procedure.getDataDecorrenza(), procedure.getDataPresDocRich()) <= 270;
    }

    private boolean checkPensionData(TbCeProcPensione pensionData) {
        return pensionData.getDataMatReqMinimo()
                .plusMonths(pensionData.getMesiIncrSperanzaV())
                .plusMonths(pensionData.getMesiFinMobile()) == pensionData.getDataDirittoTratt();
    }

    private boolean checkRightPension(Procedure procedure, TbCeProcPensione pensionData) {
        return !pensionData.getDataDirittoTratt().isAfter(ChronoLocalDate.from(procedure.getDataDecorrenza()));
    }

    private boolean checkPensionMinimumRequirement(TbCeProcPensione pensionData) {
        return pensionData.getAnniAnzContr() == 40
                || (pensionData.getAnniAnzContr() == 35 && pensionData.getEtaDipendente() == 57);
    }

    private boolean checkCessationImpediment(LocalDate date, Procedure procedure) {
        return date.isAfter(ChronoLocalDate.from(procedure.getDataDecorrenza()));
    }

    private boolean checkFiveYearsRequirement(Procedure procedure) {
        String jwtToken = securityService.getUserToken();
        Long employeeId = procedure.getEmployeeId();
        EmployeeResponseDTO employeeDetail = authServiceClient.getEmployeeById(employeeId, jwtToken);
        LimiteEtaRequestDTO limiteEtaRequestDTO = new LimiteEtaRequestDTO();
        limiteEtaRequestDTO.setRankId(employeeDetail.getRankId());
        limiteEtaRequestDTO.setRoleId(employeeDetail.getRoleId());
        limiteEtaRequestDTO.setArmedForceId(employeeDetail.getArmedForceId());
        Integer limitByTypological = tpCelceLimitiCszPerEtaService.getLimiteEta(limiteEtaRequestDTO);
        int limit = Objects.requireNonNullElse(limitByTypological, 60);
        LocalDate pensionData = (employeeDetail.getBirthDate()).plusYears(limit);
        return pensionData.minusYears(5).isBefore(ChronoLocalDate.from(procedure.getDataDecorrenza()));
    }

    public void deleteDeclarationsByIdProcedure(Long idProcedure) {
        declarationProcedureRepository.deleteDeclarationProcedureByIdProcId(idProcedure);
    }

}
