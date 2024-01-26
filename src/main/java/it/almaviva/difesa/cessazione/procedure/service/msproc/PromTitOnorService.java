package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.model.common.GenericResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwSg155StgiuridicoDTO;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.cessazione.procedure.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromTitOnorService {

    private final SipadClient sipadClient;

    public GenericResponse promozioneATitoloOnorificoDefault(String acronimoTipoProcedimento, Long employeeId) {
        GenericResponse response = new GenericResponse();
        if (Utils.TIPI_PROCEDIMENTO.contains(acronimoTipoProcedimento)) {
            VwSg155StgiuridicoDTO sg155StgiuridicoDTO = sipadClient.getEmployeeById(employeeId);
            Short sg155ValGerarchia = sg155StgiuridicoDTO.getSg155ValGerarchia();
            if (Utils.GERARCHIE.contains(sg155ValGerarchia)) {
                response.setMessage("false");
            }
        }
        return response;
    }

}
