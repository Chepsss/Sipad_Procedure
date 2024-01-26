package it.almaviva.difesa.cessazione.procedure.model.dto.sipad;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TpDostaStatoDTO implements Serializable {

    private static final long serialVersionUID = 4270579760217605345L;

    private Long id;
    private String descrSta;
    private String acrSta;
    private Date dataIniz;
    private Date dataFine;

}
