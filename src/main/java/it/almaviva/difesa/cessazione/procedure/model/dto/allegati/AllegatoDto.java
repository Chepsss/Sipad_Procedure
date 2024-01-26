package it.almaviva.difesa.cessazione.procedure.model.dto.allegati;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AllegatoDto implements Serializable {

    private Long id;
    private Long idProc;

    /**
     * Valori ammessi: 'L','A','R'
     */
    private String codTipoAllegato;

    /**
     * Valori ammessi: 'Allegato da locale', 'Allegato da Adhoc', 'Risposta'
     */
    private String tipoAllegato;

    /**
     * num protocollo (se allegato da AdHoc)
     */
    private String numProtocollo;

    /**
     * data protocollo (se allegato da AdHoc)
     */
    private String dataProtocollo;

    /**
     * id file protocollo (se allegato da AdHoc)
     */
    private Long idFile;

    /**
     * mittente protocollo (se allegato da AdHoc)
     */
    private Long mittente;

    /**
     * descrizione dell'allegato
     */
    private String descrizione;

    /**
     * nome del file
     */
    private String nomeFile;

    /**
     * utente che ha caricato l'allegato
     */
    private String caricatoDa;

    // prova cr14
    private String firstName;
    private String lastName;

    /**
     * Data inserimento
     */
    private String dataIns;

    /**
     * ID del documento risposta
     */
    private Long idDocPrincipale;

    /**
     * Numero di Protocollo della risposta
     */
    private List<String> protocolloRisp;

    private String file;
    private AllegatoActionsDto azioni;

    private String nomeCognome;

}
