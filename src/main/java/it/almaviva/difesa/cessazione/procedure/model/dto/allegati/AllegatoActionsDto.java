package it.almaviva.difesa.cessazione.procedure.model.dto.allegati;

import lombok.Data;

import java.io.Serializable;

@Data
public class AllegatoActionsDto implements Serializable {

    private Boolean visualizza;
    private Boolean assDissProtocolloUscita;
    private Boolean assDissPredisposizione;
    private Boolean elimina;

}
