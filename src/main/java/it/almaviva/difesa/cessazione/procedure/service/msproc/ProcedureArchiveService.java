package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.difesa_sipad_main_be_centrale_api.ProcedimentoQueue;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.ProceduresArchiveDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwDo007ProcedimentiDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.ProceduresArchiveMapper;
import it.almaviva.difesa.cessazione.procedure.service.rest.DifesaSipadMainBeCentraleApiClient;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.cessazione.procedure.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service("procedureArchiveService")
@RequiredArgsConstructor
public class ProcedureArchiveService {

    private final SipadClient sipadClient;
    private final ProceduresArchiveMapper procedimentiMapper;
    private final DifesaSipadMainBeCentraleApiClient difesaSipadMainBeCentraleApiClient;
    private final StateProcedureService stateProcedureService;
    private final FaseProcedureService faseProcedureService;


    public Page<ProceduresArchiveDTO> getProceduresArchiveInProgressFromSIPAD(String codiceFiscale, Long idProcedimentoEscluso, Pageable pageable) {
        try {
            List<ProceduresArchiveDTO> procedimenti = getProcedimentiAttiviFromSipadClient(codiceFiscale, idProcedimentoEscluso);
            List<ProceduresArchiveDTO> procedimentiSipadCentrale = getProcedimentiInProgressFromSipadCentrale(codiceFiscale, idProcedimentoEscluso);
            return mergeProcedureLists(procedimenti, procedimentiSipadCentrale, pageable);
        } catch (Exception e) {
            log.error(">>>>>> ERROR in getProceduresArchiveInProgressFromSIPAD", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private Page<ProceduresArchiveDTO> mergeProcedureLists(List<ProceduresArchiveDTO> procedimenti, List<ProceduresArchiveDTO> procedimentiSipadCentrale, Pageable pageable) {
        List<ProceduresArchiveDTO> all = new ArrayList<>(procedimenti);

        if (!CollectionUtils.isEmpty(procedimentiSipadCentrale)) all.addAll(procedimentiSipadCentrale);

        String property = pageable.getSort().stream().map(Sort.Order::getProperty).findFirst().orElse("");
        Sort.Direction direction = pageable.getSort().stream().map(Sort.Order::getDirection).findFirst().orElse(null);

        List<ProceduresArchiveDTO> newListSorted = getListOrdered(all, direction, List.of(property.split("[.]")));

        newListSorted.forEach(proc -> {
            proc.setStato(stateProcedureService.getDescriptionByCodeStateCentral(proc.getStato()));
            proc.setFase(faseProcedureService.getDescriptionByCodeFaseCentral(proc.getFase()));
        });

        return Utils.getPageFromList(newListSorted, pageable);
    }

    private List<ProceduresArchiveDTO> getProcedimentiInProgressFromSipadCentrale(String codiceFiscale, Long idProcedimentoEscluso) {
        return getProcedimentiFromSipadCentrale(codiceFiscale, "L", idProcedimentoEscluso);
    }

    private List<ProceduresArchiveDTO> getProcedimentiChiusiFromSipadCentrale(String codiceFiscale, Long idProcedimentoEscluso) {
        return getProcedimentiFromSipadCentrale(codiceFiscale, "C", idProcedimentoEscluso);
    }

    private List<ProceduresArchiveDTO> getProcedimentiFromSipadCentrale(String codiceFiscale, String statoLavorazione, Long idProcedimentoEscluso) {
        Page<ProcedimentoQueue> allProceduresArchiveByFiscalCode = difesaSipadMainBeCentraleApiClient.getAllProceduresArchiveByFiscalCode(codiceFiscale, statoLavorazione, idProcedimentoEscluso);

        if (Objects.nonNull(allProceduresArchiveByFiscalCode)) {
            List<ProcedimentoQueue> procedures = allProceduresArchiveByFiscalCode.getContent();
            return procedures.stream().map(procedimentiMapper::copyProperties).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Page<ProceduresArchiveDTO> getProceduresArchiveClosedInProgressFromSIPAD(String codiceFiscale, Long idProcedimentoEscluso, Pageable pageable) {
        try {
            List<ProceduresArchiveDTO> procedimenti = getProcedimentiChiusiFromSipadClient(codiceFiscale, idProcedimentoEscluso);
            List<ProceduresArchiveDTO> procedimentiSipadCentrale = getProcedimentiChiusiFromSipadCentrale(codiceFiscale, idProcedimentoEscluso);
            return mergeProcedureLists(procedimenti, procedimentiSipadCentrale, pageable);
        } catch (Exception e) {
            log.error(">>>>>> ERROR in getProceduresArchiveClosedInProgressFromSIPAD", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private List<ProceduresArchiveDTO> getProcedimentiFromSipadClient(String codiceFiscale, boolean isClosed, Long idProcedimentoEscluso) {
        PageRequest allInAPage = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
        Page<VwDo007ProcedimentiDTO> procedimentiDTOS = sipadClient.getAllProceduresArchiveByFiscalCodeAndState(codiceFiscale, isClosed, allInAPage);
        if (procedimentiDTOS.isEmpty()) {
            return new ArrayList<>();
        }
        return procedimentiDTOS.getContent().stream()
                .filter(it -> !it.getDo007PrproId().equals(String.valueOf(idProcedimentoEscluso)))
                .map(procedimentiMapper::copyProperties).collect(Collectors.toList());
    }

    private List<ProceduresArchiveDTO> getProcedimentiAttiviFromSipadClient(String codiceFiscale, Long idProcedimentoEscluso) {
        return getProcedimentiFromSipadClient(codiceFiscale, false, idProcedimentoEscluso);
    }

    private List<ProceduresArchiveDTO> getProcedimentiChiusiFromSipadClient(String codiceFiscale, Long idProcedimentoEscluso) {
        return getProcedimentiFromSipadClient(codiceFiscale, true, idProcedimentoEscluso);
    }

    private List<ProceduresArchiveDTO> getListOrdered(List<ProceduresArchiveDTO> proceduresArchive, Sort.Direction direction, List<String> properties) {
        if (Objects.nonNull(direction) && !properties.isEmpty()) {

            Comparator<ProceduresArchiveDTO> comparator = (o1, o2) -> 0;
            if (properties.contains(Constant.PRPRO_COD_PRO.toLowerCase())) {
                comparator = Comparator.comparing(ProceduresArchiveDTO::getPrproCodPro, Comparator.nullsFirst(Comparator.naturalOrder()));
            } else if (properties.contains(Constant.DATA_AVVIO)) {
                comparator = Comparator.comparing(ProceduresArchiveDTO::getDataAvvio, Comparator.nullsFirst(Comparator.naturalOrder()));
            } else if (properties.contains(Constant.DATA_FINE)) {
                comparator = Comparator.comparing(ProceduresArchiveDTO::getDataFine, Comparator.nullsFirst(Comparator.naturalOrder()));
            } else if (properties.contains(Constant.FASE)) {
                comparator = Comparator.comparing(ProceduresArchiveDTO::getFase, Comparator.nullsFirst(Comparator.naturalOrder()));
            } else if (properties.contains(Constant.STATO)) {
                comparator = Comparator.comparing(ProceduresArchiveDTO::getStato, Comparator.nullsFirst(Comparator.naturalOrder()));
            } else if (properties.contains(Constant.TIPO_PROC)) {
                comparator = Comparator.comparing(ProceduresArchiveDTO::getTipoProc, Comparator.nullsFirst(Comparator.naturalOrder()));
            } else if (properties.contains(Constant.AUTORE)) {
                comparator = Comparator.comparing(ProceduresArchiveDTO::getAutore, Comparator.nullsFirst(Comparator.naturalOrder()));
            }
            return Utils.sortList(proceduresArchive, direction, comparator);
        }
        return proceduresArchive;
    }

}
