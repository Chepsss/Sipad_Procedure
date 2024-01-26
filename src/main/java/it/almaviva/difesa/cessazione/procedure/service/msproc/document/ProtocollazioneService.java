package it.almaviva.difesa.cessazione.procedure.service.msproc.document;

import it.almaviva.difesa.cessazione.procedure.model.dto.rest.DocumentListDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.ProtocollazioneMessage;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.StatoPredisposizioneMessage;
import it.almaviva.difesa.documenti.document.model.dto.request.protocollazioneIngresso.ProtocolloIngressoDto;
import it.almaviva.difesa.documenti.document.model.dto.request.protocollazioneUscita.ProtocolloUscitaDto;
import it.almaviva.difesa.documenti.document.model.dto.response.DocumentRegistrationResponse;
import it.almaviva.difesa.documenti.document.model.dto.response.predisposizione.PredisposizioneResponse;

public interface ProtocollazioneService {

    /**
     * Protocollazione in ingresso decreto
     */
    DocumentRegistrationResponse ingresso(ProtocolloIngressoDto input);

    /**
     * Terza via lettera di trasmissione e altre lettere
     */
    PredisposizioneResponse uscita(ProtocolloUscitaDto input);

    DocumentListDto checkUscita(ProtocolloUscitaDto input);

    /**
     * Stato predisposizione
     */
    StatoPredisposizioneMessage stato(Long idDocumento);

    /**
     * Check ammissibilità quarta via
     */
    ProtocollazioneMessage previewProtocollo(ProtocolloUscitaDto input);

    /**
     * Quarta via
     */
    PredisposizioneResponse protocolla(ProtocolloUscitaDto input);

}
