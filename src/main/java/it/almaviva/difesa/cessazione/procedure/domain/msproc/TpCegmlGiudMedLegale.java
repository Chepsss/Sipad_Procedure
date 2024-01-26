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
@Table(name = "TP_CEGML_GIUD_MED_LEGALE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners(PreventAnyDataManipulation.class)
public class TpCegmlGiudMedLegale implements GenericEntity, Serializable, Sortable {

    private static final long serialVersionUID = -3086255459569121178L;

    @Id
    @Column(name = "CEGML_COD_PK", nullable = false, length = 12)
    private String id;

    @Column(name = "CEGML_DESCRIZ_ABBR_GML", length = 70)
    private String cegmlDescrizAbbrGml;

    @Column(name = "CEGML_DESCRIZIONE_GML", length = 350)
    private String cegmlDescrizioneGml;

    @Column(name = "CEGML_DATA_INIZ")
    private LocalDate cegmlDataIniz;

    @Column(name = "CEGML_DATA_FINE")
    private LocalDate cegmlDataFine;

    @Column(name = "CEGML_COD_ASP", length = 3)
    private String cegmlCodAsp;

    @Column(name = "CEGML_NUM_ISTANZA", length = 2)
    private String cegmlNumIstanza;

    @Column(name = "CEGML_DATA_INS", nullable = false)
    private LocalDateTime cegmlDataIns;

    @Column(name = "CEGML_DATA_ULT_AGG", nullable = false)
    private LocalDateTime cegmlDataUltAgg;

    @Column(name = "CEGML_COD_ULT_AGG", nullable = false, length = 50)
    private String cegmlCodUltAgg;

    @Column(name = "lista_gml1", nullable = false)
    private Boolean listaGml1 = false;

    @Column(name = "lista_gml2", nullable = false)
    private Boolean listaGml2 = false;

    @OneToMany(mappedBy = "tpCegmlGiudMedLegale", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Procedure> tbCeProceduras1 = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tpCegmlGiudMedLegale2", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Procedure> tbCeProceduras2 = new LinkedHashSet<>();

    @Override
    public Sort getSort() {
        return SortConstant.SORT_BY_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TpCegmlGiudMedLegale that = (TpCegmlGiudMedLegale) o;
        return id.equals(that.id)
                && Objects.equals(cegmlDescrizAbbrGml, that.cegmlDescrizAbbrGml)
                && Objects.equals(cegmlDescrizioneGml, that.cegmlDescrizioneGml)
                && Objects.equals(cegmlDataIniz, that.cegmlDataIniz)
                && Objects.equals(cegmlDataFine, that.cegmlDataFine)
                && Objects.equals(cegmlCodAsp, that.cegmlCodAsp)
                && Objects.equals(cegmlNumIstanza, that.cegmlNumIstanza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cegmlDescrizAbbrGml, cegmlDescrizioneGml, cegmlDataIniz,
                cegmlDataFine, cegmlCodAsp, cegmlNumIstanza);
    }

}