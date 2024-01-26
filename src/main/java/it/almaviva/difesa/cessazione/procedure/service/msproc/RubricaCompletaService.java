package it.almaviva.difesa.cessazione.procedure.service.msproc;


import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.RubricaCompletaDTO;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class RubricaCompletaService {


    private final SipadClient sipadClient;
    public List<RubricaCompletaDTO> getRubricaCompletaDenomEnteCC(String denomEnteCC) {

        try {
            return sipadClient.getRubricaCompletaDenomEnteCC(denomEnteCC);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
