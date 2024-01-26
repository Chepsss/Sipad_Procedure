package it.almaviva.difesa.cessazione.procedure.service.rest;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.enums.StatusEnum;
import it.almaviva.difesa.cessazione.procedure.model.common.GenericResponse;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.StgceCessazioneReqDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.*;
import it.almaviva.difesa.cessazione.procedure.model.dto.sipad.request.VwDo007ProcedimentiCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class SipadClient extends BaseRestTemplateClient {

    private static final String LIST_PRATT_T_ATTIVAZIONE = "%s/pratAttivazione/list/%s";
    private static final String PRATT_ATTIVAZIONE_BY_ID = "%s/pratAttivazione/%d";
    private static final String TIPI_PROCEDIMENTO = "%s/tipoProcedimento/%s/%s";
    private static final String TIPO_PROCEDIMENTO_BY_ID = "%s/tipoProcedimento/%d";
    private static final String TIPO_PROCEDIMENTO_IDS = "%s/tipoProcedimento/listIds/%s/%s/%s";
    private static final String DOCAT_C_DOCUMENTO_LIST = "%s/tipodocumento/docat/list";
    private static final String DOCAT_C_DOCUMENTO_BY_ID = "%s/tipodocumento/docat/%d";
    private static final String DOSTA_STATO_BY_ID = "%s/dostastato/%d";
    private static final String DOSTA_STATO_BY_ACR = "%s/dostastato/acr/%s";
    private static final String STNAZ_NAZIONE_LIST = "%s/stnazNazione/list";
    private static final String STNAZ_NAZIONE_BY_ID = "%s/stnazNazione/%d";
    private static final String STREG_REGIONE_LIST = "%s/stregRegione/list";
    private static final String STPRO_PROVINCIA_LIST = "%s/stproProvincia/list";
    private static final String STPRO_PROVINCIA_BY_ID = "%s/stproProvincia/%d";
    private static final String STCOM_COMUNE_LIST = "%s/stcomComune/list";
    private static final String STCOM_COMUNE_LIST_BY_PROVINCE = "%s/stcomComune/list/%d";
    private static final String STCOM_COMUNE_BY_ID = "%s/stcomComune/%d";
    private static final String STENT_ENTE_LIST_BY_DENOM = "%s/stentEnte/list/%s";
//    //cr16
    private static final String RUBRICA_COMPLETA_LIST_BY_DENOM_CC = "%s/rubrica/listRubrica/%s";

    private static final String ENTE_BY_ID_ENTE = "%s/rubrica/listRubrica/ente/%s";
//    //fine cr16
    private static final String STENT_ENTE_BY_ID = "%s/stentEnte/%s";
    private static final String STFAA_FORZA_ARMATA_LIST = "%s/stfaaForzaArmata/list";
    private static final String STFAA_FORZA_ARMATA_BY_ID = "%s/stfaaForzaArmata/%s";
    private static final String STAFF_CATEGORIES = "%s/sgctpCatpersonale/staffCategories";
    private static final String CAT_MILITARE_BY_ID = "%s/sgctpCatpersonale/%d";
    private static final String EMPLOYEE_CATEGORIES = "%s/sgtpoPosizioneStato/categories";
    private static final String CATEGORIES_OF_LEAVE = "%s/sgtpoPosizioneStato/categoriesOfLeave";
    private static final String CATEGORY_OF_LEAVE_BY_ID = "%s/sgtpoPosizioneStato/%s";
    private static final String CATEGORIES_OF_LEAVE_BY_ACR = "%s/sgtpoPosizioneStato/categoriesByAcr?acrPosizione={acrPosizione}";
    private static final String CATEGORIES_OF_LEAVE_NON_ADMISSIBILITY = "%s/sgtpoPosizioneStato/categoriesOfLeaveNonAdmissibility";
    private static final String LIMITE_CSZ_ETA = "%s/age-limits/limit";
    private static final String GET_ALL_PROCEDURES_ARCHIVE = "%s/proceeding/filter?size=%d&page=%d";
    private static final String GET_EMPLOYEE_BY_ID = "%s/user/findById?employeeId={employeeId}";
    private static final String STGIURIDICODS_DTO = "%s/sg122StGiuridico/%d";
    private static final String TEMPLATE_ANAGR_DTO = "%s/do001TemplateAnagr/%d";
    private static final String INSERT_STGCE_CESSAZIONE = "%s/stgceCessazione/insert";
    private static final String CALL_STORED_PROCEDURE_CESSAZIONE = "%s/stgceCessazione/callStoredProcedureCessazione";
    private static final String GET_STGCE_CESSAZIONE = "%s/stgceCessazione/stgceCessazioneByCFAndNumDecreto";
    private static final String SG001_STGIURIDICO = "%s/sg001StGiuridico/{idDip}";
    private static final String GET_USER_LOGGED_DETAIL_BY_EMP_ID = "%s/userFast/findById?employeeId={employeeId}";
    private static final String SORT = "&sort=%s";

    @Value("${ms-sipad-api.baseurl}")
    private String baseUrl;

    public List<TpPrattAttivazioneDTO> listPratTAttivazione() {
        return callGetService(String.format(LIST_PRATT_T_ATTIVAZIONE, baseUrl, Constant.ACR_PROC), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public TpPrattAttivazioneDTO prattAttivazioneById(Long id) {
        return callGetService(String.format(PRATT_ATTIVAZIONE_BY_ID, baseUrl, id), null, TpPrattAttivazioneDTO.class, null);
    }

    public List<TpPrtpoTprocedimentoDTO> tipiProcedimento(String type) {
        return callGetService(String.format(TIPI_PROCEDIMENTO, baseUrl, type, Constant.ACR_PROC), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public TpPrtpoTprocedimentoDTO tipoProcedimentoById(Long id) {
        return callGetService(String.format(TIPO_PROCEDIMENTO_BY_ID, baseUrl, id), null, TpPrtpoTprocedimentoDTO.class, null);
    }

    public List<Long> tipoProcedimentoIds(String type, String acrProc, String prtpoAcrProc) {
        String url = String.format(TIPO_PROCEDIMENTO_IDS, baseUrl, type, acrProc, prtpoAcrProc);
        return callGetService(url, null, null, new ParameterizedTypeReference<>() {
        });
    }

    public List<TpDocatCdocumentoDTO> docatCDocumentoList() {
        return callGetService(String.format(DOCAT_C_DOCUMENTO_LIST, baseUrl), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public TpDocatCdocumentoDTO docatCDocumentoById(Long id) {
        return callGetService(String.format(DOCAT_C_DOCUMENTO_BY_ID, baseUrl, id), null, TpDocatCdocumentoDTO.class, null);
    }

    public TpDostaStatoDTO dostaStatoById(Long id) {
        return callGetService(String.format(DOSTA_STATO_BY_ID, baseUrl, id), null, TpDostaStatoDTO.class, null);
    }

    public TpDostaStatoDTO dostaStatoByAcr(String acr) {
        String service = String.format(DOSTA_STATO_BY_ACR, baseUrl, acr);
        log.info("CALLING SERVICE " + service);
        try {
            return callGetService(service, null, TpDostaStatoDTO.class, null);
        } catch (Exception e){
            log.error("Errore nella chiamata al servizio " + service, e);
            throw e;
        }
    }

    public List<TpStnazNazioneDTO> stnazNazioneDTOList() {
        return callGetService(String.format(STNAZ_NAZIONE_LIST, baseUrl), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public TpStnazNazioneDTO stnazNazioneById(long id) {
        return callGetService(String.format(STNAZ_NAZIONE_BY_ID, baseUrl, id), null, TpStnazNazioneDTO.class, null);
    }

    public List<TpStregRegioneDTO> stregRegioneDTOList() {
        return callGetService(String.format(STREG_REGIONE_LIST, baseUrl), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public List<TpStproProvinciaDTO> stproProvinciaDTOList() {
        return callGetService(String.format(STPRO_PROVINCIA_LIST, baseUrl), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public TpStproProvinciaDTO stproProvinciaById(long id) {
        return callGetService(String.format(STPRO_PROVINCIA_BY_ID, baseUrl, id), null, TpStproProvinciaDTO.class, null);
    }

    public List<TpStComComuneDTO> stComComuneDTOList() {
        return callGetService(String.format(STCOM_COMUNE_LIST, baseUrl), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public TpStComComuneDTO stcomComuneById(long id) {
        return callGetService(String.format(STCOM_COMUNE_BY_ID, baseUrl, id), null, TpStComComuneDTO.class, null);
    }

    public List<TpStComComuneDTO> stComComuneListByProvince(long id) {
        return callGetService(String.format(STCOM_COMUNE_LIST_BY_PROVINCE, baseUrl, id), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public List<TbStentEnteDTO> stentEnteListByDenom(String denomEnte) {
        return callGetService(String.format(STENT_ENTE_LIST_BY_DENOM, baseUrl, denomEnte), null, null, new ParameterizedTypeReference<>() {
        });


    }
    //cr16
    public List<RubricaCompletaDTO> getRubricaCompletaDenomEnteCC(String denomEnteCC) {
        return callGetService(String.format(RUBRICA_COMPLETA_LIST_BY_DENOM_CC, baseUrl, denomEnteCC), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public RubricaCompletaDTO getEnteByIdEnte(String idEnte) {
        return callGetService(String.format(ENTE_BY_ID_ENTE, baseUrl, idEnte), null, null, new ParameterizedTypeReference<>() {
        });
    }
    //fine cr16

    public TbStentEnteDTO stentEnteById(String id) {
        return callGetService(String.format(STENT_ENTE_BY_ID, baseUrl, id), null, TbStentEnteDTO.class, null);
    }

    public List<TpStfaaForzaArmataDTO> stfaaForzaArmataDTOList() {
        return callGetService(String.format(STFAA_FORZA_ARMATA_LIST, baseUrl), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public TpStfaaForzaArmataDTO stfaaForzaArmataById(String id) {
        return callGetService(String.format(STFAA_FORZA_ARMATA_BY_ID, baseUrl, id), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public StaffCategoryDTO staffCategories() {
        return callGetService(String.format(STAFF_CATEGORIES, baseUrl), null, StaffCategoryDTO.class, null);
    }

    public TpSgctpCatpersonaleDTO catMilitareById(long id) {
        return callGetService(String.format(CAT_MILITARE_BY_ID, baseUrl, id), null, TpSgctpCatpersonaleDTO.class, null);
    }

    public EmployeeCategoryDTO employeeCategories() {
        return callGetService(String.format(EMPLOYEE_CATEGORIES, baseUrl), null, EmployeeCategoryDTO.class, null);
    }

    public List<TpSgtpoPosizioneStatoDTO> categoriesOfLeave(CategoriesOfLeaveRequestDTO categoriesOfLeaveRequestDTO) {
        return callPostService(String.format(CATEGORIES_OF_LEAVE, baseUrl), categoriesOfLeaveRequestDTO, null, null, new ParameterizedTypeReference<>() {
        });
    }

    public TpSgtpoPosizioneStatoDTO categoryOfLeaveById(String id) {
        return callGetService(String.format(CATEGORY_OF_LEAVE_BY_ID, baseUrl, id), null, TpSgtpoPosizioneStatoDTO.class, null);
    }

    public List<TpSgtpoPosizioneStatoDTO> categoriesOfLeaveByAcr(String acrPosizione) {
        UriTemplate uriTemplate = new UriTemplate(String.format(CATEGORIES_OF_LEAVE_BY_ACR, baseUrl));
        URI uri = uriTemplate.expand(acrPosizione);
        return callGetService(uri.toString(), null, null, new ParameterizedTypeReference<>() {
        });
    }

    public List<TpSgtpoPosizioneStatoDTO> categoriesOfLeaveNonAdmissibility(CategoriesOfLeaveRequestDTO categoriesOfLeaveRequestDTO) {
        return callPostService(String.format(CATEGORIES_OF_LEAVE_NON_ADMISSIBILITY, baseUrl), categoriesOfLeaveRequestDTO, null, null, new ParameterizedTypeReference<>() {
        });
    }

    public Integer limiteCszEta(LimiteEtaRequestDTO limiteEtaRequestDTO) {
        return callPostService(String.format(LIMITE_CSZ_ETA, baseUrl), limiteEtaRequestDTO, null, null, new ParameterizedTypeReference<>() {
        });
    }

    public Page<VwDo007ProcedimentiDTO> getAllProceduresArchiveByFiscalCodeAndState(String codiceFiscale, boolean isClosed, Pageable pageable) {
        String sort = getSortToString(pageable);
        String url = String.format(GET_ALL_PROCEDURES_ARCHIVE, baseUrl, pageable.getPageSize(), pageable.getPageNumber());
        if (Objects.nonNull(sort)) {
            url += String.format(SORT, sort);
        }
        VwDo007ProcedimentiCriteria request = new VwDo007ProcedimentiCriteria();
        request.setDo007CodFisc(codiceFiscale);
        request.setIsClosed(isClosed);
        return callPostService(url, request, null, null, new ParameterizedTypeReference<RestPageImpl<VwDo007ProcedimentiDTO>>() {
        });
    }

    public VwSg155StgiuridicoDTO getEmployeeById(Long id) {
        UriTemplate uriTemplate = new UriTemplate(String.format(GET_EMPLOYEE_BY_ID, baseUrl));
        URI uri = uriTemplate.expand(id);
        return callGetService(uri.toString(), null, VwSg155StgiuridicoDTO.class, null);
    }

    public VwSg155StgiurFastMiCiDTO getUserLoggedDetailByEmpId(Long employeeId) {
        UriTemplate uriTemplate = new UriTemplate(String.format(GET_USER_LOGGED_DETAIL_BY_EMP_ID, baseUrl));
        URI uri = uriTemplate.expand(employeeId);
        return callGetService(uri.toString(), null, VwSg155StgiurFastMiCiDTO.class, null);
    }

    public VwSg122StgiuridicoDsDTO stgiuridicoDsDTO(long id) {
        return callGetService(String.format(STGIURIDICODS_DTO, baseUrl, id), null, VwSg122StgiuridicoDsDTO.class, null);
    }

    public VwDo001TemplateAnagrDTO templateAnagrDTO(long id) {
        return callGetService(String.format(TEMPLATE_ANAGR_DTO, baseUrl, id), null, VwDo001TemplateAnagrDTO.class, null);
    }

    public TsStgceCessazioneDTO insertStgceCessazione(TsStgceCessazioneDTO stgceCessazioneRequestDTO) {
        return callPostService(String.format(INSERT_STGCE_CESSAZIONE, baseUrl), stgceCessazioneRequestDTO, null, null, new ParameterizedTypeReference<>() {
        });
    }

    public GenericResponse callStoredProcedureCessazione(StgceCessazioneReqDTO stgceCessazioneReqDTO) {
        return callPostService(String.format(CALL_STORED_PROCEDURE_CESSAZIONE, baseUrl), stgceCessazioneReqDTO, null, null, new ParameterizedTypeReference<>() {
        });
    }

    public TsStgceCessazioneDTO getStgceCessazioneByCodFiscaleAndNumDecreto(StgceCessazioneReqDTO stgceCessazioneReqDTO) {
        return callPostService(String.format(GET_STGCE_CESSAZIONE, baseUrl), stgceCessazioneReqDTO, null, null, new ParameterizedTypeReference<>() {
        });
    }

    public VwSg001StgiuridicoMilDTO sg001StgiuridicoMilDTO(Long idDip) {
        UriTemplate uriTemplate = new UriTemplate(String.format(SG001_STGIURIDICO, baseUrl));
        URI uri = uriTemplate.expand(idDip);
        return callGetService(uri.toString(), null, VwSg001StgiuridicoMilDTO.class, null);
    }

    private String getSortToString(Pageable pageable) {
        Sort sorts = pageable.getSort();
        Optional<Sort.Order> order = sorts.stream().findFirst();
        if (order.isPresent()) {
            Sort.Direction direction = order.get().getDirection();
            StatusEnum.ProcArchiveEnumField field = StatusEnum.ProcArchiveEnumField.fromLabelDto(order.get().getProperty());
            switch (Objects.requireNonNull(field)) {
                case COD_FISC:
                    return StatusEnum.ProcArchiveEnumField.COD_FISC.getEntityColumn() + "," + direction.name();
                case PRPRO_ID:
                    return StatusEnum.ProcArchiveEnumField.PRPRO_ID.getEntityColumn() + "," + direction.name();
                case PRPRO_COD_PRO:
                    return StatusEnum.ProcArchiveEnumField.PRPRO_COD_PRO.getEntityColumn() + "," + direction.name();
                case DATA_AVVIO:
                    return StatusEnum.ProcArchiveEnumField.DATA_AVVIO.getEntityColumn() + "," + direction.name();
                case DATA_FINE:
                    return StatusEnum.ProcArchiveEnumField.DATA_FINE.getEntityColumn() + "," + direction.name();
                case FASE:
                    return StatusEnum.ProcArchiveEnumField.FASE.getEntityColumn() + "," + direction.name();
                case STATO:
                    return StatusEnum.ProcArchiveEnumField.STATO.getEntityColumn() + "," + direction.name();
                case TIPO_PROC:
                    return StatusEnum.ProcArchiveEnumField.TIPO_PROC.getEntityColumn() + "," + direction.name();
                case AUTORE:
                    return StatusEnum.ProcArchiveEnumField.AUTORE.getEntityColumn() + "," + direction.name();
                default:
                    return null;
            }
        }
        return null;
    }

}
