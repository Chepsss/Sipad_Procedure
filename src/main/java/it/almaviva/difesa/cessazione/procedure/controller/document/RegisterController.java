package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.documenti.document.model.dto.response.registrosegnatura.TpRegistroSegnaturaDTO;
import it.almaviva.difesa.documenti.document.service.RegistriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(Constant.REGISTER_INDEX_URL)
@RequiredArgsConstructor
public class RegisterController {

    private final RegistriService registriService;

    @GetMapping("/registers")
    public ResponseEntity<List<TpRegistroSegnaturaDTO>> registers() {
        log.debug("get the list of protocol registers");
        TpRegistroSegnaturaDTO registroSegnatura = registriService.list().stream()
                .filter(reg -> reg.getTipoRegistro().equals(Constant.REGISTRO_DECRETI))
                .findFirst()
                .orElse(null);
        return ResponseEntity.ok().body(Collections.singletonList(registroSegnatura));
    }

}
