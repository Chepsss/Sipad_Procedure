package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.DocumentActionConst;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.ActionDocumentService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.DocumentService;
import it.almaviva.difesa.documenti.document.model.dto.request.segnatura.SegnaturaInput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.SegnaturaDto;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.TitolarioDto;
import it.almaviva.difesa.documenti.document.service.SegnaturaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(Constant.SEGNATURA_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class SegnaturaController {

    private final SegnaturaService segnaturaService;
    private final ActionDocumentService actionDocumentService;
    private final DocumentService documentService;

    @PostMapping("/insert")
    public ResponseEntity<SegnaturaDto> salvaSegnatura(@Valid @RequestBody SegnaturaInput input) {
        log.debug("Inserimento segnatura");
        actionDocumentService.checkPermissionOnAction(input.getIdDocumento(), DocumentActionConst.SEGNATURA);
        return ResponseEntity.ok().body(documentService.saveSignature(input));
    }

    @PostMapping("/crea/{idDocumento}")
    public ResponseEntity<SegnaturaDto> anteprimaSalvataggioSegnatura(@PathVariable @Validated Long idDocumento) {
        log.debug("Inserimento segnatura");
        actionDocumentService.checkPermissionOnAction(idDocumento, DocumentActionConst.SEGNATURA);
        TitolarioDto titolarioDto = documentService.titolarioDefault(idDocumento, Constant.TIPO_SOTTOFASCICOLO_TITOLARIO_DEFAULT);
        return ResponseEntity.ok().body(segnaturaService.preparaRegistroETitolario(idDocumento, titolarioDto));
    }

}
