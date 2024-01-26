package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwSg001StgiuridicoMilDTO;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.documenti.document.model.dto.response.scrivania.TbAdhocScrivaniaDto;
import it.almaviva.difesa.documenti.document.service.TbAdhocScrivaniaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeskService {

    private final TbAdhocScrivaniaService scrivaniaService;
    private final SipadClient sipadClient;

    public List<TbAdhocScrivaniaDto> list() {
        return scrivaniaService.list(String.valueOf(Constant.ID_APPLICATIVO));
    }

    public List<TbAdhocScrivaniaDto> listByCodAppAndCatPers(Long employeeId) throws MalformedURLException, URISyntaxException {
        VwSg001StgiuridicoMilDTO stgiuridicoMilDTO = sipadClient.sg001StgiuridicoMilDTO(employeeId);
        Long ctpCatPersonale = stgiuridicoMilDTO.getIdCtpCatPersonale();
        return scrivaniaService.listByCodAppAndCatPers(String.valueOf(Constant.ID_APPLICATIVO), ctpCatPersonale);
    }

}
