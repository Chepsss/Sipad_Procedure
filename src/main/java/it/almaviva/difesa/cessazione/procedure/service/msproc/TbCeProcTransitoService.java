package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcTransito;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.TbCeProcTransitoDTORequest;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.TbCeProcTransitoMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.TbCeProcTransitoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Service Implementation for managing {@link TbCeProcTransito}.
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class TbCeProcTransitoService {

    private final TbCeProcTransitoRepository tbCeProcTransitoRepository;
    private final TbCeProcTransitoMapper tbCeProcTransitoMapper;

    public TbCeProcTransito saveOrUpdateTransition(Procedure procedure, TbCeProcTransitoDTORequest request) {
        TbCeProcTransito transito = tbCeProcTransitoRepository.findById(procedure.getId()).orElse(null);
        if (Objects.isNull(transito)) {
            transito = tbCeProcTransitoMapper.toEntity(request);
        } else {
            tbCeProcTransitoMapper.updateTbCeProcTransitoFromTransitRequestDTO(transito, request);
        }
        transito.setTbCeProcedura(procedure);
        return tbCeProcTransitoRepository.save(transito);
    }

}
