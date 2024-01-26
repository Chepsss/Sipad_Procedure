package it.almaviva.difesa.cessazione.procedure.domain.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.common.GenericEntity;
import it.almaviva.difesa.cessazione.procedure.domain.common.SortConstant;
import it.almaviva.difesa.cessazione.procedure.domain.common.Sortable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

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
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_ce_dich_proc")
public class DeclarationProcedure extends AbstractAuditingEntity implements GenericEntity, Serializable, Sortable {

    private static final long serialVersionUID = -5218577061511046793L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "declarationProcGenerator")
    @SequenceGenerator(name = "declarationProcGenerator", sequenceName = "declaration_proc_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proc", nullable = false)
    private Procedure idProc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dich", nullable = false)
    private Declaration idDich;

    @Column(name = "flag_dich")
    private Boolean flagDich;

    @Column(name = "data_dich")
    private LocalDate dataDich;

    @Override
    public Sort getSort() {
        return SortConstant.SORT_BY_ID_DICH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeclarationProcedure that = (DeclarationProcedure) o;
        return id.equals(that.id)
                && Objects.equals(idProc, that.idProc)
                && Objects.equals(idDich, that.idDich)
                && Objects.equals(flagDich, that.flagDich)
                && Objects.equals(dataDich, that.dataDich);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idProc, idDich, flagDich, dataDich);
    }

}