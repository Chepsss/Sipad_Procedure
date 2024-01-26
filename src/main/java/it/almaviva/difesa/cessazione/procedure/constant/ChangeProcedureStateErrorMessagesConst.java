package it.almaviva.difesa.cessazione.procedure.constant;

public class ChangeProcedureStateErrorMessagesConst {

    public static final String STATE_NOT_FOUND = "Stato procedimento %s non trovato";
    public static final String CHANGE_STATE_NOT_POSSIBLE = "Il cambio stato non è possibile";
    public static final String UNAUTHORIZED = "Utente non autorizzato a questa operazione";
    public static final String PROCEDURE_ALREADY_EXISTS = "Esiste già un procedimento per: %s";
    public static final String DECRETO_NOT_FOUND = "Documento Decreto inesistente";
    public static final String LETTER_NOT_FOUND = "Lettera di trasmissione non trovata o inesistente";
    public static final String SEGNATURA_NOT_FOUND = "E' necessario compilare la segnatura del Decreto";
    public static final String PREDISPOSIZIONE_NOT_FOUND = "E' necessario compilare la predisposizione della Lettera di trasmissione";
    public static final String LETTER_NOT_SENT_TO_ADHOC = "Attenzione! Non puoi chiudere il procedimento senza aver inviato in AdHoc la lettera di trasmissione!";
    public static final String ACCOUNTING_REQUIRED = "Attenzione! Occorre compilare i dati del Parere della Ragioneria.";
    public static final String ERROR_PROCEDURE_CALL = "Errore nella chiamata alla procedura per l'aggiornamento dello stato giuridico.";
    public static final String ERROR_INSERT_STGCE_CESSAZIONE = "Attenzione! Errore nell'aggiornamento dello stato giuridico. Operazione annullata.";
    public static final String GO_TO_LAVORAZIONE_NOT_POSSIBLE = "Attenzione! Non è possibile portare il procedimento in Istruttoria perchè è già stato fatto un invio documenti ad AdHoc.";

}
