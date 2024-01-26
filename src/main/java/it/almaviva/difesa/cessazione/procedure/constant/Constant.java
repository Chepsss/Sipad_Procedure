package it.almaviva.difesa.cessazione.procedure.constant;

public final class Constant {

    private Constant() {
    }

    public static final String AUTH_HEADER = "Authorization-Cess";
    public static final String SYSTEM = "system";
    public static final long ID_APPLICATIVO = 3;
    public static final String ACR_PROC = "CSZIMP";
    public static final String CESSAZIONE = "Cessazione";

    //date formats
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DD_MM_YYYY = "dd-MM-yyyy";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DD_MM_YYYY_WITH_SLASH = "dd/MM/yyyy";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy hh:mm:ss";

    //controller paths
    public static final String PROCEDURE_INDEX_URL = "/procedure";
    public static final String RIASSEGNAZIONE_INDEX_URL = "/riassegnazione";
    public static final String STATE_PROCEDURE_INDEX_URL = "/state-procedure";
    public static final String FASE_PROCEDURE_INDEX_URL = "/fase-procedure";
    public static final String PROCEDURE_HISTORY_INDEX_URL = "/procedure-history";
    public static final String CAMUNDA_URL = "/camunda";
    public static final String PROCEDURES_ARCHIVE_URL = "/proceduresArchive";
    public static final String LOGICTYPE_INDEX_URL = "/logic-type";
    public static final String PROCEDURE_OPENING_DATA_VISIBILITY_INDEX_URL = "/procedure-opening-data-visibility";
    public static final String DECLARATIONS_VISIBILITY_INDEX_URL = "/declarations-visibility";
    public static final String DECLARATIONS_PROCEDURE_INDEX_URL = "/declarationsProcedure";
    public static final String PENSION_DATA_VISIBILITY_INDEX_URL = "/pension-data-visibility";
    public static final String PROCEDURE_TRANSIT_DATA_DECISION_INDEX_URL = "/procedure-transit-data-decision";
    public static final String CHECK_PROCEDURE_INDEX_URL = "/check-procedure";
    public static final String CHANGE_PROCEDURE_STATE_INDEX_URL = "/changeState";
    public static final String DOCUMENT_INDEX_URL = "/document";
    public static final String FILING_PLAN_INDEX_URL = "/filingPlan";
    public static final String REGISTER_INDEX_URL = "/register";
    public static final String DESK_INDEX_URL = "/desk";
    public static final String ORGANIZATION_CHART_INDEX_URL = "/organizationChart";
    public static final String EMPLOYEE_CATEGORY_INDEX_URL = "/employeeCategory";
    public static final String STAFF_CATEGORY_INDEX_URL = "/staffCategory";
    public static final String PROCEDURE_TABS_DATA_VISIBILITY_INDEX_URL = "/procedure-data-visibility";
    public static final String ALLEGATI_INDEX_URL = "/allegati";
    public static final String DESTINATARI_EXT_INDEX_URL = "/destinatariext";
    public static final String PREDISPOSIZIONE_INDEX_URL = "/predisposizione";
    public static final String PREVIEW_INDEX_URL = "/editor";
    public static final String PROTOCOLLAZIONE_INDEX_URL = "/protocollazione";
    public static final String SEGNATURA_INDEX_URL = "/segnatura";
    public static final String TIPO_DOC_INDEX_URL = "/tipodocumento";
    public static final String TITOLARIO_INDEX_URL = "/titolario";
    public static final String CLOSE_PROCEDURE_INDEX_URL = "/closeProcedure";

    //camunda
    public static final String CAMUNDA_START_PROCESS = "%s%s/key/%s/tenant-id/%s/start";
    public static final String CAMUNDA_KEY_S_EVALUATE = "%s%s/key/%s/tenant-id/%s/evaluate";
    public static final String CAMUNDA_PROCESS = "%s%s/%s";
    public static final String CAMUNDA_LIST_TASK = "%s%s?processInstanceId=%s";
    public static final String CAMUNDA_COMPLETE_TASK = "%s%s/%s/complete";
    public static final String VARIABLE_FASE = "fase";
    public static final String VARIABLE_ID_TIPO_PROCEDIMENTO = "idTipoProcedimento";
    public static final String VARIABLE_ID_TIPO_ATTIVAZIONE = "idTipoAttivazione";
    public static final String VARIABLE_CURRENT_STATE = "currentState";
    public static final String VARIABLE_STATE = "state";
    public static final String VARIABLE_NEXT_STATE = "nextState";
    public static final String VARIABLE_EMPLOYEE_CATEGORY = "employeeCategory";
    public static final String VARIABLE_ROLE = "role";
    public static final String VARIABLE_TIPO_DOCUMENTO = "tipoDocumento";
    public static final String VARIABLE_STATO = "stato";
    public static final String VARIABLE_EDITABILE = "editabile";
    public static final String VARIABLE_VISTO_RAGIONERIA = "flVistoRagioneria";
    public static final String VARIABLE_ID_MOTIVO = "idMotivo";
    public static final String VARIABLE_CAT_PERSONALE_RICHIESTA = "catPersonaleRichiesta";
    public static final String VARIABLE_D01 = "D01";
    public static final String VARIABLE_C08 = "C08";
    public static final String VARIABLE_ANNI_SERVIZIO_EFF = "anniServizioEff";
    public static final String VARIABLE_CAT_MILITARE = "catMilitare";
    public static final String VARIABLE_STATO_RICHIESTA_ADHOC = "statoRichiestaAdHoc";
    public static final String VARIABLE_STATO_PROCEDIMENTO = "statoProcedimento";
    public static final String VARIABLE_STATO_DOCUMENTO = "statoDocumento";
    public static final String VARIABLE_END_EVENT = "endEvent";
    public static final String VARIABLE_SUB_STATO = "subStato";
    public static final String YES_OUTPUT = "yes";
    public static final String NO_OUTPUT = "No";

    //cess roles
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String ADMIN = "ADMIN";
    public static final String GRADUATED = "GRADUATED";
    public static final String OFFICER = "OFFICER";
    public static final String SUB_OFFICER = "SUB_OFFICER";
    public static final String ADMIN_ROLE_ID = ROLE_PREFIX + ADMIN;

    public static final String INSTRUCTOR_ROLE_PREFIX = ROLE_PREFIX + "INSTRUCTOR_";
    public static final String INSTRUCTOR_GRADUATED_ROLE_ID = INSTRUCTOR_ROLE_PREFIX + GRADUATED;
    public static final String INSTRUCTOR_OFFICER_ROLE_ID = INSTRUCTOR_ROLE_PREFIX + OFFICER;
    public static final String INSTRUCTOR_SUB_OFFICER_ROLE_ID = INSTRUCTOR_ROLE_PREFIX + SUB_OFFICER;

    public static final String APPROVER_ROLE_PREFIX = ROLE_PREFIX + "APPROVER_";
    public static final String APPROVER_GRADUATED_ROLE_ID = APPROVER_ROLE_PREFIX + GRADUATED;
    public static final String APPROVER_OFFICER_ROLE_ID = APPROVER_ROLE_PREFIX + OFFICER;
    public static final String APPROVER_SUB_OFFICER_ROLE_ID = APPROVER_ROLE_PREFIX + SUB_OFFICER;

    public static final String SIGNATURE_ROLE_PREFIX = ROLE_PREFIX + "SIGNATURE_";
    public static final String SIGNATURE_GRADUATED_ROLE_ID = SIGNATURE_ROLE_PREFIX + GRADUATED;
    public static final String SIGNATURE_OFFICER_ROLE_ID = SIGNATURE_ROLE_PREFIX + OFFICER;
    public static final String SIGNATURE_SUB_OFFICER_ROLE_ID = SIGNATURE_ROLE_PREFIX + SUB_OFFICER;

    public static final String MANAGER_ROLE_PREFIX = ROLE_PREFIX + "MANAGER_";
    public static final String MANAGER_GRADUATED_ROLE_ID = MANAGER_ROLE_PREFIX + GRADUATED;
    public static final String MANAGER_OFFICER_ROLE_ID = MANAGER_ROLE_PREFIX + OFFICER;
    public static final String MANAGER_SUB_OFFICER_ROLE_ID = MANAGER_ROLE_PREFIX + SUB_OFFICER;

    public static final String ID = "id";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String ID_ASSIGNED_TO = "idAssignedTo";
    public static final String ASSIGNED_TO = "assignedTo";
    public static final String CODE_PROCESS = "codeProcess";
    public static final String STATE_PROCEDURE = "stateProcedure";
    public static final String ASSIGNMENT_DATE = "assignmentDate";
    public static final String FASE_PROCEDURE = "faseProcedure";
    public static final String OPENING_CESSATION = "openingCessation";
    public static final String TYPE_CESSATION = "typeCessation";
    public static final String DATA_DOCRICHIESTA = "dataDocRichiesta";
    public static final String DATA_PRESDOCRICH = "dataPresDocRich";
    public static final String DATA_PROTISTANZA = "dataProtIstanza";
    public static final String DATA_DECORRENZA = "dataDecorrenza";
    public static final String DATA_PROTISTANZA_PEC = "dataProtIstanzaPec";
    public static final String CODE_STATE = "codeState";
    public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
    public static final String PRIORITY = "priorita";
    public static final String NUM_VERB_COMM_AV = "numVerbCommAv";
    public static final String NUM_VERB_COMM_AV_DESC = "Numero Verbale Commissione di Avanzamento";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PENSION_MOD_COMPILAZIONE_MANUALE = "M";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String FLAG_ATTUALE = "flagAttuale";
    public static final String FLAG_LAVORATO = "flagLavorato";
    public static final String ROLE_CODE = "roleCode";
    public static final String ID_PROC = "idProc";
    public static final String PROCEDURE_IDS = "procedureIds";
    public static final String ID_STATO = "idStato";
    public static final String PROCEDURE_ASSIGNMENTS = "procedureAssignments";
    public static final String PROCEDURE_HISTORIES = "procedureHistories";
    public static final String NUMERO_ATTO_SIPAD = "numAttoSipad";
    public static final String ANNO_ATTO_SIPAD = "annoAttoSipad";

    //cetip
    public static final String CETIP_PERMANENTLY_UNSUITABLE = "BD169";
    public static final String CETIP_STFAA_SEQ_PK = "cetipStfaaSeqPk";
    public static final String CETIP_SGCTP_SEQ_PK = "cetipSgctpSeqPk";
    public static final String CETIP_PRTPO_SEQ_PK = "cetipPrtpoSeqPk";

    //note
    public static final String NOTE_SEPARATOR = "*";
    public static final String NOTES_SEPARATOR = "//";
    public static final String NOTE_SAVED = "Annotazione salvata correttamente.";

    public static final String CHANGE_STATE_OK = "Stato %s cambiato correttamente";

    public static final String STRING = "String";
    public static final String BOOLEAN = "Boolean";
    public static final String INTEGER = "Integer";
    public static final String SHOW_TAB = "showTab";
    public static final String PROCEDURE_DEFAULT_ORDER = "default";
    public static final String ALL = "ALL";
    public static final String ME = "ME";
    public static final String ASSEGNATI = "ASSEGNATI";
    public static final String LAVORATI = "LAVORATI";

    //declaration and controls
    public static final String CONTROL_01 = "C01";
    public static final String CONTROL_05 = "C05";
    public static final String CONTROL_06 = "C06";
    public static final String CONTROL_08 = "C08";
    public static final String CONTROL_11 = "C11";
    public static final String CONTROL_13 = "C13";
    public static final String CONTROL_14 = "C14";
    public static final String DECLARATION_01 = "D01";

    public static final String DO_007_COD_FISC = "do007CodFisc";
    public static final String DO_007_PRPRO_COD_PRO = "do007PrproCodPro";
    public static final String DO_007_PRPRO_ID = "do007PrproId";
    public static final String DO_007_DATA_AVVIO = "do007DataAvvio";
    public static final String DO_007_DATA_FINE = "do007DataFine";
    public static final String DO_007_STATO = "do007Stato";
    public static final String DO_007_FASE = "do007Fase";
    public static final String DO_007_TIPO_PROC = "do007TipoProc";
    public static final String DO_007_AUTORE = "do007Autore";

    public static final String COD_FISC = "codFisc";
    public static final String PRPRO_ID = "prproId";
    public static final String PRPRO_COD_PRO = "prproCodPro";
    public static final String DATA_AVVIO = "dataAvvio";
    public static final String DATA_FINE = "dataFine";
    public static final String FASE = "fase";
    public static final String STATO = "stato";
    public static final String TIPO_PROC = "tipoProc";
    public static final String AUTORE = "autore";

    //stato giuridico
    public static final String MILITARE_IN_CONGEDO = "AE110";
    public static final String CONGEDO = "AF53";

    //docs
    public static final String DECRETO_PRESIDENZIALE = "DECRETO PRESIDENZIALE";
    public static final String SENTENZA = "SENTENZA";
    public static final String ORDINANZA = "ORDINANZA";
    public static final String LETTERA_TRASMISSIONE = "Lettera trasmissione";
    public static final String DOC_OGGETTO = "n. atto SIPAD %s";
    public static final String NUM_ATTO_SIPAD = "%d del %d CE";
    public static final String LETTERA = "LET";
    public static final String LETTERA_RICHIESTA = "Lettera di richiesta";
    public static final String DOCAT_DATA_INIZIO = "0001-01-01";
    public static final String DOCAT_DATA_FINE = "9999-12-31";
    public static final String CICLO_ADHOC_4_VIA = "CICLO_ADHOC_4_VIA";
    public static final String TIPO_SOTTOFASCICOLO_TITOLARIO_DEFAULT = "Stato_Giuridico_-_Cessazione";

    public static final String TIPO_COLLEGAMENTO_ALLEGATO_USCITA = "U";
    public static final String COD_TIPO_ALLEGATO_RISPOSTA = "R";
    public static final String COD_TIPO_ALLEGATO_ADHOC = "A";

    public static final String LIMITI_ETA = "CszLimitiEtà";
    public static final String SU_ISTANZA_DI_PARTE = "ISTPAR";

    public static final String REASSIGNMENT_OK = "Riassegnazione completata con successo";

    public static final String REGISTRO_DECRETI = "D";
    public static final String REGISTRO_GENERALE = "G";

    public static final String FASE_CHIUSURA = "CHIUSURA";

}
