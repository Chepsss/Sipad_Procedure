package it.almaviva.difesa.cessazione.procedure.model.mapper.msproc;

import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureCompleteDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.ProcedureDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrattAttivazioneDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpPrtpoTprocedimentoDTO;
import it.almaviva.difesa.cessazione.procedure.service.msproc.common.CommonService;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProcedureCompleteMapper implements CommonService {

    private final SipadClient sipadClient;
    private final ProcedureMapper procedureMapper;

    public synchronized ProcedureCompleteDTO toDto(Procedure procedure) {
        ProcedureDTO procedureDTO = procedureMapper.toDto(procedure);
        return toDto(procedureDTO);
    }

    public ProcedureCompleteDTO toDto(ProcedureDTO procedureDTO) {
        ProcedureCompleteDTO target = new ProcedureCompleteDTO(procedureDTO);
        Long openingCessationId = procedureDTO.getOpeningCessationId();
        if (Objects.nonNull(openingCessationId)) {
            TpPrattAttivazioneDTO openingCessationDTO = sipadClient.prattAttivazioneById(openingCessationId);
            target.setOpeningCessation(openingCessationDTO);
        }
        if (Objects.nonNull(procedureDTO.getTypeCessationId())) {
            TpPrtpoTprocedimentoDTO ty = sipadClient.tipoProcedimentoById(procedureDTO.getTypeCessationId());
            target.setTypeCessation(ty);
        }
        if (Objects.nonNull(procedureDTO.getCategLeaveReqId())) {
            target.setCategLeaveReq(sipadClient.categoryOfLeaveById(procedureDTO.getCategLeaveReqId()));
        }
        if (Objects.nonNull(procedureDTO.getCatMilitareId())) {
            target.setCatMilitare(sipadClient.catMilitareById(procedureDTO.getCatMilitareId()));
        }
        if (Objects.nonNull(procedureDTO.getAuthorityId())) {
            target.setAuthority(sipadClient.stentEnteById(procedureDTO.getAuthorityId()));
        }
        //cr16
            if (Objects.nonNull(procedureDTO.getAuthorityId_cc1())) {
                target.setAuthority_cc1(sipadClient.stentEnteById(procedureDTO.getAuthorityId_cc1()));
            }
            if (Objects.nonNull(procedureDTO.getAuthorityId_cc2())) {
                target.setAuthority_cc2(sipadClient.stentEnteById(procedureDTO.getAuthorityId_cc2()));
            }
            if (Objects.nonNull(procedureDTO.getAuthorityId_cc3())) {
                target.setAuthority_cc3(sipadClient.stentEnteById(procedureDTO.getAuthorityId_cc3()));
            }
            //fine cr16

        if (Objects.nonNull(procedureDTO.getCatDocumentoId())) {
            if (procedureDTO.getCatDocumentoId().equals(-1L)) {
                target.setCatDocumento(getLetteraRichiestaDTO());
            } else {
                target.setCatDocumento(sipadClient.docatCDocumentoById(procedureDTO.getCatDocumentoId()));
            }
        }
        if (Objects.nonNull(procedureDTO.getTpStproProvinciaId())) {
            target.setTpStproProvincia(sipadClient.stproProvinciaById(procedureDTO.getTpStproProvinciaId()));
        }
        if (Objects.nonNull(procedureDTO.getIdComune())) {
            target.setTpStcomComune(sipadClient.stcomComuneById(procedureDTO.getIdComune()));
        }
        if (Objects.nonNull(procedureDTO.getIdNazione())) {
            target.setTpStnazNazione(sipadClient.stnazNazioneById(procedureDTO.getIdNazione()));
        }
        return target;
    }

}
