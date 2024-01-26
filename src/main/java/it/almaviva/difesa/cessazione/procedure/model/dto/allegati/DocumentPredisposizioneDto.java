package it.almaviva.difesa.cessazione.procedure.model.dto.allegati;

import lombok.Data;

import java.io.Serializable;

@Data
public class DocumentPredisposizioneDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nomeFile;
    private String stato;
    private String tipoAllegato;
    private Boolean collegato;

}
