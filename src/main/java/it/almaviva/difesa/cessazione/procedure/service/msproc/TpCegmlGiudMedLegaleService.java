package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCegmlGiudMedLegale;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCegmlGiudMedLegaleDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.TpCegmlGiudMedLegaleMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.TpCegmlGiudMedLegaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class TpCegmlGiudMedLegaleService {

    private final TpCegmlGiudMedLegaleRepository tpCegmlGiudMedLegaleRepository;
    private final TpCegmlGiudMedLegaleMapper tpCegmlGiudMedLegaleMapper;

    public List<TpCegmlGiudMedLegaleDTO> getFirstTypeInstanceOfGML() {
        List<TpCegmlGiudMedLegale> tpCegmlGiudMedLegales = tpCegmlGiudMedLegaleRepository.getTpCegmlGiudMedLegaleByListaGml1IsTrueOrderById();
        return tpCegmlGiudMedLegaleMapper.toDto(tpCegmlGiudMedLegales);
    }

    public List<TpCegmlGiudMedLegaleDTO> getSecondTypeInstanceOfGML() {
        List<TpCegmlGiudMedLegale> tpCegmlGiudMedLegales = tpCegmlGiudMedLegaleRepository.getTpCegmlGiudMedLegaleByListaGml2IsTrueOrderById();
        return tpCegmlGiudMedLegaleMapper.toDto(tpCegmlGiudMedLegales);
    }

}
