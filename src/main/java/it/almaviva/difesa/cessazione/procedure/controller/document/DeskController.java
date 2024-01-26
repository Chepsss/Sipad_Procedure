package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.service.msproc.DeskService;
import it.almaviva.difesa.documenti.document.model.dto.response.scrivania.TbAdhocScrivaniaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(Constant.DESK_INDEX_URL)
@RequiredArgsConstructor
@Slf4j
public class DeskController {

    private final DeskService deskService;

    @GetMapping("/desk")
    public ResponseEntity<List<TbAdhocScrivaniaDto>> desk() {
        log.debug("get desk data");
        return ResponseEntity.ok().body(deskService.list());
    }

    @GetMapping("/list")
    public ResponseEntity<List<TbAdhocScrivaniaDto>> desk(@RequestParam(Constant.EMPLOYEE_ID) Long employeeId) throws MalformedURLException, URISyntaxException {
        log.debug("get desk list");
        return ResponseEntity.ok().body(deskService.listByCodAppAndCatPers(employeeId));
    }

}
