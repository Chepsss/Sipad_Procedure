package it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request;

import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.documenti.document.utils.FileNameUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ChangeDocNameRequest {

    @NotNull
    private Long id;
    @NotNull
    @Size(max = 90)
    @Pattern(message = ErrorsConst.DOCUMENT_NAME_NOT_VALID, regexp = FileNameUtils.pattern)
    private String nomeFile;

}
