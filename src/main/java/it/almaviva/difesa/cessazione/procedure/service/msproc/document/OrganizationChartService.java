package it.almaviva.difesa.cessazione.procedure.service.msproc.document;

import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.OrganigrammaLevelListDTO;
import it.almaviva.difesa.documenti.document.model.dto.response.organigramma.OrganigrammaLevelList;
import it.almaviva.difesa.documenti.document.model.dto.response.organigramma.TbStoraOrganigrammaAooDto;
import it.almaviva.difesa.documenti.document.service.OrganigrammaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class OrganizationChartService {

    private final OrganigrammaService organigrammaService;

    public OrganigrammaLevelListDTO getOrganizationChart(String level0, String level1, String level2, String level3, String level4) throws MalformedURLException, URISyntaxException {
        OrganigrammaLevelListDTO out = new OrganigrammaLevelListDTO();
        OrganigrammaLevelList organigrammaLevelList = organigrammaService.level(level0, level1, level2, level3);
        out.setList(organigrammaLevelList.getList());
        out.setLivelli(organigrammaLevelList.getLivelli());
        List<TbStoraOrganigrammaAooDto> assegnatari = organigrammaService.assegnatari(level0, level1, level2, level3, level4);
        out.setAssegnatari(assegnatari);
        return out;
    }

}
