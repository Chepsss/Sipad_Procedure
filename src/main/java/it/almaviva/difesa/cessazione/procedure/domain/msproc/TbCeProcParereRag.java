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
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
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
@Table(name = "tb_ce_proc_parere_rag")
public class TbCeProcParereRag extends AbstractAuditingEntity implements GenericEntity, Serializable, Sortable {

    private static final long serialVersionUID = -1832064818190098883L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parereRagProcGenerator")
    @SequenceGenerator(name = "parereRagProcGenerator", sequenceName = "parere_rag_proc_sequence", allocationSize = 1)
    @Column(name = "id_proc", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proc", nullable = false)
    private Procedure tbCeProcedura;

    @Column(name = "esito")
    private Boolean esito;

    @Column(name = "num_registrazione")
    private Long numRegistrazione;

    @Column(name = "data_esito")
    private LocalDate dataEsito;

    @Override
    public Sort getSort() {
        return SortConstant.SORT_BY_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbCeProcParereRag that = (TbCeProcParereRag) o;
        return id.equals(that.id)
                && Objects.equals(esito, that.esito)
                && Objects.equals(numRegistrazione, that.numRegistrazione)
                && Objects.equals(dataEsito, that.dataEsito);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, esito, numRegistrazione, dataEsito);
    }

}