package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.DocumentActionConst;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.ActionDocumentService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.DocumentService;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizioneUsita.PredisposizioneInput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.PredisposizioneDto;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.TitolarioDto;
import it.almaviva.difesa.documenti.document.service.PredisposizioneService;
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

import javax.validation.Valid;

@RestController
@RequestMapping(Constant.PREDISPOSIZIONE_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class PredisposizioneController {

    private final PredisposizioneService predisposizioneService;
    private final ActionDocumentService actionDocumentService;
    private final DocumentService documentService;

    @GetMapping("/new/{idDocumento}")
    public ResponseEntity<PredisposizioneDto> edit(@PathVariable @Validated long idDocumento) {
        log.debug("create/edit predisposizione");
        TitolarioDto titolarioDto = documentService.titolarioDefault(idDocumento, Constant.TIPO_SOTTOFASCICOLO_TITOLARIO_DEFAULT);
        return ResponseEntity.ok().body(predisposizioneService.getOrCreateAndPrefill(idDocumento, titolarioDto));
    }

    @PostMapping("/salva")
    public ResponseEntity<PredisposizioneDto> salvaPredisposizioneInUscita(@Valid @RequestBody PredisposizioneInput predisposizioneInput) {
        log.debug("salvataggio nuova predisposizione");
        actionDocumentService.checkPermissionOnAction(predisposizioneInput.getIdDocumento(), DocumentActionConst.PREDISPOSIZIONE);
        return ResponseEntity.ok().body(documentService.savePredisposition(predisposizioneInput));
    }

}
