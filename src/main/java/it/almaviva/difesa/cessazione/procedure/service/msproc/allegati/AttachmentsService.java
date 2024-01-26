package it.almaviva.difesa.cessazione.procedure.service.msproc.allegati;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.ProcedureStateConst;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.AllegatiList;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.AllegatoActionsDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.allegati.AllegatoDto;
import it.almaviva.difesa.cessazione.procedure.model.mapper.sipad.AllegatiMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureRepository;
import it.almaviva.difesa.documenti.document.model.dto.response.allegati.AllegatiOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.allegati.ListaAllegatiOut;
import it.almaviva.difesa.documenti.document.service.AllegatiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentsService {

    private final AllegatiService allegatiService;
    private final AllegatiMapper allegatiMapper;
    private final ProcedureRepository procedureRepository;

    @Transactional
    public AllegatiList list(long idProcedimento) {
        ListaAllegatiOut list = allegatiService.list(idProcedimento);
        List<AllegatiOutput> allegati = list.getAllegati();
        Procedure procedure = procedureRepository.findProcedureById(idProcedimento);
        String statoProcedura = procedure.getStateProcedure().getCodeState();
        ArrayList<AllegatoDto> allegatiDto = new ArrayList<>();
        for (AllegatiOutput each : allegati) {
            AllegatoDto allegatoDto = allegatiMapper.copyProperties(each);
            allegatoDto.setNomeCognome(each.getNomeCognome()); /* Strano che il copyProperties non lo faccia da solo.. approfondiremo.. */
            actionMapping(allegatoDto, each, statoProcedura);
            allegatiDto.add(allegatoDto);
        }
        AllegatiList out = new AllegatiList();
        out.setAllegati(allegatiDto);
        return out;
    }

    private void actionMapping(AllegatoDto target, AllegatiOutput source, String statoProcedura) {
        AllegatoActionsDto azioni = new AllegatoActionsDto();
        azioni.setVisualizza(true);
        azioni.setAssDissPredisposizione(true);
        azioni.setAssDissProtocolloUscita(source.getCodTipoAllegato().equals(Constant.COD_TIPO_ALLEGATO_ADHOC));
        azioni.setElimina(true);
        if (ProcedureStateConst.CHIUSO.equals(statoProcedura)
                || ProcedureStateConst.CHIUSO_ANT.equals(statoProcedura)) {
            azioni.setElimina(false);
        }
        target.setAzioni(azioni);
    }

}
