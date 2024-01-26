package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.documenti_editor.InputPreviewDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.documenti_editor.OutputPreviewDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.TemplateResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.InputDocument;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.DocumentService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.documenti_editor.DocumentEditorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(Constant.PREVIEW_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class PreviewDocumentController {

    private final DocumentEditorService documentEditorService;
    private final DocumentService documentService;

    @PostMapping("/preview")
    public ResponseEntity<OutputPreviewDocumentDTO> generaPreview(@RequestBody InputPreviewDocumentDTO previewDocumentDTO) {
        log.debug("REST request to preview document ");
        return ResponseEntity.ok(documentEditorService.preview(previewDocumentDTO));
    }

    @GetMapping("/previewDocumento")
    public ResponseEntity<OutputPreviewDocumentDTO> generaPreviewDocumento(@RequestParam("idDocumento") Long idDocumento) {
        log.debug("REST request to preview document ");
        return ResponseEntity.ok(documentService.getDocumentoPdf(idDocumento));
    }

    @PostMapping("/template")
    public ResponseEntity<TemplateResponseDTO> getTemplate(@Valid @RequestBody InputDocument document) {
        documentService.checkUnivocitaDecreto(document.getIdProcedura(), document.getIdTipo());
        return ResponseEntity.ok(documentEditorService.getTemplate(document));
    }

}
