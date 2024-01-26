package it.almaviva.difesa.cessazione.procedure.model.dto.allegati;

import it.almaviva.difesa.documenti.document.model.dto.response.documenti.DestinatariPredOutput;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DocumentProtocollatoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nomeFile;
    private String richiestaAdhoc;
    private String protocollo;
    private List<DestinatariPredOutput> destinatari;
    private List<DestinatariPredOutput> destinatariInterni;
    private Boolean collegato;

}
