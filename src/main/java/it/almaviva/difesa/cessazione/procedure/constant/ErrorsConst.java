package it.almaviva.difesa.cessazione.procedure.constant;

public class ErrorsConst {

    public static final String PROCEDURE_NOT_FOUND = "Procedimento con Id %d non trovato";
    public static final String BOZZA_NOT_FOUND = "Stato BOZZA non trovato";
    public static final String NATION_REQUIRED = "Nazione: campo obbligatorio in caso di provincia estera";
    public static final String DECLARATION_NOT_FOUND = "Dichiarazione/controllo con id %d non trovata/o";
    public static final String NOTE_UNSAVED = "Non è stato possibile salvare l'annotazione.";
    public static final String MAX_INSERT_ERROR = "Il valore inserito '${validatedValue}' supera il numero massimo di {max} caratteri";
    public static final String PRIORITY_ERROR = "Inserire un valore valido per assegnare una nuova priorità al procedimento [1-5]";
    public static final String DATE_GREATER_ERROR = "La data inserita è superiore alla data odierna";
    public static final String DATE_LOWER_ERROR = "La data inserita non è superiore o uguale alla data odierna";
    public static final String DATE_PRESDOCRICH_ERROR = "La data inserita non è superiore alla Data compilazione atto o istanza";
    public static final String DATE_DATAPROTISTANZA_ERROR = "La data inserita non è superiore alla Data di presentazione dell'atto all'Ente";
    public static final String DATE_DATAPROTISTANZA_PEC_ERROR = "La data inserita non è superiore alla Data di protocollo dell'atto all'Ente";
    public static final String TEMPLATE_NOT_FOUND = "Template non trovato";
    public static final String DECRETO_ALREADY_EXISTS = "Decreto già presente";
    public static final String CREATE_DOCUMENTS_ERROR = "Impossibile creare i nuovi documenti, esiste più di un template in corso di validità per la Lettera di Trasmissione";
    public static final String TEMPLATE_NOT_EXISTS = "Impossibile creare i nuovi documenti, non esiste un template in corso di validità per la Lettera di Trasmissione";
    public static final String DOCUMENT_NOT_FOUND = "Documento con id %d non trovato";
    public static final String UNAUTHORIZED_ACTION = "Operazione non consentita";
    public static final String SEGNATURA_NOT_FOUND = "Segnatura del documento non presente";
    public static final String GESTIONE_PROTOCOLLATA_ERROR_DECRETO_NULL = "gestioneProtocollata(): Il decreto è null";
    public static final String PROTOCOLLAZIONE_ADHOC_ERROR = "Errore protocollazione su adhoc";
    public static final String SEGNATURA_ERROR = "È necessario compilare la segnatura del decreto";
    public static final String PREDISPOSIZIONE_ERROR = "È necessario compilare la predisposizione della lettera di trasmissione";
    public static final String PREDISPOSIZIONE_NOT_FOUND = "È necessario compilare prima la predisposizione della lettera";
    public static final String DOCUMENTS_NOT_FOUND_OR_INVALID_LIST = "Documenti non disponibili o in stato non ammesso: %s";
    public static final String PROTOCOLLAZIONE_NOT_POSSIBLE = "Operazione non consentita. Non è presente un Decreto firmato e protocollato da trasmettere";
    public static final String PROTOCOL_NOT_VALID = "Numero di protocollo non valido.";
    public static final String PROTOCOLLAZIONE_USCITA_ERROR_MESSAGE = "Errore in protocollazione in uscita";
    public static final String RICERCA_PROTOCOLLI_ERROR_MESSAGE = "Errore nella ricerca protocolli su adhoc: %s";
    public static final String ERRORE_IN_REASSIGNMENT = "Errore in riassegnazione";
    public static final String ERRORE_IN_SAVE_NOTIFICATION = "Errore in inserimento notifica: %s";
    public static final String ERRORE_IN_SAVE_SIGNATURE = "Errore nel salvataggio della segnatura del decreto: %s";
    public static final String ERRORE_IN_SAVE_PREDISPOSITION = "Errore nel salvataggio della predisposizione: %s";
    public static final String PLACEHOLDERS_NOT_MATCH = "Salvataggio non effettuato. Non è possibile cancellare i placeholders originali del template";
    public static final String TEMPLATE_NOT_VALID = "Template non più in corso di validità.";
    public static final String CAP_NOT_VALID = "CAP non valido";
    public static final String DOCUMENT_NAME_NOT_VALID = "Nome documento non valido";
    public static final String FILE_NAME_NOT_VALID = "Nome file non valido";
    public static final String PROM_TIT_ONOR_REQUIRED = "Promozione a titolo onorifico: Campo obbligatorio";
    public static final String PROM_TIT_ONOR_NOT_VALID = "Promozione a titolo onorifico: Valore non ammesso per questo dipendente";

}
