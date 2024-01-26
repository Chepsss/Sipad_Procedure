package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class TemplateGenerationDTO {

    private HashMap<String, Object> model;
    private Long templateId;
    private Boolean isPdf;
    private Boolean force;
    private String file;
    private String styleCss;

}
