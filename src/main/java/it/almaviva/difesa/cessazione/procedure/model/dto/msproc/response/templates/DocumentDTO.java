package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class DocumentDTO {

    /**
     * file content in base64 format
     */
    private String file;
    private ArrayList<String> placeholderErrors;
    private Boolean error;
    private Long templateId;

}
