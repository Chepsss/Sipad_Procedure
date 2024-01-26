package it.almaviva.difesa.cessazione.procedure.model.dto.rest.document;

import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.documenti.document.utils.FileNameUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class DocEsternoDTO implements Serializable {

    private static final long serialVersionUID = -4131955476078088521L;

    private String dataProtocollo;
    private String tipoAllegato;
    @Size(max = 90)
    @Pattern(message = ErrorsConst.FILE_NAME_NOT_VALID, regexp = FileNameUtils.pattern)
    private String nomeFile;
    private String mittente;
    private String mail;
    private Long objectID;
    private Long numeroProtocollo;
    private String segnatura;
    private String oggetto;

}
