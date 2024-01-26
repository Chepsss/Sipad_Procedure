package it.almaviva.difesa.cessazione.procedure.model.mapper.sipad;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.DocumentPredisposizioneDto;
import it.almaviva.difesa.documenti.document.domain.msdoc.AllegatoDocumento;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbDocumento;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface DocumentPredisposizioneMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomeFile", source = "nomeFile")
    @Mapping(target = "stato", source = "statoRich")
    DocumentPredisposizioneDto toDto(TbDocumento source, @Context Long idAllegato, @Context HashMap<String, String> dizionarioTipoDoc);

    @BeforeMapping
    default void beforeMapping(@MappingTarget DocumentPredisposizioneDto target, TbDocumento source, @Context Long idAllegato, @Context HashMap<String, String> dizionarioTipoDoc) {
        Set<AllegatoDocumento> allegatoDocumentos = source.getAllegatoDocumentos();
        target.setCollegato(false);
        if (Objects.nonNull(source.getAllegatoDocumentos())) {
            for (AllegatoDocumento ad : allegatoDocumentos) {
                if (ad.getTipoCollegamento().equals(Constant.TIPO_COLLEGAMENTO_ALLEGATO_USCITA)
                        && idAllegato.equals(ad.getAllegatoDocumentoId().getAllegato().getId())) {
                    target.setCollegato(true);
                }
            }
        }
        target.setTipoAllegato(dizionarioTipoDoc.get(source.getIdTipo()));
    }

}
