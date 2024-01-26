package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.PrecompilazioneTitolarioDto;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.DocumentService;
import it.almaviva.difesa.documenti.document.model.dto.request.titolario.InputTitolarioDTO;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.ListaClassi;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.ListaFascicoli;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.ListaSottoclassi;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.ListaSottofascicoli;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.ListaTitoli;
import it.almaviva.difesa.documenti.document.service.TitolarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping(Constant.TITOLARIO_INDEX_URL)
@Slf4j
@Validated
@RequiredArgsConstructor
public class TitolarioController {

    private final TitolarioService titolarioService;
    private final DocumentService documentService;

    @GetMapping("/")
    public ResponseEntity<ListaTitoli> titolario() {
        log.debug("TitolarioCompleto");
        return ResponseEntity.ok().body(titolarioService.listaTitoli());
    }

    @GetMapping("/{codiceTitolo}")
    public ResponseEntity<ListaClassi> titolario(@PathVariable @Validated String codiceTitolo) {
        log.debug("Titolario per Titolo");
        return ResponseEntity.ok().body(titolarioService.listaClassi(codiceTitolo));
    }

    @GetMapping("/{codiceTitolo}/{codiceClasse}")
    public ResponseEntity<ListaSottoclassi> titolario(@PathVariable @Validated String codiceTitolo,
                                                      @PathVariable @Validated String codiceClasse) {
        log.debug("Titolario per classe");
        return ResponseEntity.ok().body(titolarioService.listaSottoclassi(codiceTitolo, codiceClasse));
    }

    @GetMapping("/{codiceTitolo}/{codiceClasse}/{codiceSottoclasse}")
    public ResponseEntity<ListaFascicoli> titolario(@PathVariable @Validated String codiceTitolo,
                                                    @PathVariable @Validated String codiceClasse,
                                                    @PathVariable @Validated String codiceSottoclasse,
                                                    @RequestParam(name = "filtro", required = false) String filtro,
                                                    @RequestParam(name = "max", required = false, defaultValue = "10") Integer max) {
        log.debug("Titolario per sottoclasse");
        return ResponseEntity.ok().body(titolarioService.listaFascicoli(codiceTitolo, codiceClasse, codiceSottoclasse, filtro, max));
    }

    @PostMapping("/fascicoli")
    public ResponseEntity<ListaFascicoli> fascicoli(@Valid @RequestBody InputTitolarioDTO input) {
        log.debug("Titolario per fascicolo");
        return ResponseEntity.ok().body(titolarioService.listaFascicoli(input.getCodiceTitolo(), input.getCodiceClasse(),
                input.getCodiceSottoclasse(), input.getFiltro(), input.getMax()));
    }

    @PostMapping("/sottofascicoli")
    public ResponseEntity<ListaSottofascicoli> sottofascicoli(@Valid @RequestBody InputTitolarioDTO input) {
        log.debug("Titolario per sottofascicolo");
        return ResponseEntity.ok().body(titolarioService.listaSottofascicoli(input));
    }

    @GetMapping("precompila/{documentId}")
    public ResponseEntity<PrecompilazioneTitolarioDto> titolarioPrecompilato(@PathVariable Long documentId) {
        PrecompilazioneTitolarioDto dto = new PrecompilazioneTitolarioDto();
        dto.setTitolario(documentService.titolarioDefault(documentId, Constant.TIPO_SOTTOFASCICOLO_TITOLARIO_DEFAULT));
        dto.setDataCompilazione(LocalDate.now());
        return ResponseEntity.ok().body(dto);
    }

}
