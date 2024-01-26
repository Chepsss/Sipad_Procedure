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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A ProcedureHistory.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_ce_proc_storico")
public class ProcedureHistory extends AbstractAuditingEntity implements GenericEntity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "procedureHistoryGenerator")
    @SequenceGenerator(name = "procedureHistoryGenerator", sequenceName = "proc_storico_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_assegnazione")
    private LocalDateTime assignmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proc")
    @JsonIgnoreProperties(value = {"procedureHistories", "stateProcedure", "faseProcedure"}, allowSetters = true)
    private Procedure procedure;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_stato_procedura", nullable = false)
    private StateProcedure stateProcedure;

    @NotNull
    @Column(name = "role_code", nullable = false)
    private String roleCode;

    @OneToMany(mappedBy = "procedureHistory", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"procedure"}, allowSetters = true)
    private Set<ProcedureAssignment> procedureAssignments = new LinkedHashSet<>();

    @NotNull
    @Column(name = "flag_attuale", nullable = false)
    private Boolean flagAttuale = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcedureHistory)) {
            return false;
        }
        return id != null && id.equals(((ProcedureHistory) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcedureHistory{" +
                "id=" + getId() +
                ", assignmentDate='" + getAssignmentDate() + "'" +
                "}";
    }

}
