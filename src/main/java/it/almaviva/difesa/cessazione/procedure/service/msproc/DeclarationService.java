package it.almaviva.difesa.cessazione.procedure.service.msproc;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Declaration;
import it.almaviva.difesa.cessazione.procedure.model.camunda.CamundaVariable;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.DeclarationDTO;
import it.almaviva.difesa.cessazione.procedure.model.mapper.msproc.DeclarationMapper;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.DeclarationRepository;
//import liquibase.repackaged.org.apache.commons.collections4.map.LinkedMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeclarationService {

    private final DeclarationRepository declarationRepository;
    private final DeclarationMapper declarationMapper;
    private final CamundaService camundaService;

    public List<DeclarationDTO> getAllDeclarations() {
        List<Declaration> declarations = declarationRepository.findAll();
        return declarationMapper.toDto(declarations);
    }

    public List<DeclarationDTO> findAllDeclarationsByCodeList(Map<String, CamundaVariable> variables) {
        Map<String, Integer> declarationsMap = camundaService.getDeclarationVisibility(variables);
        ArrayList<String> declarationCodeList = new ArrayList<>(declarationsMap.keySet());
        List<Declaration> declarationList = declarationRepository.findAllByCodiceIn(declarationCodeList);
        Map<String, Integer> sortedDeclarationMap = new LinkedMap<>();
        declarationsMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedDeclarationMap.put(x.getKey(), x.getValue()));
        List<DeclarationDTO> sortedDeclarationDTOList = new ArrayList<>();
        sortedDeclarationMap.forEach((key, integer) -> {
            if (key.equals(Constant.NUM_VERB_COMM_AV)) {
                DeclarationDTO declarationDTO = new DeclarationDTO();
                declarationDTO.setCodice(Constant.NUM_VERB_COMM_AV);
                declarationDTO.setDescrizione(Constant.NUM_VERB_COMM_AV_DESC);
                sortedDeclarationDTOList.add(declarationDTO);
            } else {
                Optional<Declaration> declarationOpt = declarationList.stream().filter(declaration -> declaration.getCodice().equals(key)).findFirst();
                declarationOpt.ifPresent(declaration -> sortedDeclarationDTOList.add(declarationMapper.toDto(declaration)));
            }
        });
        return sortedDeclarationDTOList;
    }

}
