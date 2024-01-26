package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StgceCessazioneReqDTO {

    @NotNull
    private String codiceFiscale;
    @NotNull
    private String numeroDecreto;

}
