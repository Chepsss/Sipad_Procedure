package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.DocumentActionConst;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.DocumentListDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.ProtocollazioneMessage;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.StatoPredisposizioneMessage;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.ActionDocumentService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.ProtocollazioneService;
import it.almaviva.difesa.documenti.document.model.dto.request.protocollazioneIngresso.ProtocolloIngressoDto;
import it.almaviva.difesa.documenti.document.model.dto.request.protocollazioneUscita.ProtocolloUscitaDto;
import it.almaviva.difesa.documenti.document.model.dto.response.DocumentRegistrationResponse;
import it.almaviva.difesa.documenti.document.model.dto.response.predisposizione.PredisposizioneResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.PROTOCOLLAZIONE_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class ProtocolloController {

    private final ProtocollazioneService protocollazioneService;
    private final ActionDocumentService actionDocumentService;

    @PostMapping("/ingresso")
    public ResponseEntity<DocumentRegistrationResponse> insertDocument(@RequestBody ProtocolloIngressoDto documentDTO) {
        log.debug("REST request to 'protocollazione in ingresso'");
        Long id = documentDTO.getDocumentoId();
        actionDocumentService.checkPermissionOnAction(id, DocumentActionConst.PROTOCOLLA);
        return ResponseEntity.ok(protocollazioneService.ingresso(documentDTO));
    }

    @PostMapping("/uscita")
    public ResponseEntity<PredisposizioneResponse> insertDocumentOut(@RequestBody ProtocolloUscitaDto documentDTO) {
        log.debug("REST request to 'protocollazione in uscita'");
        Long id = documentDTO.getDocumentoId();
        actionDocumentService.checkPermissionOnAction(id, DocumentActionConst.PREDISPONI_ADHOC);
        return ResponseEntity.ok(protocollazioneService.uscita(documentDTO));
    }

    @PostMapping("/uscita/check")
    public ResponseEntity<DocumentListDto> previewInsertDocumentOut(@RequestBody ProtocolloUscitaDto documentDTO) {
        log.debug("REST request to previewInsertDocumentOut");
        Long id = documentDTO.getDocumentoId();
        actionDocumentService.checkPermissionOnAction(id, DocumentActionConst.PREDISPONI_ADHOC);
        return ResponseEntity.ok(protocollazioneService.checkUscita(documentDTO));
    }

    @GetMapping(value = {"/stato/{id}"})
    public ResponseEntity<StatoPredisposizioneMessage> stato(@PathVariable Long id) {
        log.debug("REST request to 'stato protocollo'");
        actionDocumentService.checkPermissionOnAction(id, DocumentActionConst.ACQUISISCI_PROTOCOLLO);
        StatoPredisposizioneMessage stato = protocollazioneService.stato(id);
        return ResponseEntity.status(stato.getEsito()).body(stato);
    }

    @PostMapping("/protocolla")
    public ResponseEntity<PredisposizioneResponse> protocolla(@RequestBody ProtocolloUscitaDto documentDTO) {
        log.debug("REST request to 'protocollazione in uscita quarta via'");
        Long id = documentDTO.getDocumentoId();
        actionDocumentService.checkPermissionOnAction(id, DocumentActionConst.APPROVAZIONE_ADHOC);
        return ResponseEntity.ok(protocollazioneService.protocolla(documentDTO));
    }

    @PostMapping("/preview")
    public ResponseEntity<ProtocollazioneMessage> previewProtocollo(@RequestBody ProtocolloUscitaDto documentDTO) {
        log.debug("REST request to 'preview Protocollo in uscita'");
        Long id = documentDTO.getDocumentoId();
        actionDocumentService.checkPermissionOnAction(id, DocumentActionConst.APPROVAZIONE_ADHOC);
        return ResponseEntity.ok(protocollazioneService.previewProtocollo(documentDTO));
    }

}
