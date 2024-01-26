package it.almaviva.difesa.cessazione.procedure.controller.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.OrganigrammaLevelListDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.OrganizationChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

@RestController
@RequestMapping(Constant.ORGANIZATION_CHART_INDEX_URL)
@Slf4j
@RequiredArgsConstructor
public class OrganizationChartController {

    private final OrganizationChartService organizationChartService;

    @GetMapping
    public ResponseEntity<OrganigrammaLevelListDTO> organization(@RequestParam(required = false) String level0,
                                                                 @RequestParam(required = false) String level1,
                                                                 @RequestParam(required = false) String level2,
                                                                 @RequestParam(required = false) String level3,
                                                                 @RequestParam(required = false) String level4)
            throws MalformedURLException, URISyntaxException {
        return ResponseEntity.ok(organizationChartService.getOrganizationChart(level0, level1, level2, level3, level4));
    }

}
