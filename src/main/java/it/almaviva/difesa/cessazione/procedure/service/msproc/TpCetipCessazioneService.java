package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TpCetipCessazione;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.TpCetipCessazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ReasonCessationRequest;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.TpCetipCessazioneCompleteMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.TpCetipCessazioneRepository;
import it.almaviva.difesa.cessazione.procedure.repository.specification.TpCetipCessazioneSpecification;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class TpCetipCessazioneService {

    private final TpCetipCessazioneRepository tpCetipCessazioneRepository;
    private final TpCetipCessazioneCompleteMapper tpCetipCessazioneMapper;
    private final SipadClient sipadClient;

    public List<TpCetipCessazioneDTO> getReasonOfCessation(ReasonCessationRequest req) {
        List<Long> ids = sipadClient.tipoProcedimentoIds(req.getPrattAcrAtt(), Constant.ACR_PROC, req.getPrtpoAcrProc());
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        List<TpCetipCessazione> tpCetipCessaziones = tpCetipCessazioneRepository.findAll(
                TpCetipCessazioneSpecification.reasonOfCessationSpecification(ids, req.getStfaaAcrFfaa(), req.getCategoryPers()));
        return tpCetipCessaziones.parallelStream().map(tpCetipCessazioneMapper::toDto).collect(Collectors.toList());
    }

    public TpCetipCessazione getReasonOfCessationById(Long id) {
        return tpCetipCessazioneRepository.findById(id).orElse(null);
    }

}
