package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.DeclarationProcedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.TbCeProcPensione;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaVariable;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpSgtpoPosizioneStatoDTO;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CongedoSpettanteProcedureService {

    private final DeclarationProcedureService declarationProcedureService;
    private final CamundaService camundaService;
    private final TpSgtpoPosizioneStatoService tpSgtpoPosizioneStatoService;
    private final ProcedureRepository procedureRepository;

    public void setCongedoSpettante(Procedure procedure, TbCeProcPensione pensionData) {
        Map<String, CamundaVariable> variables = new HashMap<>();
        Boolean D01 = null;
        Boolean C08 = null;
        Integer anniServizioEff = null;
        Optional<DeclarationProcedure> declarationD01 = declarationProcedureService.getControl(procedure, Constant.DECLARATION_01);
        Optional<DeclarationProcedure> controlC08 = declarationProcedureService.getControl(procedure, Constant.CONTROL_08);
        if (declarationD01.isPresent()) {
            D01 = declarationD01.get().getFlagDich();
        }
        if (controlC08.isPresent()) {
            C08 = controlC08.get().getFlagDich();
        }
        if (Objects.nonNull(pensionData) && Objects.nonNull(pensionData.getAnniServizioEff())) {
            anniServizioEff = pensionData.getAnniServizioEff();
        }
        String catPersSpettante = camundaService.getCongedoSpettante(procedure, variables, D01, C08, anniServizioEff);
        if (Objects.nonNull(catPersSpettante)) {
            List<TpSgtpoPosizioneStatoDTO> categories = tpSgtpoPosizioneStatoService.getCategLeaveReqByAcr(catPersSpettante);
            Optional<TpSgtpoPosizioneStatoDTO> catSpett = categories.stream().findFirst();
            catSpett.ifPresent(tpSgtpoPosizioneStato -> {
                procedure.setIdCatPersSpettante(tpSgtpoPosizioneStato.getSgtpoDescrPosizione());
                procedureRepository.save(procedure);
            });
        }
    }

}
