package it.almaviva.difesa.cessazione.procedure.service.rest;

import it.almaviva.difesa.cessazione.procedure.model.dto.difesa_sipad_main_be_centrale_api.MonitoraggioDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.difesa_sipad_main_be_centrale_api.ProcedimentoQueue;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DifesaSipadMainBeCentraleApiClient extends BaseRestTemplateClient {

    private static final String PROCEDURES_MONITORAGGIO = "%s/procedimenti/monitoraggio";

    @Value("${centrale-api.baseurl}")
    private String baseUrl;

    @Value("${app.idApplicativoCentral:null}")
    private Long idApplicativoCentral;

    @SneakyThrows
    public Page<ProcedimentoQueue> getAllProceduresArchiveByFiscalCode(String codiceFiscale, String statoLavorazione, Long idProcedimentoEscluso) {
        String url = String.format(PROCEDURES_MONITORAGGIO, baseUrl);
        MonitoraggioDto request = new MonitoraggioDto();
        request.setCodiceFiscale(codiceFiscale);
        request.setStatoLavorazione(statoLavorazione);
        request.setIdVerticaleEscluso(idApplicativoCentral);
        request.setIdProcedimentoEscluso(idProcedimentoEscluso);
        return callPostService(url, request, null, null, new ParameterizedTypeReference<RestPageImpl<ProcedimentoQueue>>() {
        });
    }

}
