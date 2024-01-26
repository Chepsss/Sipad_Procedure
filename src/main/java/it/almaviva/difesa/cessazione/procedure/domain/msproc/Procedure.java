package it.almaviva.difesa.cessazione.procedure.domain.msproc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.almaviva.difesa.cessazione.procedure.domain.common.GenericEntity;
import it.almaviva.difesa.cessazione.procedure.domain.common.SortConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A Procedure.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_ce_procedura")
public class Procedure extends AbstractAuditingEntity implements GenericEntity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "procedureGenerator")
    @SequenceGenerator(name = "procedureGenerator", sequenceName = "procedure_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_procedimento")
    private String codeProcess;

    @Column(name = "bpmn_process_id", unique = true)
    private String bpmnProcessId;

    @NotNull
    @Column(name = "id_autore", nullable = false)
    private Long idAuthor;

    @Column(name = "autore")
    private String author;

    @NotNull
    @Column(name = "data_assegnazione", nullable = false)
    private LocalDateTime assignmentDate;

    @NotNull
    @Column(name = "ce_ute_andip_id", nullable = false)
    private Long employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_stato_procedura")
    private StateProcedure stateProcedure;

    @OneToMany(mappedBy = "procedure", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"stateProcedure", "faseProcedure", "procedure"}, allowSetters = true)
    private Set<ProcedureHistory> procedureHistories = new LinkedHashSet<>();

    @Column(name = "id_tipo_attivazione")
    private Long openingCessation;

    @Column(name = "id_tipo_procedimento")
    private Long typeCessation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_cessazione")
    private TpCetipCessazione reasonCessation; // Motivo cessazione

    @Column(name = "id_cat_pers_richiesta")
    private String categLeaveReq;

    @Column(name = "id_cat_pers_spettante")
    private String idCatPersSpettante;

    @Column(name = "id_cat_mil")
    private Long idCatMilitare; // Categoria personale militare

    @Column(name = "id_tipo_comunicazione")
    private Long catDocumento;

    @Column(name = "id_ente")
    private String idEnte;

// inserito per evolutive cr16
    @Column(name = "id_ente_cc1") // cr16
    private String idEnte_cc1;

    @Column(name = "id_ente_cc2")// cr16
    private String idEnte_cc2;

    @Column(name = "id_ente_cc3")// cr16
    private String idEnte_cc3;
// fine cr16
    @Column(name = "data_doc_richiesta")
    private LocalDateTime dataDocRichiesta;

    @Column(name = "data_pres_doc_rich")
    private LocalDateTime dataPresDocRich;

    @Column(name = "data_decorrenza")
    private LocalDateTime dataDecorrenza;

    @Column(name = "fl_visto_ragioneria")
    private Boolean flVistoRagioneria = false;

    @Column(name = "data_ragg_eta")
    private LocalDateTime dataRaggEta;

    @Column(name = "data_gml")
    private LocalDateTime dataGml;

    @Column(name = "organo_sanita")
    private String organoSanita;

    @Column(name = "mod_verbale_gml")
    private String modVerbaleGml;

    @Column(name = "num_verb_gml")
    private String numVerbGml;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_gml_1")
    private TpCegmlGiudMedLegale tpCegmlGiudMedLegale;

    @Column(name = "prot_istanza")
    private String protIstanza;

    @Column(name = "data_prot_istanza")
    private LocalDateTime dataProtIstanza;

    @Column(name = "prot_istanza_pec")
    private String protIstanzaPec;

    @Column(name = "data_prot_istanza_pec")
    private LocalDateTime dataProtIstanzaPec;

    @Column(name = "fl_impugna_gml")
    private Boolean flImpugnaGml = false;

    @Column(name = "fl_gml_concordi")
    private Boolean flGmlConcordi = false;

    @Column(name = "data_gml_appello")
    private LocalDateTime dataGmlAppello;

    @Column(name = "n_verb_gml_appello")
    private String nVerbGmlAppello;

    @Column(name = "prot_verb_gml_appello")
    private String protVerbGmlAppello;

    @Column(name = "data_prot_verb_gml_appello")
    private LocalDateTime dataProtVerbGmlAppello;

    @Column(name = "prot_verb_gml_app_pec")
    private String protVerbGmlAppPec;

    @Column(name = "data_prot_verb_gml_app_pec")
    private LocalDateTime dataProtVerbGmlAppPec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_gml_2")
    private TpCegmlGiudMedLegale tpCegmlGiudMedLegale2;

    @Column(name = "id_provincia_res")
    private Long idProvincia;

    @Column(name = "id_comune_res")
    private Long idComune;

    @Column(name = "cap_res")
    private String capRes;

    @Column(name = "indirizzo_res")
    private String indirizzoRes;

    @Column(name = "id_nazione_res")
    private Long idNazione;

    @Column(name = "num_verb_comm_av")
    private String numVerbCommAv;

    @OneToOne(mappedBy = "tbCeProcedura", orphanRemoval = true)
    private TbCeProcTransito tbCeProcTransito;

    @OneToMany(mappedBy = "idProc", orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy(value = SortConstant.ID_DICH + " ASC")
    private List<DeclarationProcedure> tpCeDichProcs = new ArrayList<>();

    @OneToOne(mappedBy = "tbCeProcedura", orphanRemoval = true)
    private TbCeProcPensione tbCeProcPensione;

    @OneToOne(mappedBy = "tbCeProcedura", orphanRemoval = true)
    private TbCeProcParereRag tbCeProcParereRag;

    @Column(name = "data_avvio")
    private LocalDateTime dataAvvio;

    @Column(name = "data_scad_lav")
    private LocalDateTime dataScadLav;

    @Column(name = "note")
    private String note;

    @Column(name = "stgce_flag_elaborato")
    // "N": Non elaborato, "S": Elaborato con successo, "E": Elaborato con errore, "A": Esecuzione procedura abortita
    private String stgceFlagElaborato;

    @Column(name = "stgce_elab_msg")
    private String stgceElabMsg;

    @NotNull
    @Column(name = "priorita")
    private Integer priorita = 5;


    @Column(name = "num_atto_sipad")
    private Long numAttoSipad;

    @Column(name = "anno_atto_sipad")
    private Integer annoAttoSipad;

    @Column(name = "prom_tit_onor")
    private Boolean promTitOnor;


    public void setTpCeDichProcs(List<DeclarationProcedure> tpCeDichProcs) {
        this.tpCeDichProcs.clear();
        if (!tpCeDichProcs.isEmpty()) {
            this.tpCeDichProcs.addAll(tpCeDichProcs);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Procedure)) {
            return false;
        }
        return id != null && id.equals(((Procedure) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Procedure{" +
                "id=" + getId() +
                ", codeProcess=" + getCodeProcess() +
                ", idAuthor=" + getIdAuthor() +
                ", author='" + getAuthor() + "'" +
                ", assignmentDate='" + getAssignmentDate() + "'" +
                ", employeeId=" + getEmployeeId() +
                "}";
    }

}
