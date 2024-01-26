package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.DeclarationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeclarationProcedureDTOResponse implements Serializable {

    private static final long serialVersionUID = -3735102453193139548L;

    private Long id;
    private DeclarationDTO idDich;
    private Boolean flagDich;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.YYYY_MM_DD_HH_MM_SS)
    private Date dataDich;

}
