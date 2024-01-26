package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TemplateInfoDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String name;
    private String format;
    private String author;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;
    private LocalDate creationDate;
    private LocalDate updateDate;

}
