package it.almaviva.difesa.cessazione.procedure.service.msproc.document.impl;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.constant.DocumentStateConst;
import it.almaviva.difesa.cessazione.procedure.constant.ErrorsConst;
import it.almaviva.difesa.cessazione.procedure.constant.TipoDocumento;
import it.almaviva.difesa.cessazione.procedure.domain.msproc.Procedure;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.documenti_editor.InputPreviewDocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.DocTipoENomeDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.DocumentListDto;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.ProtocollazioneMessage;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.document.StatoPredisposizioneMessage;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.TpDostaStatoDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.VwSg155StgiurFastMiCiDTO;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.CamundaService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.ProcedureService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.DocumentService;
import it.almaviva.difesa.cessazione.procedure.service.msproc.document.ProtocollazioneService;
import it.almaviva.difesa.cessazione.procedure.service.rest.SipadClient;
import it.almaviva.difesa.documenti.document.data.sipad.repository.AllegatoDocumentoRepository;
import it.almaviva.difesa.documenti.document.data.sipad.repository.TbCeDestinatariPredRepository;
import it.almaviva.difesa.documenti.document.data.sipad.repository.TbDocumentoRepository;
import it.almaviva.difesa.documenti.document.data.sipad.specification.DocumentoSpecifications;
import it.almaviva.difesa.documenti.document.domain.msdoc.AllegatoDocumento;
import it.almaviva.difesa.documenti.document.domain.msdoc.DestinatariPred;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbAllegato;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbDocumento;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbPredisposizione;
import it.almaviva.difesa.documenti.document.domain.msdoc.TbSegnatura;
import it.almaviva.difesa.documenti.document.model.dto.request.AttachmentDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.DestinatariInterniDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.MittenteDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.RegistrationEntryDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizione.AssegnatarioDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizione.ClassificazioneDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizione.DestinatarioEsternoDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizione.DestinatarioInternoDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizione.FascicoloV5DTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizione.PredisposizioneEntryDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizione.RuoloFirmaDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizione.TipoContattoDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizione.TipologiaDocumentaleDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.predisposizione.UoMittenteDTO;
import it.almaviva.difesa.documenti.document.model.dto.request.protocollazioneIngresso.ProtocolloIngressoDto;
import it.almaviva.difesa.documenti.document.model.dto.request.protocollazioneUscita.ProtocolloUscitaDto;
import it.almaviva.difesa.documenti.document.model.dto.response.DocumentRegistration;
import it.almaviva.difesa.documenti.document.model.dto.response.DocumentRegistrationResponse;
import it.almaviva.difesa.documenti.document.model.dto.response.organigramma.TbStoraOrganigrammaAooDto;
import it.almaviva.difesa.documenti.document.model.dto.response.predisposizione.Documento;
import it.almaviva.difesa.documenti.document.model.dto.response.predisposizione.PredisposizioneDocAssociatoResponse;
import it.almaviva.difesa.documenti.document.model.dto.response.predisposizione.PredisposizioneResponse;
import it.almaviva.difesa.documenti.document.model.dto.response.predisposizione.Protocollo;
import it.almaviva.difesa.documenti.document.model.dto.response.predisposizione.StatoPredisposizione;
import it.almaviva.difesa.documenti.document.model.dto.response.registrosegnatura.TpRegistroSegnaturaDTO;
import it.almaviva.difesa.documenti.document.model.dto.response.rubrica.DettaglioRubricaDestinatarioOut;
import it.almaviva.difesa.documenti.document.model.dto.response.tipidocumenti.TpDotipTDocumentoDto;
import it.almaviva.difesa.documenti.document.service.AdhocService;
import it.almaviva.difesa.documenti.document.service.RegistriService;
import it.almaviva.difesa.documenti.document.service.TbDocumentoService;
import it.almaviva.difesa.documenti.document.service.rest.MsSipadApiClient;
import it.almaviva.difesa.documenti.document.utils.DateUtils;
import it.almaviva.difesa.documenti.document.utils.FileNameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ProtocollazioneServiceImpl implements ProtocollazioneService {

    private static final String PREDISPOSIZIONE = "PREDISPOSIZIONE";
    private static final String ELETTRONICO = "ELETTRONICO";
    private static final String INVIATA = "Inviata";
    private static final String LETTERA_DI_TRASMISSIONE = "Lettera di trasmissione";
    private static final String DECRETO = "Decreto";
    private static final String DECRETO_CESS = "Decreto Cessazione mod. A";
    private static final String OK = "OK";
    private static final String IN_ITINERE = "in itinere";
    private static final String RICHIESTA_IN_ATTESA = "La richiesta n. %d è in attesa di lavorazione.";
    private static final String PROTOCOLLATA_E_ANNULLATA = "Protocollata e annullata";
    private static final String RICHIESTA_PROTOCOLLATA_E_ANNULLATA = "La richiesta n. %d è stata protocollata e annullata in AdHoc.";
    private static final String ELIMINATA = "Eliminata";
    private static final String RICHIESTA_ELIMINATA = "La richiesta n. %d è stata eliminata in AdHoc.";
    private static final String PROTOCOLLATA = "Protocollata";
    private static final String RICHIESTA_PROTOCOLLATA = "La richiesta n. %d è stata protocollata  in AdHoc.";
    private static final String NUOVO_STATO_DOCUMENTO = "nuovoStatoDocumento";
    private static final String APPUNTO = "Nota/Appunto";
    private static final String NOTA_APPUNTO = "NOTA_APPUNTO";
    private static final String RTF_CONTENT = "e1xydGYxfQo=";
    private static final String RTF_NAME = "appunti.rtf";
    private static final String COPIA_CONOSCENZA = "cc";
    private static final String TIPO_CONTATTO_PAE = "PAE";
    private static final String TIPO_CONTATTO_PG = "PG";
    private static final String TIPO_CONTATTO_IPA = "IPA";
    private static final String TIPO_CONTATTO_PF = "PF";
    private static final String TIPO_ASSEGNAZIONE_N = "N";
    private static final String APPROVAZIONE_NON_CONCLUSA = "approvazione non conclusa";
    private static final String APPROVAZIONE_COMPLETATA = "approvazione completata";
    private static final String APPROVAZIONE_NEGATIVA = "approvazione negativa";
    private static final String SEGNATURA_MITTENTE_CESS_FORMAT = "%d %s CESS";
    private static final String ID_ORIGINE_PROTOCOLLAZIONE_FORMAT = "%s_%s";
    private static final String ID_ORIGINE_DOC_PREDISPOSTI_ASSOCIATI_FORMAT = "CE_%d";
    private static final String COD_TIPO_ALLEGATO_PRIMARIO = "D";

    @Value("${isAdhocMock:#{false}}")
    private Boolean useMock;

    @Value("${adhoc.regex.date}")
    private String date;

    private final TbDocumentoService documentoService;
    private final TbDocumentoRepository documentoRepository;
    private final TbCeDestinatariPredRepository destinatariPredRepository;
    private final AdhocService adhocService;
    private final SipadClient sipadClient;
    private final MsSipadApiClient sipadApiClient;
    private final ProcedureService procedureService;
    private final CamundaService camundaService;
    private final DocumentService documentService;
    private final SecurityService securityService;
    private final AllegatoDocumentoRepository allegatoDocumentoRepository;
    private final RegistriService registriService;

    @Transactional
    @Override
    public DocumentRegistrationResponse ingresso(ProtocolloIngressoDto input) {
        TbDocumento documento = documentoRepository.findById(input.getDocumentoId()).orElse(null);
        if (Objects.isNull(documento)) {
            throw new EntityNotFoundException();
        }
        TbSegnatura segnatura = documento.getSegnatura();
        try {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>>Protocollazione in ingresso<<<<<<");
                log.debug("Chiamata ad adhocService /protocollo/registrazioneIngresso");
            }
            checkSegnaturaCompleta(segnatura);
            String fileContent = convertToPdf(documento);
            RegistrationEntryDTO dto = prepareProtocolloInIngressoInput(documento, segnatura, fileContent);
            DocumentRegistrationResponse response = adhocService.registrazioneIngresso(dto);
            log.debug(">>>>>>>>> DocumentRegistrationResponse {}", response);

            DocumentRegistration datiProtocollazione = response.getDatiProtocollazione();
            String dataProtocolloResp = datiProtocollazione.getDataProtocollazione();
            Date dateProtocol = DateUtils.string2Date(dataProtocolloResp, Constant.DD_MM_YYYY);

            documento.setNumProtocollo(datiProtocollazione.getSignature());
            documento.setDataProtocollo(dateProtocol);

            response.setDataProtocollo(dataProtocolloResp);
            TpDostaStatoDTO stato = sipadClient.dostaStatoByAcr(DocumentStateConst.PROTOCOLLATO);
            if (Objects.nonNull(stato)) {
                documento.setIdStato(stato.getId());
            }
            documento.setFile(Base64.getDecoder().decode(fileContent.getBytes(StandardCharsets.UTF_8)));
            documento.setEditabile(false);
            documentoRepository.save(documento);
            documentService.updateProcedureLastModified(documento.getIdProc(), null);
            return response;

        } catch (Exception e) {
            log.error("Errore in protocollazione in ingresso =>", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void checkSegnaturaCompleta(TbSegnatura segnatura) {
        if (Objects.isNull(segnatura)) {
            throw new ServiceException(ErrorsConst.SEGNATURA_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        assert segnatura.getNumReg() != null;
    }

    private String convertToPdf(TbDocumento documento) {
        if (Boolean.TRUE.equals(documento.getEditabile())) {
            InputPreviewDocumentDTO inputPreviewDTO = new InputPreviewDocumentDTO();
            inputPreviewDTO.setContent(new String(documento.getFile(), StandardCharsets.UTF_8));
            inputPreviewDTO.setIdProc(documento.getIdProc());
            return documentService.getDocumentoPdf(inputPreviewDTO);
        } else {
            return Base64.getEncoder().encodeToString(documento.getFile());
        }
    }

    private RegistrationEntryDTO prepareProtocolloInIngressoInput(TbDocumento documento, TbSegnatura segnatura, String file) {
        Long userLoggedId = securityService.getEmployeeIdOfUserLogged();
        VwSg155StgiurFastMiCiDTO userLoggedDetail = sipadClient.getUserLoggedDetailByEmpId(userLoggedId);
        TbStoraOrganigrammaAooDto assegnatario = sipadApiClient.assegnatario(segnatura.getCodRuolo());

        RegistrationEntryDTO dto = new RegistrationEntryDTO();
        dto.setOggetto(documento.getOggetto());
        String registro = segnatura.getRegistro();
        dto.setTipoDocumento(risolviRegistroProtIngresso(registro));
        MittenteDTO mittente = new MittenteDTO();
        mittente.setDenominazione(segnatura.getDenomMittente());
        mittente.setEmail(segnatura.getMailMittente());
        mittente.setNome(userLoggedDetail.getSg155Nome());
        mittente.setCognome(userLoggedDetail.getSg155Cognome());
        mittente.setCf(userLoggedDetail.getSg155CodiceFiscale());
        mittente.setTipoContatto(TipoContattoDTO.PERSONA_FISICA);
        dto.setMittente(mittente);

        if (Objects.nonNull(segnatura.getDataReg())) {
            String dataReg = segnatura.getDataReg().toString();
            dataReg = DateUtils.changeDateFormat(dataReg, Constant.YYYY_MM_DD, Constant.DD_MM_YYYY_WITH_SLASH);
            dto.setSegnaturaMittente(String.format(SEGNATURA_MITTENTE_CESS_FORMAT, segnatura.getNumReg(), dataReg));
            dto.setDataProtocolloMittente(dataReg);
        }

        ClassificazioneDTO classificazione = new ClassificazioneDTO();
        FascicoloV5DTO v5 = new FascicoloV5DTO();
        v5.setIdentificativo(segnatura.classificazione());
        classificazione.setFascicoloV5(v5);
        dto.setClassificazione(classificazione);

        //set allegati primario e secondari
        List<AttachmentDTO> allegati = new ArrayList<>();
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setPrimario(true);
        attachmentDTO.setFileName(getFileName(documento));
        attachmentDTO.setContent(file);
        setAllegatiDocumento(allegati, documento);
        allegati.add(attachmentDTO);
        dto.setAllegati(allegati);

        List<DestinatariInterniDTO> destinatariInterni = new ArrayList<>();
        DestinatariInterniDTO destinatario = new DestinatariInterniDTO();
        destinatario.setIdMittDest(assegnatario.getIdUo().intValue());
        destinatario.setDenominazione(assegnatario.getCodLoginPk());
        destinatariInterni.add(destinatario);
        dto.setDestinatariInterni(destinatariInterni);

        return dto;
    }

    private String risolviRegistro(String registro) {
        TpRegistroSegnaturaDTO byType = registriService.getByType(registro);
        return byType.getRegCodAdhoc();
    }

    private String risolviRegistroProtIngresso(String registro) {
        TpRegistroSegnaturaDTO byType = registriService.getByType(registro);
        return byType.getRegCodAdhocIngresso();
    }

    private void setAllegatiDocumento(List<AttachmentDTO> allegati, TbDocumento documento) {
        List<AllegatoDocumento> listAllegati = allegatoDocumentoRepository.getListaAllegatiDocumento(documento.getId(),
                Constant.TIPO_COLLEGAMENTO_ALLEGATO_USCITA);
        if (Objects.nonNull(listAllegati) && !listAllegati.isEmpty()) {
            listAllegati.forEach(allegatoDocumento -> {
                TbAllegato allegato = allegatoDocumento.getAllegatoDocumentoId().getAllegato();
                AttachmentDTO attachmentDTO = new AttachmentDTO();
                attachmentDTO.setPrimario(false);
                attachmentDTO.setFileName(allegato.getNomeFile());
                if (Objects.nonNull(allegato.getTipoAllegato())) {
                    attachmentDTO.setTipoAllegato(allegato.getTipoAllegato());
                }
                if (allegato.getCodTipoAllegato().equalsIgnoreCase(Constant.COD_TIPO_ALLEGATO_ADHOC)) {
                    byte[] fileAdHoc = adhocService.recuperoFile(allegato.getIdFile().intValue());
                    attachmentDTO.setContent(Base64.getEncoder().encodeToString(fileAdHoc));
                } else {
                    attachmentDTO.setContent(Base64.getEncoder().encodeToString(allegato.getBlobFile()));
                }
                allegati.add(attachmentDTO);
            });
        }
    }

    @Transactional
    @Override
    public PredisposizioneResponse uscita(ProtocolloUscitaDto input) {
        TbDocumento documento = documentoRepository.findById(input.getDocumentoId()).orElse(null);
        if (Objects.isNull(documento)) {
            throw new EntityNotFoundException();
        }
        TbPredisposizione predisposizione = documento.getPredisposizione();
        PredisposizioneResponse response = new PredisposizioneResponse();
        try {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>>Protocollazione in uscita<<<<<<");
                log.debug("Chiamata ad adhocService /predisposizione");
            }
            checkPresenzaDecretoProtocollato(documento);
            PredisposizioneEntryDTO entry = prepareProtocolloInUscita(documento, predisposizione);
            response = adhocService.predisposizione(entry);
        } catch (Exception e) {
            log.error("Errore in protocollazione in uscita => \n", e);
        }
        if (Boolean.FALSE.equals(response.getHasError())) {
            documento.setIdRichProtocollo(String.valueOf(response.getIdPredSalvata()));
            TpDostaStatoDTO stato = sipadClient.dostaStatoByAcr(DocumentStateConst.PREDISPOSTO_ADHOC);
            if (Objects.nonNull(stato)) {
                documento.setIdStato(stato.getId());
            }
            if (Boolean.TRUE.equals(documento.getEditabile())) {
                String fileContent = convertToPdf(documento);
                documento.setFile(Base64.getDecoder().decode(fileContent.getBytes(StandardCharsets.UTF_8)));
            }
            documento.setStatoRich(INVIATA);
            documento.setEditabile(false);
            documentoRepository.save(documento);
            documentService.updateProcedureLastModified(documento.getIdProc(), null);
        } else {
            throw new ServiceException(ErrorsConst.PROTOCOLLAZIONE_USCITA_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private void checkPresenzaDecretoProtocollato(TbDocumento documento) {
        TpDostaStatoDTO statoDTO = sipadClient.dostaStatoByAcr(DocumentStateConst.PROTOCOLLATO);
        String acrTip = documento.getIdTipo();
        if (TipoDocumento.LETTERA_TRASMISSIONE.equalsIgnoreCase(acrTip)) {
            Long idProc = documento.getIdProc();
            Long idStato = statoDTO.getId();
            List<TbDocumento> decretoProtocollato = documentoRepository.findAll(DocumentoSpecifications
                    .findByTipoAndStato(idProc, TipoDocumento.DECRETO, idStato));
            if (decretoProtocollato.size() != 1) {
                throw new ServiceException(ErrorsConst.PROTOCOLLAZIONE_NOT_POSSIBLE, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void checkPredisposizioneCompilata(TbDocumento documento) {
        String acrTip = documento.getIdTipo();
        if (!TipoDocumento.DECRETO.equalsIgnoreCase(acrTip) || !TipoDocumento.APPUNTO.equalsIgnoreCase(acrTip)) {
            TbPredisposizione predisposizioneLettera = documento.getPredisposizione();
            if (predisposizioneLetteraNonCompilata(predisposizioneLettera)) {
                throw new ServiceException(ErrorsConst.PREDISPOSIZIONE_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private PredisposizioneEntryDTO prepareProtocolloInUscita(TbDocumento documento, TbPredisposizione predisposizione) {
        PredisposizioneEntryDTO entry = new PredisposizioneEntryDTO();
        entry.setIdOrigine(String.format(ID_ORIGINE_PROTOCOLLAZIONE_FORMAT, documento.getId(), new Date().getTime()));
        //set allegato primario e secondari
        List<AttachmentDTO> allegati = prepareAllegati(documento);
        entry.setAllegati(allegati);
        TipologiaDocumentaleDTO tipologia = new TipologiaDocumentaleDTO();
        tipologia.setTipologia(PREDISPOSIZIONE);
        tipologia.setTipoDocumentoWF(ELETTRONICO);
        entry.setTipologiaDocumentale(tipologia);
        entry.setIsP7MSign(false);
        entry.setHasIndirizziFile(true);
        if (Objects.nonNull(predisposizione)) {
            entry.setOggetto(predisposizione.getOggetto());
            entry.setAssegnatario(new AssegnatarioDTO(predisposizione.getAssegnatario()));

            ClassificazioneDTO classificazione = new ClassificazioneDTO();
            FascicoloV5DTO v5 = new FascicoloV5DTO();
            v5.setIdentificativo(predisposizione.classificazione());
            classificazione.setFascicoloV5(v5);
            entry.setClassificazione(classificazione);
        }
        UoMittenteDTO uoMittente = new UoMittenteDTO();
        if (Objects.nonNull(predisposizione)) {
            uoMittente.setIdElemento(predisposizione.getUoMittente());
            uoMittente.setLabel(predisposizione.getAoo());
        }
        entry.setUoMittente(uoMittente);
        if (Objects.nonNull(predisposizione)) {
            entry.setDestinatariEsterni(entryDestinatariEsterni(predisposizione));
            entry.setDestinatariInterni(entryDestinatariInterni(predisposizione));
        }
        entry.setAccessori(new ArrayList<>());
        entry.setListaNote(new ArrayList<>());
        return entry;
    }

    private List<AttachmentDTO> prepareAllegati(TbDocumento documento) {
        List<AttachmentDTO> attachments = new ArrayList<>();
        TpDostaStatoDTO statoDTO = sipadClient.dostaStatoByAcr(DocumentStateConst.PROTOCOLLATO);
        String cod = documento.getIdTipo();
        Long idStato = statoDTO.getId();
        if (TipoDocumento.LETTERA_TRASMISSIONE.equalsIgnoreCase(cod)) {
            List<TbDocumento> decretoProtocollato = documentoRepository
                    .findAll(DocumentoSpecifications.findByTipoAndStato(documento.getIdProc(), TipoDocumento.DECRETO, idStato));
            TbDocumento decreto = decretoProtocollato.get(0);
            addToAttachments(attachments, decreto, false, null);
            setAllegatiDocumento(attachments, decreto);
        }
        addToAttachments(attachments, documento, true, null);
        setAllegatiDocumento(attachments, documento);
        return attachments;
    }

    private void addToAttachments(List<AttachmentDTO> attachments, TbDocumento documento, Boolean primario, Long l) {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        String file = convertToPdf(documento);
        attachmentDTO.setContent(file);
        attachmentDTO.setFileName(getFileName(documento));
        attachmentDTO.setPrimario(primario);
        if (Objects.nonNull(l)) {
            attachmentDTO.setOrdinamento(l);
        }
        attachments.add(attachmentDTO);
    }

    private List<DestinatarioEsternoDTO> entryDestinatariEsterni(TbPredisposizione predisposizione) {
        List<DestinatarioEsternoDTO> destinatari = new ArrayList<>();
        List<DestinatariPred> destinatariPreds = destinatariPredRepository.findAllByPredisposizione_Id(predisposizione.getId());
        for (DestinatariPred destinatario : destinatariPreds) {
            if (Boolean.TRUE.equals(destinatario.getFlagExt())) {

                DettaglioRubricaDestinatarioOut dettaglioDestinatarioOut = sipadApiClient.dettaglioRubricaEsterno(destinatario.getIdDestinatarioEsterno());

                DestinatarioEsternoDTO dto = new DestinatarioEsternoDTO();
                dto.setDenominazione(destinatario.getNome());
                dto.setInvioPerConoscenza(destinatario.getInvioACc().equalsIgnoreCase(COPIA_CONOSCENZA));
                dto.setSendWithPec(destinatario.getFlagUsaPec());
                dto.setEmail(destinatario.getMail());
                dto.setCitta(destinatario.getIndirCitta());
                dto.setEmailCertificata(destinatario.getPec());
                dto.setIndirizzo(destinatario.getIndirVia());
                campiPerTipoContattoPF(dto, dettaglioDestinatarioOut);
                campiPerTipoContattoIPA(dto, dettaglioDestinatarioOut);
                campiPerTipoContattoPG(dto, dettaglioDestinatarioOut);
                campiPerTipoContattoPAE(dto, dettaglioDestinatarioOut);
                destinatari.add(dto);
            }
        }
        return destinatari;
    }

    private void campiPerTipoContattoPAE(DestinatarioEsternoDTO dto, DettaglioRubricaDestinatarioOut destinatario) {
        String tipoContatto = destinatario.getTipoContatto();
        if (tipoContatto.equalsIgnoreCase(TIPO_CONTATTO_PAE)) {
            dto.setTipoContatto(new TipoContattoDTO(TIPO_CONTATTO_PAE));
            dto.setDenominazioneUfficio(destinatario.getDenominazioneUfficio());
        }
    }

    private void campiPerTipoContattoPG(DestinatarioEsternoDTO dto, DettaglioRubricaDestinatarioOut destinatario) {
        String tipoContatto = destinatario.getTipoContatto();
        if (tipoContatto.equalsIgnoreCase(TIPO_CONTATTO_PG)) {
            dto.setTipoContatto(new TipoContattoDTO(TIPO_CONTATTO_PG));
            dto.setPartitaIva(destinatario.getPartitaIva());
        }
    }

    private void campiPerTipoContattoIPA(DestinatarioEsternoDTO dto, DettaglioRubricaDestinatarioOut destinatario) {
        String tipoContatto = destinatario.getTipoContatto();
        if (tipoContatto.equalsIgnoreCase(TIPO_CONTATTO_IPA)) {
            dto.setTipoContatto(new TipoContattoDTO(TIPO_CONTATTO_IPA));
            dto.setDenominazioneAoo(destinatario.getDenominazioneAoo());
            dto.setCodiceIpa(destinatario.getCodiceIpa());
            dto.setCodiceUnivocoAoo(destinatario.getCodiceUnivocoAoo());
            dto.setCodiceAoo(destinatario.getCodiceAoo());
        }
    }

    private void campiPerTipoContattoPF(DestinatarioEsternoDTO dto, DettaglioRubricaDestinatarioOut destinatario) {
        String tipoContatto = destinatario.getTipoContatto();
        if (tipoContatto.equalsIgnoreCase(TIPO_CONTATTO_PF)) {
            dto.setTipoContatto(new TipoContattoDTO(TIPO_CONTATTO_PF));
            dto.setTitolo(destinatario.getTitolo());
            dto.setNome(destinatario.getNome());
            dto.setCognome(destinatario.getCognome());
            dto.setCf(destinatario.getCf());
        }
    }

    private List<DestinatarioInternoDTO> entryDestinatariInterni(TbPredisposizione predisposizione) {
        List<DestinatarioInternoDTO> destinatari = new ArrayList<>();
        List<DestinatariPred> destinatariPreds = destinatariPredRepository.findAllByPredisposizione_Id(predisposizione.getId());
        for (DestinatariPred destinatario : destinatariPreds) {
            if (Boolean.FALSE.equals(destinatario.getFlagExt())) {
                DestinatarioInternoDTO dto = new DestinatarioInternoDTO();
                dto.setIdMittDest(Long.parseLong(destinatario.getIntLoginAssegnatario()));
                dto.setDenominazione(destinatario.getIntUnitaOrg());
                dto.setInvioPerConoscenza(destinatario.getTipoAssegnazione().equalsIgnoreCase(TIPO_ASSEGNAZIONE_N));
                destinatari.add(dto);
            }
        }
        return destinatari;
    }

    @Override
    public DocumentListDto checkUscita(ProtocolloUscitaDto input) {
        TbDocumento documento = documentoRepository.findById(input.getDocumentoId()).orElse(null);
        if (Objects.isNull(documento)) {
            throw new EntityNotFoundException();
        }
        DocumentListDto outDto = new DocumentListDto();
        try {
            checkPresenzaDecretoProtocollato(documento);
            checkPredisposizioneCompilata(documento);
            outDto.setMessage(OK);
            DocTipoENomeDto docTipoENomeDto = new DocTipoENomeDto();
            TpDotipTDocumentoDto dotip = sipadApiClient.getDotipDocumentoByCode(documento.getIdTipo());
            docTipoENomeDto.setNome(documento.getNomeFile());
            docTipoENomeDto.setTipo(dotip.getDescrTip());
            outDto.setFiles(Collections.singletonList(docTipoENomeDto));
            List<AttachmentDTO> allegatiDTO = prepareAllegati(documento);
            List<DocTipoENomeDto> allegati = new ArrayList<>();
            for (AttachmentDTO allegato : allegatiDTO) {
                if (!allegato.getPrimario()) {
                    DocTipoENomeDto dto = new DocTipoENomeDto();
                    dto.setNome(allegato.getFileName());
                    dto.setTipo(Objects.nonNull(allegato.getTipoAllegato()) ? allegato.getTipoAllegato() : DECRETO_CESS);
                    allegati.add(dto);
                }
            }
            outDto.setAllegati(allegati);
        } catch (Exception e) {
            log.error(">>>>>> Error in checkUscita => ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return outDto;
    }

    @Override
    @Transactional
    public StatoPredisposizioneMessage stato(Long idDocumento) {
        TbDocumento documento = documentoRepository.findById(idDocumento).orElse(null);
        if (Objects.isNull(documento)) {
            throw new EntityNotFoundException();
        }
        Long idPredisposizione = idPredDocOrNotaAppunti(documento);
        String result = "";
        StatoPredisposizione statoPredisposizione;
        HttpStatus esito = HttpStatus.OK;
        try {
            if (log.isDebugEnabled()) {
                log.debug(">>>>>>Stato protocollo<<<<<<");
                log.debug("Chiamata ad adhocService (statoPredisposizione) /predisposizione/{id}");
            }
            statoPredisposizione = adhocService.statoPredisposizione(idPredisposizione);

            if (Boolean.TRUE.equals(statoPredisposizione.getHasError())) {
                result = String.format(RICHIESTA_IN_ATTESA, idPredisposizione);
            }
            if (Objects.isNull(statoPredisposizione.getStato()) || statoPredisposizione.getStato().equalsIgnoreCase(PROTOCOLLATA)) {
                result = gestioneProtocollata(documento, idPredisposizione, statoPredisposizione);
            } else if (statoPredisposizione.getStato().equalsIgnoreCase(IN_ITINERE)) {
                result = String.format(RICHIESTA_IN_ATTESA, idPredisposizione);
            } else if (statoPredisposizione.getStato().equalsIgnoreCase(ELIMINATA)) {
                result = gestioneRichiestaEliminata(documento, idPredisposizione, statoPredisposizione);
                esito = HttpStatus.BAD_REQUEST;
            } else if (statoPredisposizione.getStato().equalsIgnoreCase(PROTOCOLLATA_E_ANNULLATA)) {
                result = gestioneProtocollataEAnnullata(documento, idPredisposizione, statoPredisposizione);
            }
        } catch (Exception e) {
            log.error("Errore in stato protocollo =>", e);
            result = String.format(RICHIESTA_IN_ATTESA, idPredisposizione);
            esito = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new StatoPredisposizioneMessage(result, esito);
    }

    // restituisce idPredisposizione da usare: puo essere quello della nota appunto in caso quarta via
    private Long idPredDocOrNotaAppunti(TbDocumento documento) {
        Long idPredisposizione = null;
        if (Objects.nonNull(documento.getSubStato()) && documento.getSubStato().equalsIgnoreCase(Constant.CICLO_ADHOC_4_VIA)) {
            log.debug("Ricerca id predisposizione della nota appunto");
            TbDocumento nota = findByTipoAndStato(documento.getIdProc(), TipoDocumento.APPUNTO, documento.getIdStato());
            assert nota != null;
            idPredisposizione = Long.parseLong(nota.getIdRichProtocollo());
        } else {
            if (Objects.nonNull(documento.getIdRichProtocollo())) {
                idPredisposizione = Long.parseLong(documento.getIdRichProtocollo());
            }
        }
        log.debug("idPredisposizione: " + idPredisposizione);
        return idPredisposizione;
    }

    private String gestioneRichiestaEliminata(TbDocumento documento, Long idPredisposizione, StatoPredisposizione statoPredisposizione) {
        salvaStatoEsitoEliminatoOAnnullato(documento, statoPredisposizione);
        return String.format(RICHIESTA_ELIMINATA, idPredisposizione);
    }

    private String gestioneProtocollataEAnnullata(TbDocumento documento, Long idPredisposizione, StatoPredisposizione statoPredisposizione) {
        salvaStatoEsitoEliminatoOAnnullato(documento, statoPredisposizione);
        return String.format(RICHIESTA_PROTOCOLLATA_E_ANNULLATA, idPredisposizione);
    }

    private void salvaStatoEsitoEliminatoOAnnullato(TbDocumento documento, StatoPredisposizione statoPredisposizione) {
        TpDostaStatoDTO stato;
        Long idProc = documento.getIdProc();
        Procedure procedure = procedureService.getProcedure(idProc);
        boolean isQuartaVia = casoRichEliminataQuartaVia(documento, statoPredisposizione);
        if (!isQuartaVia) {
            String codeState = procedure.getStateProcedure().getCodeState();
            TpDostaStatoDTO statoDTO = sipadClient.dostaStatoById(documento.getIdStato());
            String acrSta = statoDTO.getAcrSta();
            Map<String, String> decision = camundaService.getDocumentAdhocNegativeOutcomeDecision(
                    statoPredisposizione.getStato(),
                    codeState,
                    documento.getIdTipo(),
                    acrSta);
            stato = sipadClient.dostaStatoByAcr(decision.get(NUOVO_STATO_DOCUMENTO));
            documento.setIdStato(stato.getId());
            documento.setStatoRich(statoPredisposizione.getStato());
            documentoRepository.save(documento);
            documentService.updateProcedureLastModified(documento.getIdProc(), null);
        }
    }

    private String gestioneProtocollata(TbDocumento documento, Long idPredisposizione, StatoPredisposizione statoPredisposizione) {
        Protocollo protocollo = statoPredisposizione.getProtocollo().get(0);
        if (Objects.nonNull(documento.getSubStato()) && documento.getSubStato().equalsIgnoreCase(Constant.CICLO_ADHOC_4_VIA)) {
            List<TbDocumento> list = trovaDecretoLetteraTrasmissione(documento, documento.getIdProc());
            TbDocumento decreto = list.get(0);
            TbDocumento letteraTrasmissione = list.get(1);
            TbDocumento notaAppunto = list.get(2);
            if (Boolean.TRUE.equals(useMock)) {
                updateDocument(decreto, protocollo);
                updateDocument(letteraTrasmissione, protocollo);
                return String.format(RICHIESTA_PROTOCOLLATA, idPredisposizione);
            }
            if (Objects.isNull(decreto)) {
                log.error("gestioneProtocollata(): decreto is null");
                throw new ServiceException(ErrorsConst.GESTIONE_PROTOCOLLATA_ERROR_DECRETO_NULL, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String esitoQuartaVia = checkEsitoQuartaVia(statoPredisposizione, decreto, letteraTrasmissione, notaAppunto);
            if (esitoQuartaVia.equalsIgnoreCase(APPROVAZIONE_NON_CONCLUSA)) {
                return String.format(RICHIESTA_IN_ATTESA, idPredisposizione);
            } else if (esitoQuartaVia.equalsIgnoreCase(APPROVAZIONE_NEGATIVA)) {
                return String.format(RICHIESTA_ELIMINATA, idPredisposizione);
            }
        } else {
            updateDocument(documento, protocollo);
        }
        return String.format(RICHIESTA_PROTOCOLLATA, idPredisposizione);
    }

    private List<TbDocumento> trovaDecretoLetteraTrasmissione(TbDocumento documento, long idProc) {
        List<TbDocumento> documentList = new LinkedList<>();
        if (TipoDocumento.DECRETO.equals(documento.getIdTipo())) {
            documentList.add(documento);
            TbDocumento lettera = findByTipoAndStato(idProc, TipoDocumento.LETTERA_TRASMISSIONE, documento.getIdStato());
            documentList.add(lettera);
            TbDocumento nota = findByTipoAndStato(idProc, TipoDocumento.APPUNTO, documento.getIdStato());
            documentList.add(nota);
        } else if (TipoDocumento.LETTERA_TRASMISSIONE.equals(documento.getIdTipo())) {
            TbDocumento decreto = findByTipoAndStato(idProc, TipoDocumento.DECRETO, documento.getIdStato());
            TbDocumento nota = findByTipoAndStato(idProc, TipoDocumento.APPUNTO, documento.getIdStato());
            documentList.add(decreto);
            documentList.add(documento);
            documentList.add(nota);
        }
        return documentList;
    }

    private TbDocumento findByTipoAndStato(Long idProc, String tipoDocumento, Long idStato) {
        Specification<TbDocumento> trovaDocSpecification = DocumentoSpecifications.findByTipoAndStato(idProc, tipoDocumento, idStato);
        List<TbDocumento> docs = documentoRepository.findAll(trovaDocSpecification);
        if (!docs.isEmpty()) {
            return docs.get(0);
        }
        log.debug("Documento per idProcedura " + idProc + " tipo: " + tipoDocumento + " non trovato");
        return null;
    }

    private void updateDocument(TbDocumento documento, Protocollo protocollo) {
        documento.setStatoRich(PROTOCOLLATA);
        TpDostaStatoDTO stato = sipadClient.dostaStatoByAcr(DocumentStateConst.PROTOCOLLATO);
        documento.setIdStato(stato.getId());
        documento.setNumProtocollo(String.valueOf(protocollo.getSegnatura()));
        if (Boolean.TRUE.equals(useMock)) {
            documento.setDataProtocollo(new Date());
        } else {
            String dataProtocollo = documentService.getProtocolData(protocollo.getSegnatura(), date);
            try {
                Date dateProtocol = new SimpleDateFormat(Constant.DD_MM_YYYY).parse(dataProtocollo);
                documento.setDataProtocollo(dateProtocol);
            } catch (Exception e) {
                log.error(">>> Segnatura '{}' not contains date at end for value \n >>> Error MSG: {}", protocollo.getSegnatura(), e.getMessage());
            }
            updateFile(documento, protocollo);
        }
        documentoRepository.save(documento);
        documentService.updateProcedureLastModified(documento.getIdProc(), null);
    }

    private void updateFile(TbDocumento documento, Protocollo protocollo) {
        List<Documento> documenti = protocollo.getDocumenti();
        try {
            Documento docPrimario = documenti.stream()
                    .filter(it -> it.getTipoAllegato().equalsIgnoreCase(COD_TIPO_ALLEGATO_PRIMARIO))
                    .findFirst()
                    .orElseThrow();
            if (documento.getIdTipo().equalsIgnoreCase(TipoDocumento.APPUNTO)) {
                documento.setNomeFile(docPrimario.getNomeFile());
            }
            int objectId = docPrimario.getObjectID().intValue();
            if (log.isDebugEnabled()) {
                log.debug(">>>>>>Recupero file<<<<<<");
                log.debug("Chiamata ad adhocService /protocollo/documento/{idFile}");
            }
            byte[] file = adhocService.recuperoFile(objectId);
            documento.setFile(file);
        } catch (Exception e) {
            log.error("Errore in updateFile =>", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Quarta via
     */
    @Override
    public ProtocollazioneMessage previewProtocollo(ProtocolloUscitaDto input) {
        TbDocumento lettera = documentoRepository.findById(input.getDocumentoId()).orElse(null);
        if (Objects.isNull(lettera)) {
            throw new EntityNotFoundException();
        }
        return checkProtocolloConsentito(lettera);
    }

    private ProtocollazioneMessage checkProtocolloConsentito(TbDocumento lettera) {
        ProtocollazioneMessage protocollazioneMessage = new ProtocollazioneMessage();
        ArrayList<String> errori = new ArrayList<>();
        HashSet<String> docErrati = new HashSet<>();
        List<DocTipoENomeDto> listaDoc = new ArrayList<>();
        // Verifica stato lettera
        checkStatoLettera(lettera, docErrati);
        TpDostaStatoDTO statoDTO = sipadClient.dostaStatoByAcr(DocumentStateConst.IN_APPROVAZIONE);
        Long idStato = statoDTO.getId();
        Long idProc = lettera.getIdProc();
        Specification<TbDocumento> byTipoAndStato = DocumentoSpecifications.findByTipoAndStato(idProc, TipoDocumento.DECRETO, idStato);
        List<TbDocumento> decreti = documentoRepository.findAll(byTipoAndStato);
        TbDocumento decreto;
        if (decreti.isEmpty()) {
            docErrati.add(DECRETO);
        } else {
            decreto = decreti.get(0);
            if (decreto != null && (TipoDocumento.DECRETO.equalsIgnoreCase(decreto.getIdTipo()))) {
                addToListDoc(listaDoc, decreto, DECRETO_CESS);
                protocollazioneMessage.setFiles(listaDoc);
                // Verifica segnatura decreto
                checkSegnaturaDecreto(decreto, errori);
            }
        }
        addToListDoc(listaDoc, lettera, LETTERA_DI_TRASMISSIONE);
        protocollazioneMessage.setFiles(listaDoc);
        // Verifica predisposizione lettera
        checkPredisposizioneLettera(lettera, errori);
        if (!docErrati.isEmpty()) {
            String docErratiList = String.join(", ", docErrati);
            errori.add(String.format(ErrorsConst.DOCUMENTS_NOT_FOUND_OR_INVALID_LIST, docErratiList));
        }
        protocollazioneMessage.setMessages(errori);
        return protocollazioneMessage;
    }

    private void checkStatoLettera(TbDocumento lettera, HashSet<String> docErrati) {
        String idTipoLettera = lettera.getIdTipo();
        TpDostaStatoDTO statoLettera = sipadClient.dostaStatoById(lettera.getIdStato());
        if (!TipoDocumento.LETTERA_TRASMISSIONE.equalsIgnoreCase(idTipoLettera)
                || !DocumentStateConst.IN_APPROVAZIONE.equals(statoLettera.getAcrSta())) {
            docErrati.add(LETTERA_DI_TRASMISSIONE);
        }
    }

    private void addToListDoc(List<DocTipoENomeDto> listaDoc, TbDocumento documento, String tipoDocumento) {
        DocTipoENomeDto dto = new DocTipoENomeDto();
        dto.setNome(documento.getNomeFile());
        dto.setTipo(tipoDocumento);
        listaDoc.add(dto);
    }

    private void checkSegnaturaDecreto(TbDocumento decreto, ArrayList<String> errori) {
        TbSegnatura segnaturaDecreto = decreto.getSegnatura();
        if (Objects.isNull(segnaturaDecreto) || Objects.isNull(segnaturaDecreto.classificazione())) {
            errori.add(ErrorsConst.SEGNATURA_ERROR);
        }
    }

    private void checkPredisposizioneLettera(TbDocumento lettera, ArrayList<String> errori) {
        TbPredisposizione predisposizioneLettera = lettera.getPredisposizione();
        if (predisposizioneLetteraNonCompilata(predisposizioneLettera)) {
            errori.add(ErrorsConst.PREDISPOSIZIONE_ERROR);
        }
    }

    private boolean predisposizioneLetteraNonCompilata(TbPredisposizione predisposizioneLettera) {
        return (Objects.isNull(predisposizioneLettera)
                || Objects.isNull(predisposizioneLettera.getUoMittente())
                || Objects.isNull(predisposizioneLettera.getAssegnatario())
                || Objects.isNull(predisposizioneLettera.classificazione()));
    }

    @Override
    @Transactional
    public PredisposizioneResponse protocolla(ProtocolloUscitaDto input) {
        TbDocumento lettera = documentoRepository.findById(input.getDocumentoId()).orElse(null);
        if (Objects.isNull(lettera)) {
            throw new EntityNotFoundException();
        }
        try {
            TbDocumento decreto = checkStatoDocumenti(lettera);
            String letteraContent = convertToPdf(lettera);
            String decretoContent = convertToPdf(decreto);
            lettera.setFile(Base64.getDecoder().decode(letteraContent.getBytes(StandardCharsets.UTF_8)));
            lettera.setEditabile(false);
            documentoService.save(lettera);
            decreto.setFile(Base64.getDecoder().decode(decretoContent.getBytes(StandardCharsets.UTF_8)));
            decreto.setEditabile(false);
            documentoService.save(decreto);
            PredisposizioneEntryDTO entry = prepareProtocolloQuartaVia(decreto, lettera);
            if (log.isDebugEnabled()) {
                log.debug(">>>>>>Protocollazione quarta via<<<<<<");
                log.debug("Chiamata ad adhocService /predisposizione");
            }
            PredisposizioneResponse response = adhocService.predisposizione(entry);
            if (Boolean.FALSE.equals(response.getHasError())) {
                createNotaAppunto(response, lettera.getIdProc());
                updateDocumentInApprovazioneAdhoc(response, lettera);
                updateDocumentInApprovazioneAdhoc(response, decreto);
            } else {
                throw new ServiceException(ErrorsConst.PROTOCOLLAZIONE_ADHOC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            documentService.updateProcedureLastModified(decreto.getIdProc(), null);
            return response;
        } catch (Exception e) {
            log.error("Errore in protocollazione quarta via =>", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void createNotaAppunto(PredisposizioneResponse response, Long idProc) {
        TpDostaStatoDTO stato = sipadClient.dostaStatoByAcr(DocumentStateConst.IN_APPROVAZIONE_ADHOC);
        TbDocumento appunto = new TbDocumento();
        appunto.setIdProc(idProc);
        appunto.setIdTipo(TipoDocumento.APPUNTO);
        appunto.setNomeFile(RTF_NAME);
        appunto.setFile(Base64.getDecoder().decode(RTF_CONTENT.getBytes(StandardCharsets.UTF_8)));
        appunto.setEditabile(false);
        appunto.setSubStato(Constant.CICLO_ADHOC_4_VIA);
        appunto.setStatoRich(INVIATA);
        appunto.setIdStato(stato.getId());
        appunto.setDataEmiss(new Date());
        appunto.setIdRichProtocollo(String.valueOf(response.getIdPredSalvata()));
        documentoRepository.save(appunto);
        log.debug("Quarta via: salvato nuovo TbDocumento per nota appunti");
    }

    private TbDocumento checkStatoDocumenti(TbDocumento lettera) {
        HashSet<String> docErrati = new HashSet<>();
        TbDocumento decreto = null;
        // Verifica stato lettera
        checkStatoLettera(lettera, docErrati);
        TpDostaStatoDTO statoDTO = sipadClient.dostaStatoByAcr(DocumentStateConst.IN_APPROVAZIONE);
        Specification<TbDocumento> byTipoAndStato = DocumentoSpecifications
                .findByTipoAndStato(lettera.getIdProc(), TipoDocumento.DECRETO, statoDTO.getId());
        List<TbDocumento> decreti = documentoRepository.findAll(byTipoAndStato);
        if (decreti.isEmpty()) {
            docErrati.add(DECRETO);
        } else {
            decreto = decreti.get(0);
            // Verifica segnatura decreto
            checkSegnaturaDecreto(decreto, docErrati);
        }
        // Verifica predisposizione lettera
        checkPredisposizioneLettera(lettera, docErrati);
        if (!docErrati.isEmpty()) {
            String docErratiList = String.join(", ", docErrati);
            throw new ServiceException(String.format(ErrorsConst.DOCUMENTS_NOT_FOUND_OR_INVALID_LIST, docErratiList), HttpStatus.BAD_REQUEST);
        }
        return decreto;
    }

    private void updateDocumentInApprovazioneAdhoc(PredisposizioneResponse response, TbDocumento documento) {
        for (PredisposizioneDocAssociatoResponse each : response.getDocPredispostiAssociati()) {
            String idOrigine = each.getIdOrigine();
            String[] split = idOrigine.split("_");
            long id = Long.parseLong(split[1]);
            if (id == documento.getId()) {
                documento.setIdRichProtocollo(String.valueOf(each.getIdPredLettera()));
            }
        }
        changeDocStateToInApprovazioneAdhoc(response, documento);
    }

    private void changeDocStateToInApprovazioneAdhoc(PredisposizioneResponse response, TbDocumento documento) {
        TpDostaStatoDTO stato = sipadClient.dostaStatoByAcr(DocumentStateConst.IN_APPROVAZIONE_ADHOC);
        if (documento.getIdTipo().equals(TipoDocumento.LETTERA_TRASMISSIONE)) {
            documento.setIdRichProtocollo(String.valueOf(response.getIdPredSalvata()));
        }
        documento.setIdStato(stato.getId());
        documento.setSubStato(Constant.CICLO_ADHOC_4_VIA);
        documento.setStatoRich(INVIATA);
        documentoService.save(documento);
    }

    private void checkSegnaturaDecreto(TbDocumento decreto, HashSet<String> docErrati) {
        TbSegnatura segnaturaDecreto = decreto.getSegnatura();
        if (Objects.isNull(segnaturaDecreto) || Objects.isNull(segnaturaDecreto.classificazione())) {
            docErrati.add(DECRETO);
        }
    }

    private void checkPredisposizioneLettera(TbDocumento lettera, HashSet<String> docErrati) {
        TbPredisposizione predisposizioneLettera = lettera.getPredisposizione();
        if (predisposizioneLetteraNonCompilata(predisposizioneLettera)) {
            docErrati.add(LETTERA_DI_TRASMISSIONE);
        }
    }

    private boolean casoRichEliminataQuartaVia(TbDocumento documento, StatoPredisposizione statoPredisposizione) {
        Long idProc = documento.getIdProc();
        List<TbDocumento> list = trovaDecretoLetteraTrasmissione(documento, idProc);
        if (list.size() != 2) {
            return false;
        }
        TbDocumento decreto = list.get(0);
        TbDocumento letteraTrasmissione = list.get(1);
        if (Objects.isNull(decreto) || Objects.isNull(letteraTrasmissione)) {
            return false;
        }
        if (Objects.isNull(decreto.getIdRichProtocollo()) || Objects.isNull(letteraTrasmissione.getIdRichProtocollo())) {
            return false;
        }
        if (!decreto.getIdRichProtocollo().equals(letteraTrasmissione.getIdRichProtocollo())) {
            return false;
        }
        TpDostaStatoDTO stato = sipadClient.dostaStatoByAcr(DocumentStateConst.IN_APPROVAZIONE_ADHOC);
        decreto.setIdStato(stato.getId());
        decreto.setSubStato(null);
        decreto.setStatoRich(statoPredisposizione.getStato());
        letteraTrasmissione.setIdStato(stato.getId());
        letteraTrasmissione.setSubStato(null);
        letteraTrasmissione.setStatoRich(statoPredisposizione.getStato());
        documentoRepository.save(decreto);
        documentoRepository.save(letteraTrasmissione);
        documentService.updateProcedureLastModified(idProc, null);
        return true;
    }

    private String checkEsitoQuartaVia(StatoPredisposizione statoPredisposizione, TbDocumento decreto, TbDocumento letteraTrasmissione, TbDocumento appunto) {
        String esito = null;
        Protocollo protocolloDecreto = null;
        Protocollo protocolloLettera = null;
        String idRichProtocolloDecreto = decreto.getIdRichProtocollo();
        String idRichProtocolloLettera = letteraTrasmissione.getIdRichProtocollo();
        try {
            if (Objects.nonNull(statoPredisposizione.getDocPredispostiAssociati())) {
                for (StatoPredisposizione docPredispostoAssociato : statoPredisposizione.getDocPredispostiAssociati()) {
                    if (log.isDebugEnabled()) {
                        log.debug(">>>>>>Protocollazione quarta via<<<<<<");
                        log.debug("Chiamata ad adhocService (statoPredisposizione) /predisposizione/{id}");
                    }
                    Long idPredisposizione = docPredispostoAssociato.getIdPredisposizione();
                    StatoPredisposizione statoDocPredispostoAssociato = adhocService.statoPredisposizione(idPredisposizione);
                    if (Objects.nonNull(statoDocPredispostoAssociato.getProtocollo())) {
                        Protocollo protocollo = statoDocPredispostoAssociato.getProtocollo().get(0);
                        if (idRichProtocolloDecreto.equals(String.valueOf(idPredisposizione))) {
                            protocolloDecreto = protocollo;
                        }
                        if (idRichProtocolloLettera.equals(String.valueOf(idPredisposizione))) {
                            protocolloLettera = protocollo;
                        }
                    } else {
                        if (statoDocPredispostoAssociato.getStato().equalsIgnoreCase(ELIMINATA)
                                || statoDocPredispostoAssociato.getStato().equalsIgnoreCase(PROTOCOLLATA_E_ANNULLATA)) {
                            esito = APPROVAZIONE_NEGATIVA;
                        } else {
                            esito = APPROVAZIONE_NON_CONCLUSA;
                        }
                    }
                }
            }
            if (Objects.nonNull(protocolloDecreto) && Objects.nonNull(protocolloLettera)) {
                esito = APPROVAZIONE_COMPLETATA;
            } else {
                log.error("protocollo decreto e protocollo lettera are null");
            }
            if (APPROVAZIONE_COMPLETATA.equals(esito)) {
                if (Objects.nonNull(protocolloDecreto.getNumeroProtocollo())) {
                    decreto.setNumProtocollo(protocolloDecreto.getNumeroProtocollo().toString());
                }
                updateDocument(decreto, protocolloDecreto);
                letteraTrasmissione.setNumProtocollo(protocolloLettera.getNumeroProtocollo().toString());
                updateDocument(letteraTrasmissione, protocolloLettera);
                updateDocument(appunto, statoPredisposizione.getProtocollo().get(0));
            }
        } catch (Exception e) {
            log.error("Errore in checkEsitoQuartaVia =>", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        if (Objects.isNull(esito)) {
            esito = APPROVAZIONE_NEGATIVA;
            log.error("Esito quarta via negativa");
        }
        return esito;
    }

    private PredisposizioneEntryDTO prepareProtocolloQuartaVia(TbDocumento decreto, TbDocumento lettera) {
        PredisposizioneEntryDTO entry = new PredisposizioneEntryDTO();
        entry.setListaNote(new ArrayList<>());
        entry.setIdOrigine(String.format(ID_ORIGINE_PROTOCOLLAZIONE_FORMAT, lettera.getId(), new Date().getTime()));
        List<AttachmentDTO> allegati = prepareAllegatoAppunto();
        entry.setAllegati(allegati);
        entry.setOggetto(APPUNTO);

        TipologiaDocumentaleDTO tipologiaDocumentale = new TipologiaDocumentaleDTO();
        tipologiaDocumentale.setTipologia(NOTA_APPUNTO);
        tipologiaDocumentale.setTipoDocumentoWF(NOTA_APPUNTO);
        entry.setTipologiaDocumentale(tipologiaDocumentale);

        AssegnatarioDTO assegnatario = new AssegnatarioDTO();
        assegnatario.setLogin(lettera.getPredisposizione().getAssegnatario());
        entry.setAssegnatario(assegnatario);
        entry.setIsP7MSign(false);

        ClassificazioneDTO classificazione = new ClassificazioneDTO();
        FascicoloV5DTO v5 = new FascicoloV5DTO();
        v5.setIdentificativo(lettera.getPredisposizione().classificazione());
        classificazione.setFascicoloV5(v5);
        entry.setClassificazione(classificazione);
        entry.setHasOrderedDocuments(true);

        UoMittenteDTO uoMittente = new UoMittenteDTO();
        uoMittente.setLabel(lettera.getPredisposizione().getAoo());
        uoMittente.setIdElemento(lettera.getPredisposizione().getUoMittente());
        entry.setUoMittente(uoMittente);

        RuoloFirmaDTO ruoloFirma = new RuoloFirmaDTO();
        ruoloFirma.setLogin(decreto.getSegnatura().getCodRuolo());
        entry.setRuoloFirma(ruoloFirma);
        entry.setHasDatiSensibili(false);
        entry.setHasAllegatiAnalogici(false);
        entry.setDestinatariInterni(null);
        entry.setAccessori(new ArrayList<>());
        entry.setDestinatariEsterni(new ArrayList<>());
        entry.setInCoordinamento(false);
        entry.setIsLettera(false);

        TbPredisposizione predisposizione = lettera.getPredisposizione();
        if (Objects.nonNull(predisposizione)) {
            entry.setDestinatariEsterni(entryDestinatariEsterni(predisposizione));
            entry.setDestinatariInterni(entryDestinatariInterni(predisposizione));
        }

        List<PredisposizioneEntryDTO> docAssociati = new ArrayList<>();
        addToDocAssociati(docAssociati, decreto, assegnatario, uoMittente, ruoloFirma, true);
        addToDocAssociati(docAssociati, lettera, assegnatario, uoMittente, ruoloFirma, false);
        entry.setDocPredispostiAssociati(docAssociati);
        return entry;
    }

    private List<AttachmentDTO> prepareAllegatoAppunto() {
        List<AttachmentDTO> attachments = new ArrayList<>();
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setContent(RTF_CONTENT); // file rtf vuoto  = {rtf1} in base64
        attachmentDTO.setIsNoPdf(true);
        attachmentDTO.setFileName(RTF_NAME);
        attachmentDTO.setPrimario(true);
        attachmentDTO.setOrdinamento(0L);
        attachments.add(attachmentDTO);
        return attachments;
    }

    private void addToDocAssociati(List<PredisposizioneEntryDTO> docAssociati,
                                   TbDocumento documento,
                                   AssegnatarioDTO assegnatario,
                                   UoMittenteDTO uoMittente,
                                   RuoloFirmaDTO ruoloFirma,
                                   boolean isDecreto) {
        PredisposizioneEntryDTO entry = new PredisposizioneEntryDTO();
        entry.setListaNote(new ArrayList<>());
        if (documento.getIdTipo().equalsIgnoreCase(TipoDocumento.LETTERA_TRASMISSIONE)) {
            entry.setOggetto(documento.getPredisposizione().getOggetto());
        } else {
            entry.setOggetto(documento.getOggetto());
        }
        entry.setIdOrigine(String.format(ID_ORIGINE_DOC_PREDISPOSTI_ASSOCIATI_FORMAT, documento.getId()));
        List<AttachmentDTO> attachments = new ArrayList<>();
        addToAttachments(attachments, documento, true, 0L);
        //set degli allegati dei documenti come allegati secondati
        setAllegatiDocumento(attachments, documento);
        entry.setAllegati(attachments);

        TipologiaDocumentaleDTO tipologiaDocumentale = new TipologiaDocumentaleDTO();

        if (isDecreto) {
            String registroIngresso = risolviRegistroProtIngresso(Constant.REGISTRO_DECRETI);
            tipologiaDocumentale.setTipologia(registroIngresso);
            tipologiaDocumentale.setTipoDocumentoWF(registroIngresso);
        } else {
            String registro = risolviRegistro(Constant.REGISTRO_GENERALE);
            tipologiaDocumentale.setTipologia(PREDISPOSIZIONE);
            tipologiaDocumentale.setTipoDocumentoWF(registro);
        }
        entry.setTipologiaDocumentale(tipologiaDocumentale);
        entry.setAssegnatario(assegnatario);
        entry.setIsP7MSign(false);

        ClassificazioneDTO classificazione = new ClassificazioneDTO();
        FascicoloV5DTO v5 = new FascicoloV5DTO();
        if (isDecreto) {
            v5.setIdentificativo(documento.getSegnatura().classificazione());
        } else {
            v5.setIdentificativo(documento.getPredisposizione().classificazione());
        }
        classificazione.setFascicoloV5(v5);
        entry.setClassificazione(classificazione);
        entry.setHasOrderedDocuments(true);
        entry.setUoMittente(uoMittente);
        entry.setRuoloFirma(ruoloFirma);
        entry.setHasDatiSensibili(false);
        entry.setHasAllegatiAnalogici(false);
        entry.setDestinatariInterni(null);
        if (Objects.nonNull(documento.getPredisposizione())) {
            entry.setDestinatariEsterni(entryDestinatariEsterni(documento.getPredisposizione()));
        } else {
            entry.setDestinatariEsterni(new ArrayList<>());
        }
        entry.setAccessori(new ArrayList<>());
        entry.setIsLettera(true);
        docAssociati.add(entry);
    }

    public static String getFileName(TbDocumento documento) {
        String nomeFile = documento.getNomeFile();
        return FileNameUtils.addPdfExtention(nomeFile);
    }

}
