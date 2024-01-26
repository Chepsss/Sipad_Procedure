package it.almaviva.difesa.cessazione.procedure.service.msproc.documenti_editor.impl;

import it.almaviva.difesa.cessazione.procedure.constant.DocumentStateConst;
import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.constant.TipoDocumento;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ConvertToPdfDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.documenti_editor.InputPreviewDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates.TemplateCriteria;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates.TemplateGenerationDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates.TemplateTypeCriteriaDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.documenti_editor.OutputPreviewDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.DocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.TemplateFilterResponseSearchDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.TemplateResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.TemplateTypeDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.InputDocument;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.ModelloDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpDostaStatoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwDo001TemplateAnagrDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwSg001StgiuridicoMilDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwSg122StgiuridicoDsDTO;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureRepository;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper.DecretoPlaceHolderHelper;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper.DipendenteAnagraficaPlaceHolderHelper;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper.DipendenteStatoGiuridicoPlaceHolderHelper;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper.ProcedimentoPlaceHolderHelper;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper.TbCeProcParereRagPlaceHolderHelper;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.templatehelper.TbCeProcTransitoPlaceHolderHelper;
import it.almaviva.difesa.cessazione.procedure.service.msproc.documenti_editor.DocumentEditorService;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.cessazione.procedure.service.rest.TemplateApiClient;
import it.almaviva.difesa.cessazione.procedure.util.Utils;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbDocumento;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.TbDocumentoList;
import it.almaviva.difesa.documenti.document.service.TbDocumentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DocumentEditorServiceImpl implements DocumentEditorService {

    private final TemplateApiClient templateApiClient;
    private final ProcedureRepository procedureRepository;
    private final ProcedimentoPlaceHolderHelper procedimentoPlaceHolderHelper;
    private final SipadClient sipadClient;
    private final TbDocumentoService documentoService;

    @Override
    @Transactional(readOnly = true)
    public OutputPreviewDocumentDTO preview(InputPreviewDocumentDTO input) {
        OutputPreviewDocumentDTO out = new OutputPreviewDocumentDTO();
        DocumentDTO dto = getDocumentoPdf(input);
        out.setContent(dto.getFile());
        return out;
    }

    @Override
    @Transactional(readOnly = true)
    public TemplateResponseDTO getTemplate(InputDocument document) {
        ArrayList<ModelloDTO> templates = getTemplates(document.getIdTipo(), document.getModello());
        if (templates.isEmpty()) {
            throw new ServiceException(ErrorsConst.TEMPLATE_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ModelloDTO template = templates.stream()
                .filter(model -> model.getModello().equals(document.getModello()))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(template)) {
            log.debug(">>>>>>>>>>> Not found template Model: {}", document.getModello());
            return null;
        }
        Long templateId = template.getId();
        return templateApiClient.getTemplateById(templateId);
    }

    @Override
    public DocumentDTO getDocumentoPdf(InputPreviewDocumentDTO inputPreview) {
        TemplateGenerationDTO input = new TemplateGenerationDTO();
        input.setIsPdf(false);
        input.setForce(true);
        String fileContent = inputPreview.getContent();
        input.setFile(Utils.replaceWrongTags(fileContent));
        input.setModel(retrievePlaceHolderForTemplate(inputPreview));
        input.setStyleCss(getStringOfFileCss());
        DocumentDTO documentDTO = templateApiClient.generateFromFile(input);
        String fileToString = new String(Base64.getDecoder().decode(documentDTO.getFile().getBytes()));
        ConvertToPdfDTO convertToPdfDTO = new ConvertToPdfDTO();
        convertToPdfDTO.setFileContent(fileToString);
        String fileInPdf = templateApiClient.convertToPdf(convertToPdfDTO);
        documentDTO.setFile(fileInPdf);
        return documentDTO;
    }

    @Override
    public HashMap<String, Object> retrievePlaceHolderForTemplate(InputPreviewDocumentDTO document) {
        Long idProcedure = document.getIdProc();
        return retrievePlaceHolders(idProcedure, null);
    }

    @Transactional(readOnly = true)
    public HashMap<String, Object> retrievePlaceHolders(Long idProcedure, Long numAttoDecreto) {
        Procedure procedure = procedureRepository.findProcedureById(idProcedure);
        HashMap<String, Object> model = new HashMap<>();
        //placeholder procedimento
        procedimentoPlaceHolderHelper.addPlaceHolders(model, procedure);
        //placeholder dati transito
        TbCeProcTransitoPlaceHolderHelper.addPlaceHolders(model, procedure.getTbCeProcTransito());
        //placeholder parere ragioneria
        TbCeProcParereRagPlaceHolderHelper.addPlaceHolders(model, procedure.getTbCeProcParereRag());

        VwDo001TemplateAnagrDTO templateAnagrDTO = sipadClient.templateAnagrDTO(procedure.getEmployeeId());
        //placeholder anagrafica employee
        DipendenteAnagraficaPlaceHolderHelper.addPlaceHolders(model, templateAnagrDTO);

        VwSg122StgiuridicoDsDTO sg122StgiuridicoDsDTO = sipadClient.stgiuridicoDsDTO(procedure.getEmployeeId());
        VwSg001StgiuridicoMilDTO sg001StgiuridicoMilDTO = sipadClient.sg001StgiuridicoMilDTO(procedure.getEmployeeId());
        //placeholder stato giuridico employee
        DipendenteStatoGiuridicoPlaceHolderHelper.addPlaceHolders(model, sg122StgiuridicoDsDTO, templateAnagrDTO, sg001StgiuridicoMilDTO);

        TbDocumentoList tbDocumentoList = documentoService.tbDocumentoList(idProcedure);
        List<TbDocumento> tbDocumentos = tbDocumentoList.getTbDocumentos();
        TpDostaStatoDTO stato = sipadClient.dostaStatoByAcr(DocumentStateConst.STERILIZZATO);
        TbDocumento decreto = tbDocumentos.stream()
                .filter(it -> it.getIdTipo().equalsIgnoreCase(TipoDocumento.DECRETO))
                .filter(it -> !it.getIdStato().equals(stato.getId()))
                .findFirst().orElse(null);
        //placeholder decreto
        DecretoPlaceHolderHelper.addPlaceHolders(model, decreto, numAttoDecreto);
        return model;
    }

    @Override
    public String getStringOfFileCss() {
        String fileName = "css/pdf-style.css";
        String fileInString = null;
        try {
            try (InputStream resource = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
                if (Objects.nonNull(resource))
                    fileInString = IOUtils.toString(resource, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("File css not found", e);
            return null;
        }
        return fileInString;
    }

    @Override
    @Transactional(readOnly = true)
    public ArrayList<ModelloDTO> getTemplates(String codice, String nome) {
        TemplateTypeCriteriaDTO templateTypeCriteriaDTO = new TemplateTypeCriteriaDTO();
        templateTypeCriteriaDTO.setDocType(codice);
        List<TemplateTypeDTO> templatesByCode = templateApiClient.getTemplateTypes(templateTypeCriteriaDTO);
        Optional<TemplateTypeDTO> templateType = Optional.empty();
        if (!templatesByCode.isEmpty()) {
            templateType = templatesByCode.stream().findFirst();
        }
        ArrayList<ModelloDTO> modelli = new ArrayList<>();
        if (templateType.isPresent()) {
            Long idTemplate = templateType.get().getId();
            TemplateCriteria filter = new TemplateCriteria();
            filter.setName(nome);
            filter.setTemplateTypeId(idTemplate);
            Page<TemplateFilterResponseSearchDTO> templateFilterResponseSearchDTOS = templateApiClient.searchTemplatesByFilter(filter);
            List<TemplateFilterResponseSearchDTO> content = templateFilterResponseSearchDTOS.getContent();
            LocalDate today = LocalDate.now();
            for (TemplateFilterResponseSearchDTO t : content) {
                LocalDate validityEndDate = t.getValidityEndDate();
                if (Objects.isNull(validityEndDate)) {
                    validityEndDate = LocalDate.of(9999, 12, 31);
                }
                if (!today.isBefore(t.getValidityStartDate()) && !today.isAfter(validityEndDate)) {
                    ModelloDTO modello = new ModelloDTO();
                    modello.setModello(t.getName());
                    modello.setId(t.getId());
                    modelli.add(modello);
                }
            }
            return modelli;
        } else {
            return new ArrayList<>();
        }
    }

}
