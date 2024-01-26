package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.ListaClassi;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.ListaFascicoli;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.ListaSottoclassi;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.ListaTitoli;
import it.almaviva.difesa.documenti.document.service.TitolarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.FILING_PLAN_INDEX_URL)
@Slf4j
@RequiredArgsConstructor
public class FilingPlanController {

    private final TitolarioService titolarioService;

    @GetMapping("/")
    public ResponseEntity<ListaTitoli> filingPlan() {
        log.debug("get the complete filing plan");
        return ResponseEntity.ok().body(titolarioService.listaTitoli());
    }

    @GetMapping("/{titleCode}")
    public ResponseEntity<ListaClassi> filingPlan(@PathVariable @Validated String titleCode) {
        log.debug("get the filing plan by title");
        return ResponseEntity.ok().body(titolarioService.listaClassi(titleCode));
    }

    @GetMapping("/{titleCode}/{classCode}")
    public ResponseEntity<ListaSottoclassi> filingPlan(@PathVariable @Validated String titleCode,
                                                       @PathVariable @Validated String classCode) {
        log.debug("get the filing plan by class");
        return ResponseEntity.ok().body(titolarioService.listaSottoclassi(titleCode, classCode));
    }

    @GetMapping("/{titleCode}/{classCode}/{subclassCode}")
    public ResponseEntity<ListaFascicoli> filingPlan(@PathVariable @Validated String titleCode,
                                                     @PathVariable @Validated String classCode,
                                                     @PathVariable @Validated String subclassCode,
                                                     @RequestParam(name = "filter", required = false) String filter,
                                                     @RequestParam(name = "max", required = false, defaultValue = "10") Integer max) {
        log.debug("get the filing plan by subclass");
        return ResponseEntity.ok().body(titolarioService.listaFascicoli(titleCode, classCode, subclassCode, filter, max));
    }

}
