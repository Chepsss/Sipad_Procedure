package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateTypeCriteriaDTO implements Serializable {

    private static final long serialVersionUID = 6614787549208618625L;
    private String docType;

}
