package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.StaffCategoryDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgctpCatpersonaleDTO;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TpSgctpCatPersonaleService {

    private final SipadClient sipadClient;

    public StaffCategoryDTO getStaffCategories() {
        try {
            return sipadClient.staffCategories();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TpSgctpCatpersonaleDTO getCatMilitareById(Long id) {
        try {
            return sipadClient.catMilitareById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
