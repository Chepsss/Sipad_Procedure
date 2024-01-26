package it.almaviva.difesa.cessazione.procedure.domain.msproc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.almaviva.difesa.cessazione.procedure.domain.common.GenericEntity;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A ProcedureAssignment.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_ce_proc_assegnazioni")
public class ProcedureAssignment extends AbstractAuditingEntity implements GenericEntity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "procedureAssignmentGenerator")
    @SequenceGenerator(name = "procedureAssignmentGenerator", sequenceName = "proc_assegnazioni_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proc_storico")
    @JsonIgnoreProperties(value = {"procedure"}, allowSetters = true)
    private ProcedureHistory procedureHistory;

    @NotNull
    @Column(name = "id_assegnato_a", nullable = false)
    private Long idAssignedTo;

    @NotNull
    @Column(name = "data_inizio")
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "data_fine")
    private LocalDateTime endDate;

    @NotNull
    @Column(name = "flag_definitivo", nullable = false)
    private Boolean definitive = false;

    @NotNull
    @Column(name = "flag_lavorato", nullable = false)
    private Boolean flagLavorato = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcedureAssignment)) {
            return false;
        }
        return id != null && id.equals(((ProcedureAssignment) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcedureAssignment{" +
                "id=" + getId() +
                ", idAssignedTo=" + getIdAssignedTo() +
                ", startDate='" + getStartDate() + "'" +
                "}";
    }

}
