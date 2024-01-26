package it.almaviva.difesa.cessazione.procedure.service.rest;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.ConvertToPdfDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates.TemplateCriteria;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates.TemplateGenerationDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.request.templates.TemplateTypeCriteriaDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.DocumentDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.MatchPlaceholdersDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.TemplateFilterResponseSearchDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.TemplateResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.msproc.response.templates.TemplateTypeDTO;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@Service
public class TemplateApiClient {

    private static final String PREFIX_BEARER = "Bearer %s";
    private static final String GET_TEMPLATE_TYPES = "%s/template-type/filter";
    private static final String GENERATE_TEMPLATE = "%s/template/generate";
    private static final String GENERATE_FROM_FILE = "%s/template/generateFromFile";
    private static final String SEARCH_TEMPLATES_BY_FILTER = "%s/template/filter?page=0&size=%d";
    private static final String GET_TEMPLATE_BY_ID = "%s/template/%d";
    private static final String CONVERT_TO_PDF = "%s/template/convertToPdf";
    private static final String GET_TEMPLATE_BY_NAME = "%s/template/getByName";
    private static final String CHECK_PLACEHOLDERS = "%s/template/check-if-placeholders-match/%d";

    @Autowired
    @Qualifier("webClient")
    WebClient.Builder webClientBuilder;

    @Value("${application.template-api.baseurl}")
    String templateApiBaseUrl;

    @Autowired
    SecurityService securityService;

    public List<TemplateTypeDTO> getTemplateTypes(TemplateTypeCriteriaDTO templateTypeCriteriaDTO) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(String.format(GET_TEMPLATE_TYPES, templateApiBaseUrl))
                    .bodyValue(templateTypeCriteriaDTO)
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<TemplateTypeDTO>>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            log.error("ERROR in getTemplateTypes", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public DocumentDTO generateTemplate(TemplateGenerationDTO input) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(String.format(GENERATE_TEMPLATE, templateApiBaseUrl))
                    .bodyValue(input)
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<DocumentDTO>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            log.error("ERROR in generateTemplate", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public DocumentDTO generateFromFile(TemplateGenerationDTO input) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(String.format(GENERATE_FROM_FILE, templateApiBaseUrl))
                    .bodyValue(input)
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<DocumentDTO>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            log.error("ERROR in generateFromFile", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public Page<TemplateFilterResponseSearchDTO> searchTemplatesByFilter(TemplateCriteria input) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(String.format(SEARCH_TEMPLATES_BY_FILTER, templateApiBaseUrl, Integer.MAX_VALUE))
                    .bodyValue(input)
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<RestPageImpl<TemplateFilterResponseSearchDTO>>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            log.error("ERROR in searchTemplatesByFilter => ", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            log.error("ERROR in searchTemplatesByFilter => ", e);
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TemplateResponseDTO getTemplateById(Long templateId) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .get()
                    .uri(String.format(GET_TEMPLATE_BY_ID, templateApiBaseUrl, templateId))
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<TemplateResponseDTO>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            log.error("ERROR in getTemplateById => ", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public String convertToPdf(ConvertToPdfDTO convertToPdfDTO) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(String.format(CONVERT_TO_PDF, templateApiBaseUrl))
                    .bodyValue(convertToPdfDTO)
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<String>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            log.error("ERROR in convertToPdf", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public TemplateResponseDTO getTemplateByName(TemplateCriteria input) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(String.format(GET_TEMPLATE_BY_NAME, templateApiBaseUrl))
                    .bodyValue(input)
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<TemplateResponseDTO>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            log.error("ERROR in getTemplateByName => ", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public MatchPlaceholdersDTO checkPlaceholdersMatch(String htmlSource, Long templateId) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(String.format(CHECK_PLACEHOLDERS, templateApiBaseUrl, templateId))
                    .bodyValue(htmlSource)
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<MatchPlaceholdersDTO>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            log.error("ERROR in checkPlaceholdersMatch => ", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

}
