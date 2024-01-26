package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcParereRag;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ParereRagRequest;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.TbCeProcParereRagMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.TbCeProcParereRagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Service Implementation for managing {@link TbCeProcParereRag}.
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class TbCeProcParereRagService {

    private final TbCeProcParereRagRepository tbCeProcParereRagRepository;
    private final TbCeProcParereRagMapper tbCeProcParereRagMapper;

    public TbCeProcParereRag saveOrUpdateParereRag(Procedure procedure, ParereRagRequest request) {
        TbCeProcParereRag parere = tbCeProcParereRagRepository.findById(procedure.getId()).orElse(null);
        if (Objects.isNull(parere)) {
            parere = tbCeProcParereRagMapper.toEntity(request);
        } else {
            tbCeProcParereRagMapper.updateTbCeProcParereRagFromParereRagRequestDTO(parere, request);
        }
        parere.setTbCeProcedura(procedure);
        return tbCeProcParereRagRepository.save(parere);
    }

}
