package it.almaviva.difesa.cessazione.procedure.model.dto.msproc;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.validation.annotations.CheckDateValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link Procedure} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CheckDateValid
public class ProcedureOpeningDataDTO implements Serializable {

    private static final long serialVersionUID = -6119178740089364138L;

    private Long id;
    private String codeProcess;
    @NotNull
    private Long idAuthor;
    @NotNull
    private Long idAssignedTo;
    @NotNull
    private Long employeeId;
    @NotNull
    private Long stateProcedureId;

    // Modalità apertura cessazione
    private Long idTipoAttivazione;

    // Tipologia accesso cessazione
    private Long idTipoProcedimento;

    // Motivo cessazione
    private Long idTipoCessazione;

    // Categoria congedo richiesta/destinazione
    private String idCatPersRichiesta;

    //Categoria congedo spettante
    private String idCatPersSpettante;

    // Categoria personale militare
    private Long idCatPers;

    private Long idTipoComunicazione;

    private String idEnte;// ente / organo amministrativo

    //cr16
    private String idEnte_cc1;// ente / cc 1
    private String idEnte_cc2;// ente / cc 2
    private String idEnte_cc3;// ente / cc 3
    // fine cr16
    private String idTipoGml1;
    private String idTipoGml2;

    private LocalDateTime dataDocRichiesta;
    private LocalDateTime dataPresDocRich;
    private LocalDateTime dataDecorrenza;
    private Boolean flVistoRagioneria;
    private LocalDateTime dataRaggEta;
    private LocalDateTime dataGml;
    @Size(max = 100, message = ErrorsConst.MAX_INSERT_ERROR)
    private String organoSanita;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String modVerbaleGml;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String numVerbGml;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protIstanza;
    private LocalDateTime dataProtIstanza;
    @Size(max = 50, message = ErrorsConst.MAX_INSERT_ERROR)
    private String protIstanzaPec;
    private LocalDateTime dataProtIstanzaPec;
    private Boolean flImpugnaGml;
    private Boolean flGmlConcordi;
    private LocalDateTime dataGmlAppello;
    @Size(max = 20, message = ErrorsConst.MAX_INSERT_ERROR)
    private String nVerbGmlAppello;
    private String protVerbGmlAppello;
    private LocalDateTime dataProtVerbGmlAppello;
    private String protVerbGmlAppPec;
    private LocalDateTime dataProtVerbGmlAppPec;
    private Boolean promTitOnor;

}
