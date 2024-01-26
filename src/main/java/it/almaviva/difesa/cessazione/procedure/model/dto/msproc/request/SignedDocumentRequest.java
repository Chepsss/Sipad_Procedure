package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SignedDocumentRequest {

    @NotNull
    private Long id;
    @NotNull
    private String file;

}
