package it.almaviva.difesa.cessazione.procedure.model.mapper.sipad;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.DocumentProtocollatoDto;
import it.almaviva.difesa.documenti.document.data.sipad.repository.TbCeDestinatariPredRepository;
import it.almaviva.difesa.documenti.document.data.sipad.specification.DestinatariPredSpecification;
import it.almaviva.difesa.documenti.document.domain.msdoc.AllegatoDocumento;
import it.almaviva.difesa.documenti.document.domain.msdoc.DestinatariPred;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbDocumento;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbPredisposizione;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.DestinatariPredOutput;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface DocumentProtocollatoMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomeFile", source = "nomeFile")
    @Mapping(target = "richiestaAdhoc", source = "idRichProtocollo")
    @Mapping(target = "protocollo", source = "numProtocollo")
    DocumentProtocollatoDto toDto(TbDocumento source, @Context TbCeDestinatariPredRepository destinatariPredRepository, @Context Long idAllegato);

    @BeforeMapping
    default void beforeMapping(@MappingTarget DocumentProtocollatoDto target, TbDocumento source, @Context TbCeDestinatariPredRepository destinatariPredRepository, @Context Long idAllegato) {
        TbPredisposizione predisposizione = source.getPredisposizione();
        if (Objects.nonNull(predisposizione)) { //  recupero destinatari Interni ed Esterni
            List<DestinatariPred> destinatari = destinatariPredRepository.findAll(DestinatariPredSpecification.listByPredisposizione(predisposizione));

            List<DestinatariPredOutput> destExt = destinatari.stream()
                    .filter(d -> Objects.nonNull(d.getFlagExt()) && Boolean.TRUE.equals(d.getFlagExt()))
                    .map(d -> new DestinatariPredOutput(d.getNome()))
                    .collect(Collectors.toList());
            target.setDestinatari(destExt);

            List<DestinatariPredOutput> destInt = destinatari.stream()
                    .filter(d -> Objects.nonNull(d.getFlagExt()) && Boolean.FALSE.equals(d.getFlagExt()))
                    .map(d -> new DestinatariPredOutput(d.getIntUnitaOrg()))
                    .collect(Collectors.toList());
            target.setDestinatariInterni(destInt);

        }
        Set<AllegatoDocumento> allegatoDocumentos = source.getAllegatoDocumentos();
        target.setCollegato(false);
        if (Objects.nonNull(source.getAllegatoDocumentos())) {
            for (AllegatoDocumento ad : allegatoDocumentos) {
                if (ad.getTipoCollegamento().equals(Constant.COD_TIPO_ALLEGATO_RISPOSTA)
                        && idAllegato.equals(ad.getAllegatoDocumentoId().getAllegato().getId())) {
                    target.setCollegato(true);
                }
            }
        }
    }

}
