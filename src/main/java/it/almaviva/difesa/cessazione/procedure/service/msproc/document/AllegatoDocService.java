package it.almaviva.difesa.cessazione.procedure.service.msproc.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.DocumentStateConst;
import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.DocProtocollatiInputDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.DocumentPredisposizioneDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.DocumentProtocollatoDto;
import it.almaviva.difesa.cessazione.procedure.model.mapper.sipad.DocumentPredisposizioneMapper;
import it.almaviva.difesa.cessazione.procedure.model.mapper.sipad.DocumentProtocollatoMapper;
import it.almaviva.difesa.cessazione.procedure.service.msproc.FaseProcedureService;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.documenti.document.data.sipad.repository.AllegatoDocumentoRepository;
import it.almaviva.difesa.documenti.document.data.sipad.repository.TbAllegatiRepository;
import it.almaviva.difesa.documenti.document.data.sipad.repository.TbCeDestinatariPredRepository;
import it.almaviva.difesa.documenti.document.data.sipad.repository.TbDocumentoRepository;
import it.almaviva.difesa.documenti.document.data.sipad.specification.DocumentoSpecifications;
import it.almaviva.difesa.documenti.document.domain.msdoc.AllegatoDocumento;
import it.almaviva.difesa.documenti.document.domain.msdoc.AllegatoDocumentoId;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbAllegato;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbDocumento;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.CategoriaDto;
import it.almaviva.difesa.documenti.document.model.dto.response.tipidocumenti.ListaTipiDocumentiOutput;
import it.almaviva.difesa.documenti.document.service.AllegatoDocumentoService;
import it.almaviva.difesa.documenti.document.service.TbDocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AllegatoDocService {

    private final TbAllegatiRepository allegatiRepository;
    private final AllegatoDocumentoRepository allegatoDocumentoRepository;
    private final AllegatoDocumentoService allegatoDocumentoService;
    private final TbDocumentoRepository documentoRepository;
    private final DocumentProtocollatoMapper documentProtocollatoMapper;
    private final TbCeDestinatariPredRepository destinatariPredRepository;
    private final SipadClient sipadClient;
    private final DocumentPredisposizioneMapper documentPredisposizioneMapper;
    private final TipoDocService tipoDocService;
    private final TbDocumentoService documentoService;
    private final FaseProcedureService faseProcedureService;

    @Transactional(readOnly = true)
    public Page<DocumentProtocollatoDto> listWithPredInUscita(Long idProcedura, Long idAllegato, Pageable pageable) {
        Page<TbDocumento> all = documentoRepository.findAll(DocumentoSpecifications.trovaProtocollati(idProcedura), pageable);
        return all.map(it -> documentProtocollatoMapper.toDto(it, destinatariPredRepository, idAllegato));
    }

    public void associaDissociaProtocolloUscita(DocProtocollatiInputDto input) {
        checkOperazioneConsentita(input.getIdProcedura());
        eliminaAllegatiDocumentiNonSelezionati(input.getIdDocumenti(), input.getIdAllegato());
        if (!input.getIdDocumentiSelezionati().isEmpty()) {
            Optional<TbAllegato> allegatoOptional = allegatiRepository.findById(input.getIdAllegato());
            if (allegatoOptional.isPresent()) {
                for (Long idDocumento : input.getIdDocumentiSelezionati()) {
                    associaAllegato(idDocumento, allegatoOptional.get(), Constant.COD_TIPO_ALLEGATO_RISPOSTA);
                }
            }
        }
    }

    private void associaAllegato(Long idDocumento, TbAllegato allegato, String tipoCollegamento) {
        TbDocumento documento = documentoService.findById(idDocumento);
        if (Objects.nonNull(documento)) {
            AllegatoDocumento ad = new AllegatoDocumento();
            ad.setTipoCollegamento(tipoCollegamento);
            if (Constant.COD_TIPO_ALLEGATO_RISPOSTA.equals(tipoCollegamento)) {
                ad.setProtocolloRisp(documento.getNumProtocollo());
            }
            AllegatoDocumentoId allegatoId = new AllegatoDocumentoId();
            allegatoId.setAllegato(allegato);
            allegatoId.setDocumento(documento);
            ad.setAllegatoDocumentoId(allegatoId);
            allegatoDocumentoRepository.save(ad);
        }
    }

    public void associaDissociaDocPredisposizione(DocProtocollatiInputDto input) {
        checkOperazioneConsentita(input.getIdProcedura());
        eliminaAllegatiDocumentiNonSelezionatiDocPredisposizione(input.getIdDocumenti(), input.getIdAllegato());
        if (!input.getIdDocumentiSelezionati().isEmpty()) {
            Optional<TbAllegato> allegatoOptional = allegatiRepository.findById(input.getIdAllegato());
            if (allegatoOptional.isPresent()) {
                for (Long idDocumento : input.getIdDocumentiSelezionati()) {
                    associaAllegato(idDocumento, allegatoOptional.get(), Constant.TIPO_COLLEGAMENTO_ALLEGATO_USCITA);
                }
            }
        }
    }

    private void eliminaAllegatiDocumentiNonSelezionatiDocPredisposizione(List<Long> idDocumenti, Long idAllegato) {
        if (Objects.nonNull(idDocumenti) && !idDocumenti.isEmpty()) {
            allegatoDocumentoService.eliminaPerListaIdDocumentiEAllegatoId(idDocumenti, idAllegato, Constant.TIPO_COLLEGAMENTO_ALLEGATO_USCITA);
        }
    }

    private void eliminaAllegatiDocumentiNonSelezionati(List<Long> idDocumenti, Long idAllegato) {
        if (Objects.nonNull(idDocumenti) && !idDocumenti.isEmpty()) {
            allegatoDocumentoService.eliminaPerListaIdDocumentiEAllegatoId(idDocumenti, idAllegato, Constant.COD_TIPO_ALLEGATO_RISPOSTA);
        }
    }

    @Transactional(readOnly = true)
    public Page<DocumentPredisposizioneDto> listProtocollazione(Long idProcedura, Long idAllegato, Pageable pageable) {
        List<CategoriaDto> tipiDocumenti = tipiDocumentiLettera();
        List<String> tipi = codicitipoLettera(tipiDocumenti);
        HashMap<String, String> dizionarioTipoDoc = dizionarioTipoDoc(tipiDocumenti);
        List<Long> stati = statiDocListProtocollazione();
        Page<TbDocumento> all = documentoRepository.findAll(DocumentoSpecifications.findByTipiAndStati(idProcedura, tipi, stati), pageable);
        return all.map(it -> documentPredisposizioneMapper.toDto(it, idAllegato, dizionarioTipoDoc));
    }

    private HashMap<String, String> dizionarioTipoDoc(List<CategoriaDto> tipiDocumenti) {
        HashMap<String, String> dizionario = new HashMap<>();
        for (CategoriaDto each : tipiDocumenti) {
            dizionario.put(each.getCodice(), each.getDescrizione());
        }
        return dizionario;
    }

    private List<CategoriaDto> tipiDocumentiLettera() {
        ListaTipiDocumentiOutput listaTipiDocumentiOutput = tipoDocService.listDocTypes(3);
        return listaTipiDocumentiOutput.getTipiDocumenti();
    }

    private List<String> codicitipoLettera(List<CategoriaDto> tipiDocumenti) {
        List<String> tipi = new ArrayList<>();
        for (CategoriaDto dto : tipiDocumenti) {
            tipi.add(dto.getCodice());
        }
        return tipi;
    }

    private List<Long> statiDocListProtocollazione() {
        List<Long> stati = new ArrayList<>();
        Long statoProtocollatoId = sipadClient.dostaStatoByAcr(DocumentStateConst.IN_LAVORAZIONE).getId();
        Long statoInApprovazioneId = sipadClient.dostaStatoByAcr(DocumentStateConst.IN_APPROVAZIONE).getId();
        stati.add(statoInApprovazioneId);
        stati.add(statoProtocollatoId);
        return stati;
    }

    private void checkOperazioneConsentita(Long idProcedura) {
        String currentProcedureFase = faseProcedureService.getCurrentFase(idProcedura);
        if (currentProcedureFase.equalsIgnoreCase(Constant.FASE_CHIUSURA)) {
            throw new ServiceException(ErrorsConst.UNAUTHORIZED_ACTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
