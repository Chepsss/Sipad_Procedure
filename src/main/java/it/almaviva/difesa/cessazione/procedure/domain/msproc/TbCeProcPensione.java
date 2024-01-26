package it.almaviva.difesa.cessazione.procedure.domain.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.common.GenericEntity;
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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
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
@Table(name = "tb_ce_proc_pensione")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TbCeProcPensione extends AbstractAuditingEntity implements GenericEntity, Serializable, Sortable {

    private static final long serialVersionUID = 935486155822943632L;

    @Id
    @Column(name = "id_proc", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proc", nullable = false)
    private Procedure tbCeProcedura;

    @Column(name = "mod_compilazione", length = 1)
    private String modCompilazione = Constant.PENSION_MOD_COMPILAZIONE_MANUALE;

    @Column(name = "prot_atto_persomil", length = 20)
    private String protAttoPersomil;

    @Column(name = "anni_servizio_eff")
    private Integer anniServizioEff;

    @Column(name = "mesi_servizio_eff")
    private Integer mesiServizioEff;

    @Column(name = "giorni_servizio_eff")
    private Integer giorniServizioEff;

    @Column(name = "eta_dipendente")
    private Integer etaDipendente;

    @Column(name = "anni_anz_contr")
    private Integer anniAnzContr;

    @Column(name = "mesi_anz_contr")
    private Integer mesiAnzContr;

    @Column(name = "giorni_anz_contr")
    private Integer giorniAnzContr;

    @Column(name = "data_agg_anz_contr")
    private LocalDate dataAggAnzContr;

    @Column(name = "data_mat_req_minimo")
    private LocalDate dataMatReqMinimo;

    @Column(name = "mesi_incr_speranza_v")
    private Integer mesiIncrSperanzaV;

    @Column(name = "mesi_fin_mobile")
    private Integer mesiFinMobile;

    @Column(name = "data_diritto_tratt")
    private LocalDate dataDirittoTratt;

    @Override
    public Sort getSort() {
        return SortConstant.SORT_BY_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbCeProcPensione that = (TbCeProcPensione) o;
        return id.equals(that.id)
                && Objects.equals(tbCeProcedura, that.tbCeProcedura)
                && Objects.equals(modCompilazione, that.modCompilazione)
                && Objects.equals(protAttoPersomil, that.protAttoPersomil)
                && Objects.equals(anniServizioEff, that.anniServizioEff)
                && Objects.equals(mesiServizioEff, that.mesiServizioEff)
                && Objects.equals(giorniServizioEff, that.giorniServizioEff)
                && Objects.equals(etaDipendente, that.etaDipendente)
                && Objects.equals(anniAnzContr, that.anniAnzContr)
                && Objects.equals(mesiAnzContr, that.mesiAnzContr)
                && Objects.equals(giorniAnzContr, that.giorniAnzContr)
                && Objects.equals(dataAggAnzContr, that.dataAggAnzContr)
                && Objects.equals(dataMatReqMinimo, that.dataMatReqMinimo)
                && Objects.equals(mesiIncrSperanzaV, that.mesiIncrSperanzaV)
                && Objects.equals(mesiFinMobile, that.mesiFinMobile)
                && Objects.equals(dataDirittoTratt, that.dataDirittoTratt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tbCeProcedura, modCompilazione, protAttoPersomil,
                anniServizioEff, mesiServizioEff, giorniServizioEff,
                etaDipendente, anniAnzContr, mesiAnzContr,
                giorniAnzContr, dataAggAnzContr, dataMatReqMinimo,
                mesiIncrSperanzaV, mesiFinMobile, dataDirittoTratt);
    }

}