package it.almaviva.difesa.cessazione.procedure.domain.msproc;

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
@Table(name = "tb_ce_proc_transito")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TbCeProcTransito extends AbstractAuditingEntity implements GenericEntity, Serializable, Sortable {

    private static final long serialVersionUID = -4653125189154119566L;

    @Id
    @Column(name = "id_proc", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proc", nullable = false)
    private Procedure tbCeProcedura;

    @Column(name = "flag_idoneo_transito")
    private Boolean flagIdoneoTransito;

    @Column(name = "flag_presentata_istanza")
    private Boolean flagPresentataIstanza;

    @Column(name = "data_istanza")
    private LocalDate dataIstanza;

    @Column(name = "prot_istanza", length = 50)
    private String protIstanza;

    @Column(name = "data_prot_istanza")
    private LocalDate dataProtIstanza;

    @Column(name = "prot_domanda_persomil", length = 50)
    private String protDomandaPersomil;

    @Column(name = "data_pr_dom_persomil")
    private LocalDate dataPrDomPersomil;

    @Column(name = "flag_pres_rinuncia")
    private Boolean flagPresRinuncia;

    @Column(name = "data_pres_rinuncia")
    private LocalDate dataPresRinuncia;

    @Column(name = "prot_rinuncia_cmd", length = 50)
    private String protRinunciaCmd;

    @Column(name = "data_rinuncia_cmd")
    private LocalDate dataRinunciaCmd;

    @Column(name = "prot_rinuncia_persomil", length = 50)
    private String protRinunciaPersomil;

    @Column(name = "data_rinuncia_persomil")
    private LocalDate dataRinunciaPersomil;

    @Column(name = "flag_noautoriz")
    private Boolean flagNoautoriz;

    @Column(name = "data_noaut_persociv")
    private LocalDate dataNoautPersociv;

    @Column(name = "prot_noaut_persociv", length = 50)
    private String protNoautPersociv;

    @Column(name = "data_pr_noaut_persociv")
    private LocalDate dataPrNoautPersociv;

    @Column(name = "prot_noaut_persomil", length = 50)
    private String protNoautPersomil;

    @Column(name = "data_pr_noaut_persomil")
    private LocalDate dataPrNoautPersomil;

    @Column(name = "data_notifica")
    private LocalDate dataNotifica;

    @Column(name = "flag_firma_contratto")
    private Boolean flagFirmaContratto;

    @Column(name = "data_firma_contr")
    private LocalDate dataFirmaContr;

    @Column(name = "prot_firma_persociv", length = 50)
    private String protFirmaPersociv;

    @Column(name = "data_pr_firma_persociv")
    private LocalDate dataPrFirmaPersociv;

    @Column(name = "prot_firma_persomil", length = 50)
    private String protFirmaPersomil;

    @Column(name = "data_pr_firma_persomil")
    private LocalDate dataPrFirmaPersomil;

    @Column(name = "flag_nonfirma_salute")
    private Boolean flagNonfirmaSalute;

    @Column(name = "data_giorno_151")
    private LocalDate dataGiorno151;

    @Column(name = "contr_data_max_266")
    private String contrDataMax266;

    @Column(name = "num_giorni_erogati_266")
    private Long numGiorniErogati266;

    @Override
    public Sort getSort() {
        return SortConstant.SORT_BY_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbCeProcTransito that = (TbCeProcTransito) o;
        return id.equals(that.id)
                && Objects.equals(flagIdoneoTransito, that.flagIdoneoTransito)
                && Objects.equals(flagPresentataIstanza, that.flagPresentataIstanza)
                && Objects.equals(dataIstanza, that.dataIstanza)
                && Objects.equals(protIstanza, that.protIstanza)
                && Objects.equals(dataProtIstanza, that.dataProtIstanza)
                && Objects.equals(protDomandaPersomil, that.protDomandaPersomil)
                && Objects.equals(dataPrDomPersomil, that.dataPrDomPersomil)
                && Objects.equals(flagPresRinuncia, that.flagPresRinuncia)
                && Objects.equals(dataPresRinuncia, that.dataPresRinuncia)
                && Objects.equals(protRinunciaCmd, that.protRinunciaCmd)
                && Objects.equals(dataRinunciaCmd, that.dataRinunciaCmd)
                && Objects.equals(protRinunciaPersomil, that.protRinunciaPersomil)
                && Objects.equals(dataRinunciaPersomil, that.dataRinunciaPersomil)
                && Objects.equals(flagNoautoriz, that.flagNoautoriz)
                && Objects.equals(dataNoautPersociv, that.dataNoautPersociv)
                && Objects.equals(protNoautPersociv, that.protNoautPersociv)
                && Objects.equals(dataPrNoautPersociv, that.dataPrNoautPersociv)
                && Objects.equals(protNoautPersomil, that.protNoautPersomil)
                && Objects.equals(dataPrNoautPersomil, that.dataPrNoautPersomil)
                && Objects.equals(dataNotifica, that.dataNotifica)
                && Objects.equals(flagFirmaContratto, that.flagFirmaContratto)
                && Objects.equals(dataFirmaContr, that.dataFirmaContr)
                && Objects.equals(protFirmaPersociv, that.protFirmaPersociv)
                && Objects.equals(dataPrFirmaPersociv, that.dataPrFirmaPersociv)
                && Objects.equals(protFirmaPersomil, that.protFirmaPersomil)
                && Objects.equals(dataPrFirmaPersomil, that.dataPrFirmaPersomil)
                && Objects.equals(flagNonfirmaSalute, that.flagNonfirmaSalute)
                && Objects.equals(dataGiorno151, that.dataGiorno151)
                && Objects.equals(contrDataMax266, that.contrDataMax266)
                && Objects.equals(numGiorniErogati266, that.numGiorniErogati266);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flagIdoneoTransito, flagPresentataIstanza, dataIstanza,
                protIstanza, dataProtIstanza, protDomandaPersomil, dataPrDomPersomil,
                flagPresRinuncia, dataPresRinuncia, protRinunciaCmd, dataRinunciaCmd,
                protRinunciaPersomil, dataRinunciaPersomil, flagNoautoriz, dataNoautPersociv,
                protNoautPersociv, dataPrNoautPersociv, protNoautPersomil, dataPrNoautPersomil,
                dataNotifica, flagFirmaContratto, dataFirmaContr, protFirmaPersociv, dataPrFirmaPersociv,
                protFirmaPersomil, dataPrFirmaPersomil, flagNonfirmaSalute, dataGiorno151, contrDataMax266, numGiorniErogati266);
    }

}