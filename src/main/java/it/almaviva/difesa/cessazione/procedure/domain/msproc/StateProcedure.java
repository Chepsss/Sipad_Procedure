package it.almaviva.difesa.cessazione.procedure.domain.msproc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.almaviva.difesa.cessazione.procedure.domain.common.GenericEntity;
import it.almaviva.difesa.cessazione.procedure.domain.common.PreventAnyDataManipulation;
import it.almaviva.difesa.cessazione.procedure.domain.common.SortConstant;
import it.almaviva.difesa.cessazione.procedure.domain.common.Sortable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A StateProcedure.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_ce_stato_procedura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners(PreventAnyDataManipulation.class)
public class StateProcedure implements GenericEntity, Serializable, Sortable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "code_stato", nullable = false)
    private String codeState;

    @Column(name = "search_code", nullable = false)
    private String searchCode;

    @Column(name = "desc_stato", nullable = false)
    private String descState;

    @NotNull
    @Column(name = "code_stato_centrale", nullable = false)
    private String codeStateCentral;

    @Column(name = "ord_stato", nullable = false)
    private Integer ordState;

    @Column(name = "desc_stato_centrale", nullable = false)
    private String descStateCentral;

    @OneToMany(mappedBy = "stateProcedure", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"procedureHistories", "stateProcedure", "faseProcedure"}, allowSetters = true)
    private Set<Procedure> procedures = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fase_procedura")
    private FaseProcedure faseProcedure;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StateProcedure)) {
            return false;
        }
        return id != null && id.equals(((StateProcedure) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StateProcedure{" +
                "id=" + getId() +
                ", codeStat='" + getCodeState() + "'" +
                ", descState='" + getDescState() + "'" +
                "}";
    }

    @Override
    public Sort getSort() {
        return SortConstant.SORT_BY_ORD_STATE;
    }
}
