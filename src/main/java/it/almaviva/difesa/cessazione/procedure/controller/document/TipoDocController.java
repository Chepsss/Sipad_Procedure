package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.ListaModelliOutput;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.DocumentService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.TipoDocService;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.ListaCategoriaOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.tipidocumenti.ListaTipiDocumentiOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.TIPO_DOC_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class TipoDocController {

    private final TipoDocService tipoDocService;
    private final DocumentService documentService;

    @GetMapping("/categorie")
    public ResponseEntity<ListaCategoriaOutput> listCategorie() {
        log.debug("Lista documenti");
        return ResponseEntity.ok().body(tipoDocService.listFilteredCategories());
    }

    @GetMapping("/tipidocumenti/{docatId}")
    public ResponseEntity<ListaTipiDocumentiOutput> listTipoDocumento(@PathVariable @Validated long docatId) {
        log.debug("Lista documenti");
        return ResponseEntity.ok().body(tipoDocService.listFilteredDocTypes(docatId));
    }

    @GetMapping("/modelli/{codice}")
    public ResponseEntity<ListaModelliOutput> listModelliDocumenti(@PathVariable @Validated String codice) {
        return ResponseEntity.ok().body(documentService.models(codice));
    }

}
