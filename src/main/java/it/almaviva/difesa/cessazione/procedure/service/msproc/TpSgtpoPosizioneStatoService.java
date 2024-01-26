package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.CategoriesOfLeaveRequestDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.EmployeeCategoryDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgtpoPosizioneStatoDTO;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.TpCeconCategCongRepository;
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
public class TpSgtpoPosizioneStatoService {

    private final SipadClient sipadClient;
    private final TpCeconCategCongRepository tpCeconCategCongRepository;

    public List<TpSgtpoPosizioneStatoDTO> getCategoryOfLeave(Long idReason) {
        List<String> categoryOfLeaveIds = tpCeconCategCongRepository.getCategoryOfLeaveIds(idReason);
        try {
            CategoriesOfLeaveRequestDTO categories = new CategoriesOfLeaveRequestDTO();
            categories.setIds(categoryOfLeaveIds);
            return sipadClient.categoriesOfLeave(categories);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<TpSgtpoPosizioneStatoDTO> getCategoryOfLeaveNonAdmissibility(Long idTypeCessation) {
        List<String> categCongIds = tpCeconCategCongRepository.getCategoryCongIds(idTypeCessation);
        try {
            CategoriesOfLeaveRequestDTO categories = new CategoriesOfLeaveRequestDTO();
            categories.setIds(categCongIds);
            return sipadClient.categoriesOfLeaveNonAdmissibility(categories);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<TpSgtpoPosizioneStatoDTO> getCategLeaveReqByAcr(String acrPosizione) {
        try {
            return sipadClient.categoriesOfLeaveByAcr(acrPosizione);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public EmployeeCategoryDTO getCategories() {
        try {
            return sipadClient.employeeCategories();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TpSgtpoPosizioneStatoDTO getCategLeaveReqById(String id) {
        try {
            return sipadClient.categoryOfLeaveById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
