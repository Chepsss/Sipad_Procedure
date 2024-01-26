package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.documenti_editor;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InputPreviewDocumentDTO implements Serializable {

    private Long idProc;
    private String content;

}
