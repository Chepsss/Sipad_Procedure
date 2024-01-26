package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MatchPlaceholdersDTO {

    /* true (default) if placeholders match with template ones */
    private Boolean placeholdersDoMatch = true;

    /* Template placeholder names,
     empty if no placeholders are present in a Template */
    private String nomiPlaceholders = "";

}
