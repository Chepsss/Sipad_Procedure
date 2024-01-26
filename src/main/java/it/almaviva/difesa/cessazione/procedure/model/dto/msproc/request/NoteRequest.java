package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class NoteRequest implements Serializable {

    private static final long serialVersionUID = -6067363838384706686L;

    private Long procedureId;
    private String author;
    private LocalDate date;
    private String note;

}
