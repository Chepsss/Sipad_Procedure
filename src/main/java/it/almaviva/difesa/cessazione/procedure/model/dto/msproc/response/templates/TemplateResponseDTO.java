package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TemplateResponseDTO extends TemplateInfoDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long templateTypeId;

}
