package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateTypeDTO implements Serializable {

    private static final long serialVersionUID = -1384960880123917031L;
    private Long id;
    private String description;
    private String docAcr;
    private String docType;

}
