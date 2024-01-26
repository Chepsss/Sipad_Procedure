package it.almaviva.difesa.cessazione.procedure.model.dto.allegati;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DocProtocollatiInputDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idProcedura;
    private Long idAllegato;
    private List<Long> idDocumentiSelezionati = new ArrayList<>();
    private List<Long> idDocumenti = new ArrayList<>();

}
