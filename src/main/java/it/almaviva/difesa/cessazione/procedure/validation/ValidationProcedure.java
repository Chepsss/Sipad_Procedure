package it.almaviva.difesa.cessazione.procedure.validation;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.DeclarationProcedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCetipCessazione;
import it.almaviva.difesa.cessazione.procedure.exception.CamundaException;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaOutputVariable;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaVariable;
import it.almaviva.difesa.cessazione.procedure.model.camunda.StateContainer;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrattAttivazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrtpoTprocedimentoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgtpoPosizioneStatoDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.CamundaService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.TpCetipCessazioneService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.TpPrattAttivazioneService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.TpPrtpoTprocedimentoService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.TpSgtpoPosizioneStatoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ValidationProcedure {

    public static final int GML_INSTACE_1 = 1;
    public static final int GML_INSTACE_2 = 2;
    public static final String CAMPO_OBBLIGATORIO = "Campo obbligatorio";
    public static final String REQUIRED = "required";
    public static final String ID_TIPO_ATTIVAZIONE = "idTipoAttivazione";
    public static final String ID_TIPO_PROCEDIMENTO = "idTipoProcedimento";
    public static final String ID_TIPO_CESSAZIONE = "idTipoCessazione";
    public static final String ID_CAT_PERS_RICHIESTA = "idCatPersRichiesta";
    public static final String DATA_DOC_RICHIESTA = "dataDocRichiesta";
    public static final String DATA_PRES_DOC_RICH = "dataPresDocRich";
    public static final String DATA_DECORRENZA = "dataDecorrenza";
    public static final String DATA_RAGG_ETA = "dataRaggEta";
    public static final String DATA_GML = "dataGml";
    public static final String ID_TIPO_GML_1 = "idTipoGml1";
    public static final String TIPOLOGIA_GML_1 = "tpCegmlGiudMedLegale";
    public static final String DATA_PROT_ISTANZA = "dataProtIstanza";
    public static final String FL_IMPUGNA_GML = "flImpugnaGml";
    public static final String FL_GML_CONCORDI = "flGmlConcordi";
    public static final String GML_RESPONSE_OUTPUT = "response";
    public static final String GML_ERROR_OUTPUT = "errorOutput";
    public static final String D05 = "D05";
    public static final String INSTANCE = "instance";
    public static final String GML = "gml";
    public static final String SGTPO_ACR_POSIZIONE = "sgtpoAcrPosizione";

    private final CamundaService camundaService;
    private final MessageSource messageSource;
    private final TpCetipCessazioneService tpCetipCessazioneService;
    private final TpPrattAttivazioneService tpPrattAttivazioneService;
    private final TpPrtpoTprocedimentoService tpPrtpoTprocedimentoService;
    private final TpSgtpoPosizioneStatoService tpSgtpoPosizioneStatoService;


    public ValidationProcedure(CamundaService camundaService, MessageSource messageSource,
                               TpCetipCessazioneService tpCetipCessazioneService,
                               TpPrattAttivazioneService tpPrattAttivazioneService,
                               TpPrtpoTprocedimentoService tpPrtpoTprocedimentoService,
                               TpSgtpoPosizioneStatoService tpSgtpoPosizioneStatoService) {
        this.camundaService = camundaService;
        this.messageSource = messageSource;
        this.tpCetipCessazioneService = tpCetipCessazioneService;
        this.tpPrattAttivazioneService = tpPrattAttivazioneService;
        this.tpPrtpoTprocedimentoService = tpPrtpoTprocedimentoService;
        this.tpSgtpoPosizioneStatoService = tpSgtpoPosizioneStatoService;
    }

    protected List<String> getNextStateOnCamunda(String currentState) {
        try {
            List<StateContainer> stateContainerList = camundaService.listNextStates(currentState);
            List<String> nextStates = stateContainerList.stream()
                    .map(stateContainer -> (String) stateContainer.getNextState().getValue()).collect(Collectors.toList());
            log.debug("The next states are: {}", nextStates);
            return nextStates;
        } catch (Exception e) {
            log.error("Error in get next States on camunda: ", e);
            throw new CamundaException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected Map<String, String> checkRequiredFieldOfOpeningData(Procedure procedure, String nextState) {
        List<Map<String, CamundaOutputVariable>> requiredField = getRequiredFieldOnCamunda(procedure, nextState);
        log.debug("requiredField {}", requiredField);
        return validateRequiredField(procedure, requiredField);
    }

    protected List<Map<String, CamundaOutputVariable>> getRequiredFieldOnCamunda(Procedure procedure, String nextState) {
        List<Map<String, CamundaOutputVariable>> requiredField;
        CamundaVariable reasonCessation = new CamundaVariable();
        if (Objects.nonNull(procedure.getReasonCessation())) {
            TpCetipCessazione tpCetipCessazione = tpCetipCessazioneService.getReasonOfCessationById(procedure.getReasonCessation().getId());
            reasonCessation.setValue(tpCetipCessazione.getCetipAcrTiv());
        }
        CamundaVariable flGmlConcordi = new CamundaVariable();
        if (Objects.nonNull(procedure.getFlGmlConcordi())) {
            flGmlConcordi.setValue(procedure.getFlGmlConcordi());
        }
        CamundaVariable flImpugnaGml = new CamundaVariable();
        if (Objects.nonNull(procedure.getFlImpugnaGml())) {
            flImpugnaGml.setValue(procedure.getFlImpugnaGml());
        }
        CamundaVariable prattAcrAtt = new CamundaVariable();
        if (Objects.nonNull(procedure.getOpeningCessation())) {
            TpPrattAttivazioneDTO tpPrattAttivazione = tpPrattAttivazioneService.getOpeningCessationById(procedure.getOpeningCessation());
            prattAcrAtt.setValue(tpPrattAttivazione.getPrattAcrAtt());
        }
        CamundaVariable prtpoAcrProc = new CamundaVariable();
        if (Objects.nonNull(procedure.getTypeCessation())) {
            TpPrtpoTprocedimentoDTO tpPrtpoTprocedimento = tpPrtpoTprocedimentoService.getTypeCessationById(procedure.getTypeCessation());
            prtpoAcrProc.setValue(tpPrtpoTprocedimento.getPrtpoAcrProc());
        }
        Map<String, CamundaVariable> variables = Map.ofEntries(
                new AbstractMap.SimpleImmutableEntry<>(FL_GML_CONCORDI, flGmlConcordi),
                new AbstractMap.SimpleImmutableEntry<>(FL_IMPUGNA_GML, flImpugnaGml),
                new AbstractMap.SimpleImmutableEntry<>(ID_TIPO_ATTIVAZIONE, prattAcrAtt),
                new AbstractMap.SimpleImmutableEntry<>(ID_TIPO_CESSAZIONE, reasonCessation),
                new AbstractMap.SimpleImmutableEntry<>(ID_TIPO_PROCEDIMENTO, prtpoAcrProc),
                new AbstractMap.SimpleImmutableEntry<>(Constant.VARIABLE_STATE, new CamundaVariable(nextState.toUpperCase(), null)));
        try {
            requiredField = camundaService.getProcedureOpeningDataVisibility(variables);
        } catch (Exception e) {
            log.error("Error in get required field on Camunda: ", e);
            throw new CamundaException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return requiredField;
    }

    protected Map<String, String> getGmlVisibilityOnCamunda(Procedure procedure) {
        Map<String, String> listGmlVisibilty;
        CamundaVariable instance = new CamundaVariable();
        CamundaVariable gml = new CamundaVariable();
        CamundaVariable d05 = new CamundaVariable();
        CamundaVariable sgtpoAcrPosizione = new CamundaVariable();
        boolean flImpugnaGml = (Objects.nonNull(procedure.getFlImpugnaGml()) && procedure.getFlImpugnaGml());
        if (flImpugnaGml && Objects.nonNull(procedure.getTpCegmlGiudMedLegale2())) {
            instance.setValue(GML_INSTACE_2);
            gml.setValue(procedure.getTpCegmlGiudMedLegale2().getCegmlDescrizAbbrGml());
        } else {
            instance.setValue(GML_INSTACE_1);
            gml.setValue(procedure.getTpCegmlGiudMedLegale().getCegmlDescrizAbbrGml());
        }
        if (!procedure.getTpCeDichProcs().isEmpty()) {
            Optional<DeclarationProcedure> decOptional = procedure.getTpCeDichProcs().stream()
                    .filter(d -> d.getIdDich().getCodice().equalsIgnoreCase(D05)).findFirst();
            decOptional.ifPresent(declarationProcedure -> d05.setValue(declarationProcedure.getFlagDich()));
        }
        if (Objects.nonNull(procedure.getCategLeaveReq())) {
            TpSgtpoPosizioneStatoDTO catLeave = tpSgtpoPosizioneStatoService.getCategLeaveReqById(procedure.getCategLeaveReq());
            sgtpoAcrPosizione.setValue(catLeave.getSgtpoAcrPosizione());
        }
        Map<String, CamundaVariable> variables = Map.ofEntries(
                new AbstractMap.SimpleImmutableEntry<>(INSTANCE, instance),
                new AbstractMap.SimpleImmutableEntry<>(GML, gml),
                new AbstractMap.SimpleImmutableEntry<>(D05, d05),
                new AbstractMap.SimpleImmutableEntry<>(SGTPO_ACR_POSIZIONE, sgtpoAcrPosizione));
        try {
            listGmlVisibilty = camundaService.getGmlVisibility(variables);
        } catch (Exception e) {
            log.error("Error in get required field on Camunda: ", e);
            throw new CamundaException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return listGmlVisibilty;
    }

    protected Map<String, String> validateRequiredField(Procedure procedure, List<Map<String, CamundaOutputVariable>> requiredField) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (Objects.nonNull(requiredField)) {
            requiredField.forEach(valueMap -> {
                checkOpeningData(procedure, valueMap, errors); // Modalità apertura cessazione
                checkTypeCessation(procedure, valueMap, errors);// Tipologia accesso cessazione
                checkReasonCessation(procedure, valueMap, errors);// Motivo cessazione
                checkCategLeaveReq(procedure, valueMap, errors);// Categoria congedo richiesta/destinazione
                checkDataCompilazioneAtto(procedure, valueMap, errors);// Data compilazione atto o istanza
                checkDataPresentazioneAtto(procedure, valueMap, errors);// Data di presentazione dell’atto all’Ente/Comando/Organo di Giustizia
                checkDataDecorrenza(procedure, valueMap, errors);// Data decorrenza Cessazione
                checkDataRaggiungimentoLimiteEta(procedure, valueMap, errors);// Data raggiungimento limite di età
                checkDataGML(procedure, valueMap, errors);// Data del giudizio medico legale
                checkTipologiaGML1(procedure, valueMap, errors);// Tipologia GML d'I istanza
                checkDataProtocolloAttoIstanzaGiustizia(procedure, valueMap, errors);// Data Protocollo (Protocollo atto o istanza all’Ente/Comando/organo di Giustizia n.ro)
            });
        }
        return errors;
    }

    protected String getCodeProccess(LocalDate dataAvvio, String prtpoAcrProc, String fiscalCode) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMMdd");
        String nowToString = dtf.format(dataAvvio);
        return nowToString + "-" + prtpoAcrProc + "-" + fiscalCode;
    }

    private void checkOpeningData(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        checkDataRequired(procedure.getOpeningCessation(), ID_TIPO_ATTIVAZIONE, ID_TIPO_ATTIVAZIONE, fieldsRequired, errors);
    }

    private void checkTypeCessation(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        checkDataRequired(procedure.getTypeCessation(), ID_TIPO_PROCEDIMENTO, ID_TIPO_PROCEDIMENTO, fieldsRequired, errors);
    }

    private void checkReasonCessation(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        checkDataRequired(procedure.getReasonCessation(), ID_TIPO_CESSAZIONE, ID_TIPO_CESSAZIONE, fieldsRequired, errors);
    }

    private void checkCategLeaveReq(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        if (!isNonAmmissibile(procedure)) {
            checkDataRequired(procedure.getCategLeaveReq(), ID_CAT_PERS_RICHIESTA, ID_CAT_PERS_RICHIESTA, fieldsRequired, errors);
        }
    }

    private void checkDataCompilazioneAtto(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        LocalDateTime date = procedure.getDataDocRichiesta();
        checkDataRequired(date, DATA_DOC_RICHIESTA, DATA_DOC_RICHIESTA, fieldsRequired, errors);
        if (!errors.containsKey(DATA_DOC_RICHIESTA)) {
            log.debug("Check Data compilazione {}", date);
        }
    }

    private void checkDataPresentazioneAtto(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        LocalDateTime date = procedure.getDataPresDocRich();
        checkDataRequired(date, DATA_PRES_DOC_RICH, DATA_PRES_DOC_RICH, fieldsRequired, errors);
        if (Objects.nonNull(date) && !errors.containsKey(DATA_PRES_DOC_RICH)) {
            log.debug("Check Data Presentazione Atto {}", date);
        }
    }

    private void checkDataDecorrenza(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        LocalDateTime date = procedure.getDataDecorrenza();
        checkDataRequired(date, DATA_DECORRENZA, DATA_DECORRENZA, fieldsRequired, errors);
        if (Objects.nonNull(date) && !errors.containsKey(DATA_PRES_DOC_RICH)) {
            log.debug("Check Data Decorrenza {}", date);
        }
    }

    private void checkDataRaggiungimentoLimiteEta(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        LocalDateTime date = procedure.getDataRaggEta();
        checkDataRequired(date, DATA_RAGG_ETA, DATA_RAGG_ETA, fieldsRequired, errors);
        if (Objects.nonNull(date) && !errors.containsKey(DATA_PRES_DOC_RICH)) {
            log.debug("Check Data Raggiungimento Limite Eta {}", date);
        }
    }

    private void checkDataGML(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        LocalDateTime date = procedure.getDataGml();
        checkDataRequired(date, DATA_GML, DATA_GML, fieldsRequired, errors);
        if (Objects.nonNull(date) && !errors.containsKey(DATA_PRES_DOC_RICH)) {
            log.debug("Check Data GML {}", date);
        }
    }

    private void checkTipologiaGML1(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        checkDataRequired(procedure.getTpCegmlGiudMedLegale(), ID_TIPO_GML_1, TIPOLOGIA_GML_1, fieldsRequired, errors);
    }

    private void checkDataProtocolloAttoIstanzaGiustizia(Procedure procedure, Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        LocalDateTime date = procedure.getDataProtIstanza();
        checkDataRequired(date, DATA_PROT_ISTANZA, DATA_PROT_ISTANZA, fieldsRequired, errors);
        if (Objects.nonNull(date) && !errors.containsKey(DATA_PRES_DOC_RICH)) {
            log.debug("Check Data Protocollo Atto o Istanza di Giustizia {}", date);
        }
    }

    private void checkDataRequired(Object procedureField, String keyMapCamunda, String keyMapError,
                                   Map<String, CamundaOutputVariable> fieldsRequired, Map<String, String> errors) {
        if (Objects.isNull(procedureField) && isRequired(fieldsRequired, keyMapCamunda)
                || (Objects.isNull(procedureField) && keyMapCamunda.equalsIgnoreCase(ID_CAT_PERS_RICHIESTA)
                && Objects.nonNull(fieldsRequired.get(ID_CAT_PERS_RICHIESTA))))
            errors.put(messageSource.getMessage(keyMapError, null, keyMapError, Locale.getDefault()), CAMPO_OBBLIGATORIO);
    }

    private boolean isRequired(Map<String, CamundaOutputVariable> variableMap, String key) {
        return Objects.nonNull(variableMap.get(key))
                && Objects.nonNull(variableMap.get(key).getValue())
                && variableMap.get(key).getValue().equals(REQUIRED);
    }

    private boolean isNonAmmissibile(Procedure procedure) {
        boolean isNonAmmissible = false;
        if (Objects.nonNull(procedure.getReasonCessation())) {
            TpCetipCessazione tpCetipCessazione = tpCetipCessazioneService.getReasonOfCessationById(procedure.getReasonCessation().getId());
            isNonAmmissible = tpCetipCessazione.getCetipCenorSeqPk().getId() == 50L; //Non ammissibile, normativa assente
        }
        return isNonAmmissible;
    }

}
