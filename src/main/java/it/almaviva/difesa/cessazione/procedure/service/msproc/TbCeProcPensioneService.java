package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcPensione;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.PensionRequest;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.TbCeProcPensioneMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.TbCeProcPensioneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class TbCeProcPensioneService {

    private final TbCeProcPensioneRepository tbCeProcPensioneRepository;
    private final TbCeProcPensioneMapper tbCeProcPensioneMapper;

    public TbCeProcPensione saveOrUpdatePensione(Procedure procedure, PensionRequest request) {
        TbCeProcPensione pensione = tbCeProcPensioneRepository.findById(procedure.getId()).orElse(null);
        if (Objects.isNull(pensione)) {
            pensione = tbCeProcPensioneMapper.toEntity(request);
        } else {
            tbCeProcPensioneMapper.updateTbCeProcPensioneFromPensionRequestDTO(pensione, request);
        }
        pensione.setTbCeProcedura(procedure);
        return tbCeProcPensioneRepository.save(pensione);
    }

}
