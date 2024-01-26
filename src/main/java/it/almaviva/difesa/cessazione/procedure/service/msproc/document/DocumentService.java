package it.almaviva.difesa.cessazione.procedure.service.msproc.document;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.DocumentStateConst;
import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.constant.TipoDocumento;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.common.CustomUserDetailDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ChangeDocNameRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ConvertToPdfDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.SignedDocumentRequest;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.documenti_editor.InputPreviewDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates.TemplateCriteria;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates.TemplateGenerationDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.documenti_editor.OutputPreviewDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.MatchPlaceholdersDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.TemplateResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.DocEsterniList;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.DocEsternoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.DocGenerationOutput;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.FileOutputResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.InputDocument;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.InputDocumentExt;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.InputDocumentFromTemplateDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.ListaModelliOutput;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.ModelloDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.UpdateDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpDostaStatoDTO;
import it.almaviva.difesa.cessazione.procedure.repository.dao.NumeroAttoDao;
import it.almaviva.difesa.cessazione.procedure.repository.msproc.ProcedureRepository;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.ProcedureService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.documenti_editor.DocumentEditorService;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.cessazione.procedure.service.rest.TemplateApiClient;
import it.almaviva.difesa.cessazione.procedure.util.Utils;
import it.almaviva.difesa.documenti.document.data.sipad.repository.PredisposizioneRepository;
import it.almaviva.difesa.documenti.document.data.sipad.repository.TbDocumentoRepository;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbDocumento;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbSegnatura;
import it.almaviva.difesa.documenti.document.model.dto.request.MittenteDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizioneUsita.PredisposizioneInput;
import it.almaviva.difesa.documenti.document.model.dto.request.segnatura.SegnaturaInput;
import it.almaviva.difesa.documenti.document.model.dto.request.tb_document.DocumentDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.tb_document.NotificaInput;
import it.almaviva.difesa.documenti.document.model.dto.request.tb_document.UpdateFileDto;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.DettaglioDocumentoOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.DocumentList;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.DocumentOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.FileOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.PredisposizioneDto;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.SegnaturaDto;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.SuccessOutput;
import it.almaviva.difesa.documenti.document.model.dto.response.documenti.TbDocumentoList;
import it.almaviva.difesa.documenti.document.model.dto.response.predisposizione.Documento;
import it.almaviva.difesa.documenti.document.model.dto.response.predisposizione.Protocollo;
import it.almaviva.difesa.documenti.document.model.dto.response.predisposizione.ProtocolloList;
import it.almaviva.difesa.documenti.document.model.dto.response.titolario.TitolarioDto;
import it.almaviva.difesa.documenti.document.service.AdhocService;
import it.almaviva.difesa.documenti.document.service.PredisposizioneService;
import it.almaviva.difesa.documenti.document.service.SegnaturaService;
import it.almaviva.difesa.documenti.document.service.TbDocumentoService;
import it.almaviva.difesa.documenti.document.service.TitolarioService;
import it.almaviva.difesa.documenti.document.utils.DateUtils;
import it.almaviva.difesa.documenti.document.utils.FileNameUtils;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class DocumentService {

    @Value("${isAdhocMock:#{false}}")
    private Boolean useMock;

    @Value("${adhoc.regex.register}")
    private String register;

    @Value("${adhoc.regex.numProtocol}")
    private String numProtocol;

    @Value("${adhoc.regex.date}")
    private String date;

    @Value("${adhoc.regex.year}")
    private String year;

    private final TbDocumentoService documentoService;
    private final SegnaturaService segnaturaService;
    private final AdhocService adhocService;
    private final TemplateApiClient templateApiClient;
    private final SipadClient sipadClient;
    private final PredisposizioneRepository predisposizioneRepository;
    private final DocumentEditorService documentEditorService;
    private final TbDocumentoRepository documentoRepository;
    private final NumeroAttoDao numeroAttoDao;
    private final ProcedureService procedureService;
    private final ProcedureRepository procedureRepository;
    private final SecurityService securityService;
    private final PredisposizioneService predisposizioneService;
    private final TitolarioService titolarioService;

    @Transactional
    public DocGenerationOutput insertDocument(@Valid InputDocument document) throws ServiceException {
        try {
            checkUnivocitaDecreto(document.getIdProcedura(), document.getIdTipo());
            DocumentDTO documentDTO = new DocumentDTO();
            documentDTO.setIdProcedura(document.getIdProcedura());
            documentDTO.setEditabile(document.getEditabile());
            documentDTO.setNomeFile(FileNameUtils.addPdfExtention(document.getNomeFile()));
            Long numAttoDecreto = getNumAttoDecreto(document.getIdProcedura(), document.getIdTipo());
            var generation = fileGeneration(document, numAttoDecreto);
            if (Boolean.FALSE.equals(generation.getError())) {
                documentDTO.setFile(generation.getFile());
                documentDTO.setIdTipo(document.getIdTipo());
                documentDTO.setTemplateId(generation.getTemplateId());
                if (!TipoDocumento.DECRETO.equalsIgnoreCase(document.getIdTipo())) {
                    DocumentOutput output = documentoService.insert(documentDTO);
                    updateProcedureLastModified(output.getIdProc(), null);
                } else {
                    DocGenerationOutput letteraTrasmissione = createLetteraTrasmissione(document);
                    if (Boolean.FALSE.equals(letteraTrasmissione.getError())) {
                        saveDecretoWithDefaultSegnature(documentDTO, numAttoDecreto);
                        letteraTrasmissione.setName(Constant.LETTERA_TRASMISSIONE);
                        return letteraTrasmissione;
                    }
                }
                return new DocGenerationOutput();
            } else {
                DocGenerationOutput out = new DocGenerationOutput();
                out.setPlaceholderErrors(generation.getPlaceholderErrors());
                out.setError(true);
                return out;
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>> ERROR ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void checkUnivocitaDecreto(Long idProcedura, String idTipo) {
        if (TipoDocumento.DECRETO.equalsIgnoreCase(idTipo)) {
            Long count = documentoService.countDocuments(idProcedura, idTipo);
            if (count > 0) {
                throw new ServiceException(ErrorsConst.DECRETO_ALREADY_EXISTS, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private Long getNumAtto() {
        return documentoService.generateNumAtto();
    }

    @Synchronized
    public Long getNumAtto(Procedure procedure) {
        Long numAtto = numeroAttoDao.generaNumAtto();
        procedure.setNumAttoSipad(numAtto);
        procedure.setAnnoAttoSipad(Calendar.getInstance().get(Calendar.YEAR));
        procedureRepository.save(procedure);
        return numAtto;
    }

    private it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.DocumentDTO fileGeneration(InputDocument document, Long numAttoDecreto) {
        ArrayList<ModelloDTO> templates = documentEditorService.getTemplates(document.getIdTipo(), document.getModello());
        if (templates.isEmpty()) {
            throw new ServiceException(ErrorsConst.TEMPLATE_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Long idTemplate = templates.get(0).getId();
        TemplateGenerationDTO input = new TemplateGenerationDTO();
        input.setTemplateId(idTemplate);
        input.setIsPdf(!document.getEditabile());
        input.setForce(document.getForce());
        input.setModel(retrievePlaceHolderForTemplate(document, numAttoDecreto));
        input.setStyleCss(documentEditorService.getStringOfFileCss());
        it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.DocumentDTO documentDTO = templateApiClient.generateTemplate(input);
        documentDTO.setTemplateId(idTemplate);
        return documentDTO;
    }

    private HashMap<String, Object> retrievePlaceHolderForTemplate(InputDocument document, Long numAttoDecreto) {
        Long idProcedura = document.getIdProcedura();
        return documentEditorService.retrievePlaceHolders(idProcedura, numAttoDecreto);
    }

    private DocGenerationOutput createLetteraTrasmissione(InputDocument document) throws ServiceException {
        ArrayList<ModelloDTO> templates = documentEditorService.getTemplates(TipoDocumento.LETTERA_TRASMISSIONE, null);
        if (templates.size() > 1) {
            throw new ServiceException(ErrorsConst.CREATE_DOCUMENTS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (templates.isEmpty()) {
            throw new ServiceException(ErrorsConst.TEMPLATE_NOT_EXISTS, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (Boolean.TRUE.equals(document.getEditabile())) {
            DocumentDTO letteraTrasmissione = new DocumentDTO();

            Long idTemplate = templates.get(0).getId();
            TemplateResponseDTO templ = templateApiClient.getTemplateById(idTemplate);
            String file = new String(Base64.getEncoder().encode(templ.getContent().getBytes()));

            letteraTrasmissione.setIdProcedura(document.getIdProcedura());
            letteraTrasmissione.setEditabile(document.getEditabile());
            letteraTrasmissione.setNomeFile(FileNameUtils.addPdfExtention(document.getNomeLetteraTrasmissione()));
            letteraTrasmissione.setFile(file);
            letteraTrasmissione.setIdTipo(TipoDocumento.LETTERA_TRASMISSIONE);
            letteraTrasmissione.setTemplateId(idTemplate);
            documentoService.insert(letteraTrasmissione);
            return new DocGenerationOutput();
        } else {
            InputDocument input = new InputDocument();
            input.setModello(templates.get(0).getModello());
            input.setEditabile(document.getEditabile());
            input.setIdProcedura(document.getIdProcedura());
            input.setNomeFile(document.getNomeLetteraTrasmissione());
            input.setIdTipo(TipoDocumento.LETTERA_TRASMISSIONE);
            return insertDocument(input);
        }
    }

    private void saveDecretoWithDefaultSegnature(DocumentDTO documentDTO, Long numAttoDecreto) {
        DocumentOutput dOut = documentoService.insert(documentDTO);
        Long id = Long.parseLong(dOut.getId());
        creaSegnaturaDefaultPerDecreto(id, numAttoDecreto);
    }

    private void creaSegnaturaDefaultPerDecreto(Long idDocumento, Long numAttoDecreto) {
        TbSegnatura segnatura = segnaturaService.getOrCreateFromDocumento(idDocumento);
        segnatura.setDenomMittente("mittente");
        segnatura.setMailMittente("mittente@mittente.it");
        TbDocumento documento = segnatura.getDocumento();

        Long numAtto = Objects.isNull(numAttoDecreto) ? getNumAtto() : numAttoDecreto;
        documento.setNumAtto(numAtto);
        Calendar now = Calendar.getInstance();
        Integer annoAtto = now.get(Calendar.YEAR);
        documento.setAnnoAtto(annoAtto);
        documento.setNumAttoProcedimento(String.format(Constant.NUM_ATTO_SIPAD, numAtto, annoAtto));
        documento.setOggetto(String.format(Constant.DOC_OGGETTO, documento.getNumAttoProcedimento()));
        segnatura.setOggetto(documento.getOggetto());
        segnatura.setNumReg(segnaturaService.generateRegistro());

        TitolarioDto titolarioDto = titolarioDefault(idDocumento, Constant.TIPO_SOTTOFASCICOLO_TITOLARIO_DEFAULT);
        segnatura.setTitolo(titolarioDto.getCodTit());
        segnatura.setClasse(titolarioDto.getCodCla());
        segnatura.setSottoclasse(titolarioDto.getCodSotcla());
        segnatura.setFascicolo(titolarioDto.getCodFas());
        segnatura.setSottofascicolo(titolarioDto.getCodSotfas());
        segnatura.setDataReg(LocalDate.now());

        segnaturaService.save(segnatura);
        documentoService.save(documento);
    }

    @Transactional
    public DocGenerationOutput insertDocumentFromTemplate(@Valid InputDocumentFromTemplateDTO document) throws ServiceException {
        try {
            checkUnivocitaDecreto(document.getIdProcedura(), document.getIdTipo());
            DocumentDTO documentDTO = new DocumentDTO();
            documentDTO.setIdProcedura(document.getIdProcedura());
            documentDTO.setEditabile(true);
            documentDTO.setNomeFile(FileNameUtils.addPdfExtention(document.getNomeFile()));
            Long templateId = getTemplateId(document);
            matchPlaceholders(document.getFile(), templateId);
            documentDTO.setFile(document.getFile());
            documentDTO.setIdTipo(document.getIdTipo());
            documentDTO.setTemplateId(templateId);
            if (!TipoDocumento.DECRETO.equalsIgnoreCase(document.getIdTipo())) {
                DocumentOutput documentOutput = documentoService.insert(documentDTO);
                updateProcedureLastModified(documentOutput.getIdProc(), null);
            } else {
                InputDocument inputLetteraTrasm = new InputDocument();
                inputLetteraTrasm.setIdProcedura(document.getIdProcedura());
                inputLetteraTrasm.setEditabile(true);
                inputLetteraTrasm.setIdTipo(document.getIdTipo());
                inputLetteraTrasm.setNomeLetteraTrasmissione(FileNameUtils.addPdfExtention(document.getNomeLetteraTrasmissione()));
                DocGenerationOutput letteraTrasmissione = createLetteraTrasmissione(inputLetteraTrasm);
                if (Boolean.FALSE.equals(letteraTrasmissione.getError())) {
                    Long numAttoDecreto = getNumAttoDecreto(document.getIdProcedura(), document.getIdTipo());
                    saveDecretoWithDefaultSegnature(documentDTO, numAttoDecreto);
                    letteraTrasmissione.setName(Constant.LETTERA_TRASMISSIONE);
                    return letteraTrasmissione;
                }
            }
            return new DocGenerationOutput();
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>> ERROR ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void matchPlaceholders(String content, Long templateId) {
        String htmlContent = new String(Base64.getDecoder().decode(content), StandardCharsets.UTF_8);
        MatchPlaceholdersDTO matchPlaceholdersDTO = templateApiClient.checkPlaceholdersMatch(htmlContent, templateId);
        if (Boolean.FALSE.equals(matchPlaceholdersDTO.getPlaceholdersDoMatch())) {
            throw new ServiceException(ErrorsConst.PLACEHOLDERS_NOT_MATCH, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Long getTemplateId(InputDocumentFromTemplateDTO document) {
        TemplateCriteria templateCriteria = new TemplateCriteria();
        templateCriteria.setName(document.getModello());
        TemplateResponseDTO template = templateApiClient.getTemplateByName(templateCriteria);
        return template.getId();
    }

    private Long getNumAttoDecreto(Long idProc, String idTipoDoc) {
        Long numAttoDecreto = null;
        Procedure procedure = procedureRepository.findById(idProc)
                .orElseThrow(() -> new ServiceException(String.format(">>> Error Not found Procedure with ID %d", idProc), HttpStatus.INTERNAL_SERVER_ERROR));
        if (idTipoDoc.equalsIgnoreCase(TipoDocumento.DECRETO) ||
                (idTipoDoc.equalsIgnoreCase(TipoDocumento.LETTERA_TRASMISSIONE) && Objects.isNull(procedure.getNumAttoSipad()))) {
            numAttoDecreto = getNumAtto(procedure);
        } else if (Objects.nonNull(procedure.getNumAttoSipad())) {
            numAttoDecreto = procedure.getNumAttoSipad();
        }
        return numAttoDecreto;
    }

    @Transactional
    public SuccessOutput updateDocument(UpdateDocumentDTO updateDocumentDTO) {
        SuccessOutput successOutput = new SuccessOutput();
        String fileContent = updateDocumentDTO.getFile();
        String idDocumento = updateDocumentDTO.getIdDocumento();
        DettaglioDocumentoOutput documento = documentoService.dettaglio(Long.parseLong(idDocumento));
        Long templateId = documento.getTemplateId();
        checkValiditaTemplate(templateId);
        matchPlaceholders(fileContent, templateId);
        try {
            UpdateFileDto fileDto = new UpdateFileDto();
            fileDto.setIdDocumento(idDocumento);
            fileDto.setFile(fileContent);
            successOutput = documentoService.updateFile(fileDto);
            if (Boolean.TRUE.equals(successOutput.getSuccess())) {
                updateProcedureLastModified(null, Long.parseLong(updateDocumentDTO.getIdDocumento()));
            }
        } catch (Exception e) {
            log.error("Error in updateDocument", e);
            successOutput.setSuccess(false);
        }
        return successOutput;
    }

    private void checkValiditaTemplate(Long templateId) {
        TemplateResponseDTO template = templateApiClient.getTemplateById(templateId);
        if (Objects.isNull(template.getValidityEndDate())) {
            template.setValidityEndDate(LocalDate.of(9999, 12, 31));
        }
        if (ChronoLocalDate.from(ChronoLocalDate.from(LocalDate.now())).isAfter(ChronoLocalDate.from(template.getValidityStartDate()))
                && ChronoLocalDate.from(template.getValidityEndDate()).isBefore(ChronoLocalDate.from(LocalDate.now()))) {
            throw new ServiceException(ErrorsConst.TEMPLATE_NOT_VALID, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public SuccessOutput deleteDocument(Long id) {
        SuccessOutput successOutput = new SuccessOutput();
        try {
            DettaglioDocumentoOutput document = documentoService.dettaglio(id);
            Long procedureId = document.getIdProc();
            String codDocumentType = document.getCodTipoDocumento();
            if (codDocumentType.equalsIgnoreCase(TipoDocumento.DECRETO)) {
                DocumentList procedureDocuments = documentoService.list(procedureId, null);
                Optional<DocumentOutput> lettera = procedureDocuments.getDocumenti().parallelStream()
                        .filter(doc -> (doc.getCodTipo().equalsIgnoreCase(TipoDocumento.LETTERA_TRASMISSIONE)
                                && !doc.getStato().equalsIgnoreCase(DocumentStateConst.STERILIZZATO))).findFirst();
                if (lettera.isPresent()) {
                    long letteraId = Long.parseLong(lettera.get().getId());
                    deletePredisposizione(letteraId);
                    documentoService.delete(letteraId);
                }
            } else {
                deletePredisposizione(id);
            }
            successOutput = documentoService.delete(id);
            if (Boolean.TRUE.equals(successOutput.getSuccess())) {
                updateProcedureLastModified(procedureId, null);
            }
        } catch (Exception e) {
            log.error("Error in deleteDocument", e);
            successOutput.setSuccess(false);
        }
        return successOutput;
    }

    private void deletePredisposizione(long docId) {
        predisposizioneRepository.findTbPredisposizioneByDocumentoId(docId)
                .ifPresent(predisposizioneRepository::delete);
    }

    public DocEsterniList consultazioneDocAdhoc(InputDocumentExt input) {
        log.debug("Start consultazioneDocAdhoc input => {}", input);
        String registro;
        String numProtocollo;
        String dataProtocollo;
        long annoProtocollo;
        if (Objects.nonNull(input.getProtocollo())) {
            registro = getProtocolData(input.getProtocollo(), register);
            numProtocollo = getProtocolData(input.getProtocollo(), numProtocol);
            dataProtocollo = getProtocolData(input.getProtocollo(), date);
            if (Objects.isNull(registro) || Objects.isNull(numProtocollo) || Objects.isNull(dataProtocollo)) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorsConst.PROTOCOL_NOT_VALID);
            }
            annoProtocollo = Long.parseLong(getProtocolData(input.getProtocollo(), year));
            DocEsterniList docEsterniList = consultazioneDocAdhoc(registro, annoProtocollo, numProtocollo);
            List<DocEsternoDTO> documenti = docEsterniList.getDocumenti();
            for (DocEsternoDTO doc : documenti) {
                doc.setDataProtocollo(dataProtocollo);
            }
            return docEsterniList;
        } else {
            registro = input.getRegistro();
            annoProtocollo = Long.parseLong(input.getAnno());
            numProtocollo = input.getNumero();
            return consultazioneDocAdhoc(registro, annoProtocollo, numProtocollo);
        }
    }

    public String getProtocolData(String protocollo, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(protocollo);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public DocEsterniList consultazioneDocAdhoc(String registro, Long year, String number) {
        try {
            ProtocolloList protocolloList = adhocService.dettaglioProtocollo(registro.toUpperCase(), year.intValue(), Integer.parseInt(number));
            List<Protocollo> protocolli = protocolloList.getProtocolli();
            String codiceRegistro = "REG" + year;
            DocEsterniList out = new DocEsterniList();
            List<DocEsternoDTO> documenti = new ArrayList<>();
            for (Protocollo protocollo : protocolli) {
                if (codiceRegistro.equalsIgnoreCase(protocollo.getCodiceRegistro())) {
                    documenti.addAll(documentiDto(protocollo));
                }
            }
            aggiustaOutputCasoMock(documenti, year);
            out.setDocumenti(documenti);
            return out;
        } catch (Exception e) {
            log.error("Errore in consultazioneDocAdhoc =>", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(ErrorsConst.RICERCA_PROTOCOLLI_ERROR_MESSAGE, e.getMessage()));
        }
    }

    @Transactional
    public TitolarioDto titolarioDefault(Long documentId, String tipoSottofascicolo) {
        TbDocumento documento = documentoRepository.findById(documentId).orElse(null);
        if (Objects.isNull(documento)) {
            throw new ServiceException(String.format(ErrorsConst.DOCUMENT_NOT_FOUND, documentId), HttpStatus.NOT_FOUND);
        }
        Long idProc = documento.getIdProc();
        Procedure procedure = procedureRepository.findProcedureById(idProc);
        Long employeeId = procedure.getEmployeeId();
        return titolarioService.titolarioDefault(employeeId, tipoSottofascicolo);
    }

    private ArrayList<DocEsternoDTO> documentiDto(Protocollo protocollo) {
        ArrayList<DocEsternoDTO> list = new ArrayList<>();
        List<Documento> documenti = protocollo.getDocumenti();
        for (Documento documento : documenti) {
            DocEsternoDTO dto = new DocEsternoDTO();
            dto.setNomeFile(documento.getNomeFile());
            MittenteDTO mittente = protocollo.getMittente();
            if (Objects.nonNull(mittente)) {
                dto.setMail(mittente.getEmail());
                String nomeCognome = String.join(" ",
                        Objects.nonNull(mittente.getNome()) ? mittente.getNome() : "", Objects.nonNull(mittente.getCognome()) ? mittente.getCognome() : "");
                dto.setMittente(nomeCognome);
            }
            dto.setTipoAllegato(documento.getTipoAllegato());
            dto.setSegnatura(protocollo.getSegnatura());
            dto.setObjectID(documento.getObjectID());
            dto.setNumeroProtocollo(protocollo.getNumeroProtocollo());
            dto.setOggetto(protocollo.getOggetto());
            Date dateProtocol = DateUtils.string2Date(protocollo.getDataProtocolloMittente(), Constant.YYYY_MM_DD_HH_MM_SS);
            dto.setDataProtocollo(DateUtils.date2String(dateProtocol, Constant.DD_MM_YYYY));
            list.add(dto);
        }
        return list;
    }

    private void aggiustaOutputCasoMock(List<DocEsternoDTO> documenti, Long year) {
        if (Boolean.TRUE.equals(useMock)) {
            String data = String.format("01-01-%d", year);
            for (DocEsternoDTO doc : documenti) {
                if (Objects.isNull(doc.getDataProtocollo()) || "".equals(doc.getDataProtocollo().trim())) {
                    doc.setDataProtocollo(data);
                }
            }
        }
    }

    public DocumentList listWithPredInUscita(Long idProcedura) {
        DocumentList list = documentoService.list(idProcedura, null);
        List<DocumentOutput> filtered = list.getDocumenti().stream()
                .filter(it -> it.getHaDatiPredisposizione() && it.getProtocollo() != null)
                .collect(Collectors.toList());
        list.setDocumenti(filtered);
        return list;
    }

    @Transactional
    public DocumentOutput saveNotification(NotificaInput notificaInputDTO) {
        try {
            DocumentOutput documentOutput = documentoService.insertDataNotifica(notificaInputDTO);
            updateProcedureLastModified(documentOutput.getIdProc(), null);
            return documentOutput;
        } catch (Exception e) {
            log.error(">>>>>>>>Errore in saveNotification", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(ErrorsConst.ERRORE_IN_SAVE_NOTIFICATION, e.getMessage()));
        }
    }

    public SuccessOutput sterilizeDocument(Long id) {
        TbDocumentoList list = getTbDocumentoList(id);
        SuccessOutput output = new SuccessOutput();
        Optional<TbDocumento> document = getTbDocumento(id, list);
        if (document.isPresent()) {
            TbDocumento tbDocumento = document.get();
            setStato(tbDocumento, DocumentStateConst.STERILIZZATO);
            documentoService.save(tbDocumento);
            output.setSuccess(true);
            if (document.get().getIdTipo().equalsIgnoreCase(TipoDocumento.DECRETO)) {
                TpDostaStatoDTO stato = sipadClient.dostaStatoByAcr(DocumentStateConst.STERILIZZATO);
                Optional<TbDocumento> letteraTrasmissione = list.getTbDocumentos().parallelStream()
                        .filter(doc -> doc.getIdTipo().equalsIgnoreCase(TipoDocumento.LETTERA_TRASMISSIONE)
                                && !doc.getIdStato().equals(stato.getId())).findFirst();
                if (letteraTrasmissione.isPresent()) {
                    TbDocumento lettera = letteraTrasmissione.get();
                    setStato(lettera, DocumentStateConst.STERILIZZATO);
                    documentoService.save(lettera);
                    Optional<Procedure> procedureOpt = procedureRepository.findById(lettera.getIdProc());
                    if (procedureOpt.isPresent()) {
                        Procedure procedure = procedureOpt.get();
                        procedure.setNumAttoSipad(null);
                        procedure.setAnnoAttoSipad(null);
                        procedureRepository.save(procedure);
                    }
                    output.setSuccess(true);
                } else {
                    output.setSuccess(false);
                }
            }
            updateProcedureLastModified(document.get().getIdProc(), null);
        } else {
            output.setSuccess(false);
        }
        return output;
    }

    private TbDocumentoList getTbDocumentoList(Long id) {
        DettaglioDocumentoOutput documentDetail = documentoService.dettaglio(id);
        Long procedureId = documentDetail.getIdProc();
        return documentoService.tbDocumentoList(procedureId);
    }

    private Optional<TbDocumento> getTbDocumento(Long id, TbDocumentoList list) {
        return list.getTbDocumentos().stream().filter(doc -> Objects.equals(doc.getId(), id)).findFirst();
    }

    private void setStato(TbDocumento tbDocumento, String statoDoc) {
        TpDostaStatoDTO stato = sipadClient.dostaStatoByAcr(statoDoc);
        tbDocumento.setIdStato(stato.getId());
    }

    public SuccessOutput saveSignedDocument(SignedDocumentRequest request) {
        Long id = request.getId();
        TbDocumentoList list = getTbDocumentoList(id);
        SuccessOutput output = new SuccessOutput();
        Optional<TbDocumento> document = getTbDocumento(id, list);
        if (document.isPresent()) {
            TbDocumento tbDocumento = document.get();
            tbDocumento.setFile(Base64.getDecoder().decode(request.getFile().getBytes(StandardCharsets.UTF_8)));
            setStato(tbDocumento, DocumentStateConst.FIRMATO);
            tbDocumento.setEditabile(false);
            documentoService.save(tbDocumento);
            updateProcedureLastModified(tbDocumento.getIdProc(), null);
            output.setSuccess(true);
        } else {
            output.setSuccess(false);
        }
        return output;
    }

    public FileOutputResponseDTO convertToPdf(Long id) {
        TbDocumentoList list = getTbDocumentoList(id);
        Optional<TbDocumento> document = getTbDocumento(id, list);
        FileOutputResponseDTO fileOutput = new FileOutputResponseDTO();
        if (document.isPresent()) {
            TbDocumento tbDocumento = document.get();
            boolean isEditabile = tbDocumento.getEditabile();
            if (isEditabile) {
                try {
                    InputPreviewDocumentDTO inputPreviewDTO = new InputPreviewDocumentDTO();
                    inputPreviewDTO.setContent(new String(tbDocumento.getFile(), StandardCharsets.UTF_8));
                    inputPreviewDTO.setIdProc(tbDocumento.getIdProc());
                    String documentoPdf = getDocumentoPdf(inputPreviewDTO);
                    fileOutput.setFile(documentoPdf);
                } catch (Exception e) {
                    log.error("Error in convert file to PDF => ", e);
                }
            } else {
                fileOutput.setFile(Base64.getEncoder().encodeToString(tbDocumento.getFile()));
            }
        }
        return fileOutput;
    }

    public String getDocumentoPdf(InputPreviewDocumentDTO inputPreview) {
        TemplateGenerationDTO input = new TemplateGenerationDTO();
        input.setIsPdf(false);
        input.setForce(true);
        input.setFile(inputPreview.getContent());
        input.setModel(documentEditorService.retrievePlaceHolderForTemplate(inputPreview));
        input.setStyleCss(documentEditorService.getStringOfFileCss());
        it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.DocumentDTO documentDTO = templateApiClient.generateFromFile(input);
        String fileToString = new String(Base64.getDecoder().decode(documentDTO.getFile().getBytes()));
        String fileContent = Utils.replaceWrongTags(fileToString);
        ConvertToPdfDTO convertToPdfDTO = new ConvertToPdfDTO();
        convertToPdfDTO.setFileContent(fileContent);
        return templateApiClient.convertToPdf(convertToPdfDTO);
    }

    public SuccessOutput changeDocumentName(ChangeDocNameRequest req) {
        SuccessOutput output = new SuccessOutput();
        TbDocumento document = documentoRepository.findById(req.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(ErrorsConst.DOCUMENT_NOT_FOUND, req.getId())));
        document.setNomeFile(FileNameUtils.addPdfExtention(req.getNomeFile()));
        documentoRepository.save(document);
        updateProcedureLastModified(document.getIdProc(), null);
        output.setSuccess(true);
        return output;
    }

    public ListaModelliOutput models(String codice) {
        ListaModelliOutput listaModelliOutput = new ListaModelliOutput();
        try {
            listaModelliOutput.setModelli(documentEditorService.getTemplates(codice, null));
        } catch (Exception e) {
            log.error(">>>>>>>>>>> Error => ", e);
        }
        return listaModelliOutput;
    }

    public void updateProcedureLastModified(Long idProc, Long idDoc) {
        CustomUserDetailDTO userDetail = securityService.getUserDetails();
        if (Objects.nonNull(idDoc)) {
            DettaglioDocumentoOutput documentDetail = documentoService.dettaglio(idDoc);
            idProc = documentDetail.getIdProc();
        }
        Procedure procedure = procedureService.getProcedure(idProc);
        procedure.setLastModifiedBy(userDetail.getUsername());
        procedure.setLastModifiedDate(LocalDateTime.now());
        procedureRepository.save(procedure);
    }

    @Transactional
    public PredisposizioneDto savePredisposition(PredisposizioneInput input) {
        try {
            PredisposizioneDto output = predisposizioneService.salva(input);
            if (Objects.nonNull(output)) {
                TbDocumentoList list = getTbDocumentoList(input.getIdDocumento());
                Optional<TbDocumento> document = getTbDocumento(input.getIdDocumento(), list);
                document.ifPresent(doc -> updateProcedureLastModified(doc.getIdProc(), null));
            }
            return output;
        } catch (Exception e) {
            log.error(">>>>>>>>Errore in savePredisposition", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(ErrorsConst.ERRORE_IN_SAVE_PREDISPOSITION, e.getMessage()));
        }
    }

    @Transactional
    public SegnaturaDto saveSignature(SegnaturaInput input) {
        try {
            SegnaturaDto output = segnaturaService.insert(input);
            if (Objects.nonNull(output)) {
                TbDocumentoList list = getTbDocumentoList(input.getIdDocumento());
                Optional<TbDocumento> document = getTbDocumento(input.getIdDocumento(), list);
                document.ifPresent(doc -> updateProcedureLastModified(doc.getIdProc(), null));
            }
            return output;
        } catch (Exception e) {
            log.error(">>>>>>>>Errore in saveSignature", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(ErrorsConst.ERRORE_IN_SAVE_SIGNATURE, e.getMessage()));
        }
    }

    @Transactional(readOnly = true)
    public OutputPreviewDocumentDTO getDocumentoPdf(Long idDocumento) {
        DettaglioDocumentoOutput dettaglioDocumento = documentoService.dettaglio(idDocumento);
        FileOutput documento = documentoService.download(idDocumento);
        String content = new String(Base64.getDecoder().decode(documento.getFile().getBytes()));
        InputPreviewDocumentDTO input = new InputPreviewDocumentDTO();
        input.setIdProc(dettaglioDocumento.getIdProc());
        input.setContent(content);
        OutputPreviewDocumentDTO out = new OutputPreviewDocumentDTO();
        it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.DocumentDTO dto = documentEditorService.getDocumentoPdf(input);
        out.setContent(dto.getFile());
        return out;
    }

}
