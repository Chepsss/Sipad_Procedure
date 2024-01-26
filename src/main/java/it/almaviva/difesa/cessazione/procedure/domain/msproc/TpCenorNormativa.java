package it.almaviva.difesa.cessazione.procedure.domain.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.common.GenericEntity;
import it.almaviva.difesa.cessazione.procedure.domain.common.PreventAnyDataManipulation;
import it.almaviva.difesa.cessazione.procedure.domain.common.SortConstant;
import it.almaviva.difesa.cessazione.procedure.domain.common.Sortable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.NONE)
@Entity
@Table(name = "TP_CENOR_NORMATIVA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners(PreventAnyDataManipulation.class)
public class TpCenorNormativa implements GenericEntity, Serializable, Sortable {

    private static final long serialVersionUID = 3506212680304001104L;

    @Id
    @Column(name = "CENOR_SEQ_PK", nullable = false)
    private Long id;

    @Column(name = "CENOR_DESCR_NORMATIVA", length = 100)
    private String cenorDescrNormativa;

    @Column(name = "CENOR_NOTA_NORMATIVA", length = 200)
    private String cenorNotaNormativa;

    @Column(name = "CENOR_DATA_INIZ", nullable = false)
    private LocalDate cenorDataIniz;

    @Column(name = "CENOR_DATA_FINE", length = 20)
    private LocalDate cenorDataFine;

    @Column(name = "CENOR_DATA_INS", nullable = false)
    private LocalDateTime cenorDataIns;

    @Column(name = "CENOR_DATA_ULT_AGG", nullable = false)
    private LocalDateTime cenorDataUltAgg;

    @Column(name = "CENOR_COD_ULT_AGG", nullable = false, length = 50)
    private String cenorCodUltAgg;

    @OneToMany(mappedBy = "cetipCenorSeqPk", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TpCetipCessazione> tpCetipCessaziones = new LinkedHashSet<>();

    @Override
    public Sort getSort() {
        return SortConstant.SORT_BY_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TpCenorNormativa that = (TpCenorNormativa) o;
        return id.equals(that.id)
                && Objects.equals(cenorDescrNormativa, that.cenorDescrNormativa)
                && Objects.equals(cenorNotaNormativa, that.cenorNotaNormativa)
                && Objects.equals(cenorDataIniz, that.cenorDataIniz)
                && Objects.equals(cenorDataFine, that.cenorDataFine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cenorDescrNormativa, cenorNotaNormativa, cenorDataIniz, cenorDataFine);
    }

}