package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrtpoTprocedimentoDTO;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class TpPrtpoTprocedimentoService {

    private final SipadClient sipadClient;

    public List<TpPrtpoTprocedimentoDTO> getTypeCessation(String type) {
        return sipadClient.tipiProcedimento(type);
    }

    public TpPrtpoTprocedimentoDTO getTypeCessationById(Long id) {
        return sipadClient.tipoProcedimentoById(id);
    }

}
