package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.documenti.document.model.dto.response.destinatari.DettaglioDestinatarioOut;
import it.almaviva.difesa.documenti.document.model.dto.response.rubrica.RubricaListReturnValue;
import it.almaviva.difesa.documenti.document.model.dto.response.rubrica.RubricaOut;
import it.almaviva.difesa.documenti.document.service.RubricaAooService;
import it.almaviva.difesa.documenti.document.service.RubricaCompletaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

@RestController
@RequestMapping(Constant.DESTINATARI_EXT_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class DestinatariController {

    private final RubricaAooService rubricaAooService;
    private final RubricaCompletaService rubricaCompletaService;

    @GetMapping("/elenco")
    public ResponseEntity<RubricaListReturnValue> elenco(@RequestParam(name = "filtro", required = false) String filtro,
                                                         @RequestParam(name = "max", required = false) Integer max)
            throws URISyntaxException, MalformedURLException {
        log.debug("chiamato destinatariext/elenco");
        return ResponseEntity.ok().body(rubricaCompletaService.elenco(filtro, max));
    }

    @GetMapping("/elencopaginate")
    public ResponseEntity<Page<RubricaOut>> elencoPaginate(@RequestParam(name = "filtro", required = false) String filtro,
                                                           Pageable pageable) {
        log.debug("chiamato destinatariext/elencopaginate con filtro: {}", filtro);
        return ResponseEntity.ok().body(rubricaCompletaService.elencoPaginate(filtro, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DettaglioDestinatarioOut> dettaglio(@PathVariable @Validated Long id) {
        log.debug("chiamato destinatariext dettaglio");
        return ResponseEntity.ok().body(rubricaAooService.getById(id));
    }

}
