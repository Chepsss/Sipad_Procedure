package it.almaviva.difesa.cessazione.procedure.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentListDto implements Serializable {

    private List<DocTipoENomeDto> files;
    private List<DocTipoENomeDto> allegati;
    private String message;

}
