package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TemplateCriteria {

    private String name;
    private String format;
    private String author;
    private Long templateTypeId;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private LocalDate creationDate;
    private LocalDate updateDate;

}
