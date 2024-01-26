package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpDocatCdocumentoDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.common.CommonService;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TpDocatCdocumentoService implements CommonService {

    private final SipadClient sipadClient;

    public List<TpDocatCdocumentoDTO> listCommunicationTypes(String prtpoAcrProc, String prattAcrAtt) {
        List<TpDocatCdocumentoDTO> dtos = sipadClient.docatCDocumentoList();
        ArrayList<String> accepted = new ArrayList<>(Arrays.asList(Constant.DECRETO_PRESIDENZIALE, Constant.SENTENZA, Constant.ORDINANZA));
        if (Objects.nonNull(dtos)) {
            List<TpDocatCdocumentoDTO> dtoList = new ArrayList<>();
            for (TpDocatCdocumentoDTO it : dtos) {
                if (Objects.nonNull(it.getDocatDescrCat()) && accepted.contains(it.getDocatDescrCat().toUpperCase())) {
                    dtoList.add(it);
                }
            }
            if (prtpoAcrProc.equalsIgnoreCase(Constant.LIMITI_ETA)
                    || prattAcrAtt.equalsIgnoreCase(Constant.SU_ISTANZA_DI_PARTE)) {
                addLetteraRichiestaToList(dtoList);
            }
            return dtoList;
        }
        return Collections.emptyList();
    }

    private void addLetteraRichiestaToList(List<TpDocatCdocumentoDTO> dtoList) {
        TpDocatCdocumentoDTO letteraDTO = getLetteraRichiestaDTO();
        dtoList.add(letteraDTO);
    }

}
