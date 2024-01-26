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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
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
@Table(name = "TP_CETIP_CESSAZIONE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners(PreventAnyDataManipulation.class)
public class TpCetipCessazione implements GenericEntity, Serializable, Sortable {

    private static final long serialVersionUID = -6753806163665904104L;

    @Id
    @Column(name = "CETIP_ID_PK", nullable = false)
    private Long id;

    @Column(name = "CETIP_SGCTP_SEQ_PK", nullable = false)
    private Long cetipSgctpSeqPk;  //TpSgctpCatpersonale

    @Column(name = "CETIP_PRTPO_SEQ_PK", nullable = false)
    private Long cetipPrtpoSeqPk;  //TpPrtpoTprocedimento

    @Column(name = "CETIP_MOTIVO_CESSAZIONE", length = 200)
    private String cetipMotivoCessazione;

    @Column(name = "CETIP_SGRUO_SEQ_PK")
    private String cetipSgruoSeqPk;  //TpSgruoRuolo

    @Column(name = "CETIP_STFAA_SEQ_PK")
    private String cetipStfaaSeqPk;  //TpStfaaForzaArmata

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CETIP_CENOR_SEQ_PK")
    private TpCenorNormativa cetipCenorSeqPk;

    @Column(name = "CETIP_SGTPO_SEQ_PK", length = 20)
    private String cetipSgtpoSeqPk;

    @Column(name = "CETIP_DATA_INS", nullable = false)
    private LocalDateTime cetipDataIns;

    @Column(name = "CETIP_DATA_ULT_AGG", nullable = false)
    private LocalDateTime cetipDataUltAgg;

    @Column(name = "CETIP_COD_ULT_AGG", nullable = false, length = 50)
    private String cetipCodUltAgg;

    @Column(name = "CETIP_ACR_TIV", length = 100)
    private String cetipAcrTiv;

    @Column(name = "ID_MOTIVO", length = 100)
    private String idMotivo;

    @OneToMany(mappedBy = "reasonCessation", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Procedure> tbCeProceduras = new LinkedHashSet<>();

    @Override
    public Sort getSort() {
        return SortConstant.SORT_BY_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TpCetipCessazione that = (TpCetipCessazione) o;
        return id.equals(that.id)
                && Objects.equals(cetipMotivoCessazione, that.cetipMotivoCessazione)
                && Objects.equals(cetipSgtpoSeqPk, that.cetipSgtpoSeqPk)
                && Objects.equals(cetipAcrTiv, that.cetipAcrTiv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cetipMotivoCessazione, cetipSgtpoSeqPk, cetipAcrTiv);
    }

}