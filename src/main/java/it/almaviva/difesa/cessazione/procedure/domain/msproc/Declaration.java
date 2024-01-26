package it.almaviva.difesa.cessazione.procedure.domain.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.common.GenericEntity;
import it.almaviva.difesa.cessazione.procedure.domain.common.PreventAnyDataManipulation;
import it.almaviva.difesa.cessazione.procedure.domain.common.SortConstant;
import it.almaviva.difesa.cessazione.procedure.domain.common.Sortable;
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
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_ce_dichiarazioni")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners(PreventAnyDataManipulation.class)
public class Declaration extends AbstractAuditingEntity implements GenericEntity, Serializable, Sortable {

    private static final long serialVersionUID = 3065369347264897966L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "codice", nullable = false, length = 10)
    private String codice;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @Column(name = "flag_automatico", nullable = false)
    private Boolean flagAutomatico = false;

    @Column(name = "cod_tipo", nullable = false, length = 3)
    private String codTipo;

    @Column(name = "descr_tipo", nullable = false, length = 100)
    private String descrTipo;

    @Column(name = "data_iniz", nullable = false)
    private LocalDate dataIniz;

    @Column(name = "data_fine", nullable = false)
    private LocalDate dataFine;

    @OneToMany(mappedBy = "idDich", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<DeclarationProcedure> declarationProcedures = new LinkedHashSet<>();

    @Override
    public Sort getSort() {
        return SortConstant.SORT_BY_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Declaration that = (Declaration) o;
        return id.equals(that.id)
                && Objects.equals(codice, that.codice)
                && Objects.equals(descrizione, that.descrizione)
                && Objects.equals(flagAutomatico, that.flagAutomatico)
                && Objects.equals(codTipo, that.codTipo)
                && Objects.equals(descrTipo, that.descrTipo)
                && Objects.equals(dataIniz, that.dataIniz)
                && Objects.equals(dataFine, that.dataFine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codice, descrizione, flagAutomatico, codTipo, descrTipo, dataIniz, dataFine);
    }

}
