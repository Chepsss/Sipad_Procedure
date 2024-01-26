package it.almaviva.difesa.cessazione.procedure.domain.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.common.GenericEntity;
import it.almaviva.difesa.cessazione.procedure.domain.common.PreventAnyDataManipulation;
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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TP_CECON_CATEG_CONG")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners(PreventAnyDataManipulation.class)
public class TpCeconCategCong implements GenericEntity, Serializable, Sortable {

    private static final long serialVersionUID = -6913395367929384652L;

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CECON_CETIP_SEQ_PK", nullable = false)
    private TpCetipCessazione ceconCetipSeqPk;

    @Column(name = "CECON_SGTPO_SEQ_PK", nullable = false)
    private String ceconSgtpoSeqPk; //TpSgtpoPosizioneStato

    @Column(name = "CECON_SGTPO_GDL", length = 12)
    private String ceconSgtpoGdl;

    @Column(name = "CECON_DATA_INS", nullable = false)
    private LocalDateTime ceconDataIns;

    @Column(name = "CECON_DATA_ULT_AGG", nullable = false)
    private LocalDateTime ceconDataUltAgg;

    @Column(name = "CECON_COD_ULT_AGG", nullable = false, length = 50)
    private String ceconCodUltAgg;

    @Override
    public Sort getSort() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TpCeconCategCong that = (TpCeconCategCong) o;
        return ceconSgtpoGdl.equals(that.ceconSgtpoGdl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ceconSgtpoGdl);
    }

}