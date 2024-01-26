package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.DocumentActionConst;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ChangeDocNameRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.SignedDocumentRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.DocEsterniList;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.DocGenerationOutput;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.FileOutputResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.InputDocument;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.InputDocumentExt;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.InputDocumentFromTemplateDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.UpdateDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.ActionDocumentService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.DocumentService;
import it.almaviva.difesa.documenti.document.model.dto.request.tb_document.NotificaInput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.DettaglioDocumentoOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.DocumentList;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.DocumentOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.FileOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.SuccessOutput;
import it.almaviva.difesa.documenti.document.service.TbDocumentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(Constant.DOCUMENT_INDEX_URL)
@Slf4j
@RequiredArgsConstructor
public class DocumentController {

    private final TbDocumentoService documentoService;
    private final ActionDocumentService actionDocumentService;
    private final DocumentService documentService;

    @GetMapping("/documentDetail/{id}")
    public DettaglioDocumentoOutput getDocumentDetail(@PathVariable Long id) {
        log.debug("REST request to get document detail");
        actionDocumentService.checkPermissionOnAction(id, DocumentActionConst.DETTAGLI_DOCUMENTO);
        return documentoService.dettaglio(id);
    }

    @GetMapping("/downloadDocument/{id}")
    public FileOutput downloadFile(@PathVariable Long id) {
        log.debug("REST request to get the file");
        actionDocumentService.checkPermissionOnAction(id, DocumentActionConst.VISUALIZZA_DOCUMENTO);
        return documentoService.download(id);
    }

    @GetMapping("documentList/{procedureId}")
    public DocumentList getDocumentList(@PathVariable Long procedureId) {
        log.debug("REST request to get document list");
        return actionDocumentService.updateDocumentListWithActions(procedureId);
    }

    @GetMapping("/checkForNewDocumentAction/{procedureId}")
    public SuccessOutput checkForNewDocumentAction(@PathVariable Long procedureId) {
        log.debug("REST request to check for new document action");
        return actionDocumentService.checkForNewDocumentAction(procedureId);
    }

    @PostMapping("/insertDocument")
    public DocGenerationOutput insertDocument(@Valid @RequestBody InputDocument documentDTO) throws ServiceException {
        log.debug("REST request to insert a document");
        return documentService.insertDocument(documentDTO);
    }

    @PutMapping("/updateDocument")
    public SuccessOutput updateDocument(@RequestBody UpdateDocumentDTO updateDocumentDTO) {
        log.debug("REST request to update a document");
        actionDocumentService.checkPermissionOnAction(Long.valueOf(updateDocumentDTO.getIdDocumento()), DocumentActionConst.MODIFICA);
        return documentService.updateDocument(updateDocumentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessOutput> deleteDocument(@PathVariable Long id) {
        log.debug("Delete document: " + id);
        actionDocumentService.checkPermissionOnAction(id, DocumentActionConst.ELIMINA);
        SuccessOutput output = documentService.deleteDocument(id);
        if (Boolean.TRUE.equals(output.getSuccess())) {
            return ResponseEntity.ok(output);
        } else {
            return ResponseEntity.internalServerError().body(output);
        }
    }

    @GetMapping(value = {"/registeredList"})
    public ResponseEntity<DocEsterniList> getRegisteredList(@RequestParam String registro,
                                                            @RequestParam Long year,
                                                            @RequestParam String number) {
        log.debug("REST request to get document registered list");
        return ResponseEntity.ok().body(documentService.consultazioneDocAdhoc(registro, year, number));
    }

    @GetMapping(value = {"/inUscita/{id}"})
    public ResponseEntity<DocumentList> getDocUscita(@PathVariable Long id) {
        log.debug("REST request to get documents available to protocollo in uscita");
        return ResponseEntity.ok().body(documentService.listWithPredInUscita(id));
    }

    @PostMapping(value = {"/registeredList"})
    public ResponseEntity<DocEsterniList> getExternalDocuments(@RequestBody InputDocumentExt input) {
        log.debug("REST request to get document registered list");
        return ResponseEntity.ok().body(documentService.consultazioneDocAdhoc(input));
    }

    @PostMapping("/saveNotification")
    public DocumentOutput saveNotification(@RequestBody NotificaInput notificaInputDTO) {
        log.debug("REST request to save notification");
        actionDocumentService.checkPermissionOnAction(notificaInputDTO.getIdDocumento(), DocumentActionConst.INSERISCI_NOTIFICA);
        return documentService.saveNotification(notificaInputDTO);
    }

    @GetMapping("/sterilize/{id}")
    public SuccessOutput sterilizeDocument(@PathVariable Long id) {
        log.debug("REST request to sterilize document");
        actionDocumentService.checkPermissionOnAction(id, DocumentActionConst.STERILIZZA);
        return documentService.sterilizeDocument(id);
    }

    @GetMapping("/convertToPdf/{id}")
    public ResponseEntity<FileOutputResponseDTO> convertToPdf(@PathVariable Long id) {
        log.debug("REST request to convert to pdf a document");
        return ResponseEntity.ok(documentService.convertToPdf(id));
    }

    @PostMapping("/saveSignedDocument")
    public SuccessOutput saveSignedDocument(@Valid @RequestBody SignedDocumentRequest request) {
        log.debug("REST request to save signed document");
        actionDocumentService.checkPermissionOnAction(request.getId(), DocumentActionConst.FIRMA);
        return documentService.saveSignedDocument(request);
    }

    @PostMapping("/insertFromTemplate")
    public DocGenerationOutput insertDocumentFromTemplate(@Valid @RequestBody InputDocumentFromTemplateDTO documentDTO)
            throws ServiceException {
        log.debug("REST request to insert a document from template");
        return documentService.insertDocumentFromTemplate(documentDTO);
    }

    @PostMapping("/changeDocName")
    public SuccessOutput changeDocName(@Valid @RequestBody ChangeDocNameRequest request) {
        log.debug("REST request to change document name");
        actionDocumentService.checkPermissionOnAction(request.getId(), DocumentActionConst.CAMBIA_NOME);
        return documentService.changeDocumentName(request);
    }

}
