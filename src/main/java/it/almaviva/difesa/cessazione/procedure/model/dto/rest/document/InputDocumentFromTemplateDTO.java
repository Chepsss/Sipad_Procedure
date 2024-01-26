package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.documenti.document.utils.FileNameUtils;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class InputDocumentFromTemplateDTO implements Serializable {

    private static final long serialVersionUID = 2923410709701275414L;

    private Long idProcedura;
    @Size(max = 90)
    @Pattern(message = ErrorsConst.DOCUMENT_NAME_NOT_VALID, regexp = FileNameUtils.pattern)
    private String nomeFile;
    private String idTipo;
    private String file;
    private Boolean force = false;
    @Size(max = 90)
    @Pattern(message = ErrorsConst.DOCUMENT_NAME_NOT_VALID, regexp = FileNameUtils.pattern)
    private String nomeLetteraTrasmissione;
    private String modello;

}
