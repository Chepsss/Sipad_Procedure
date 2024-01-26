package it.almaviva.difesa.cessazione.procedure.service.rest;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import it.almaviva.difesa.cessazione.procedure.model.CustomUserDetail;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.EmployeeResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.EmployeeSearchDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.RoleDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.UserDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.UserDetailResponseDTO;
import it.almaviva.difesa.cessazione.procedure.model.dto.rest.UserSearchDTO;
import it.almaviva.difesa.cessazione.procedure.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
public class AuthServiceClient {

    private static final String PREFIX_BEARER = "Bearer %s";
    private static final String GET_USER_DETAILS = "%s/auth/getUserDetails";
    private static final String GET_ROLES = "%s/role/all";
    private static final String GET_EMPLOYEE_BY_ID = "%s/employee/%d";
    private static final String SEARCH_EMPLOYEE_IDS = "%s/employee/searchEmployeeIds";
    private static final String SEARCH_USER_BY_ROLE_IDS = "%s/user/searchUserRoleIds";
    private static final String GET_USER_BY_EMPLOYEE_ID = "%s/user/userDetailByEmployeeId/%d";
    private static final String LIST_BY_ROLES = "%s/user/listByRoles";

    @Autowired
    @Qualifier("serviceWebClient")
    WebClient.Builder webClientBuilder;

    @Value("${application.auth-service.baseurl}")
    String authServiceBaseUrl;

    @Autowired
    SecurityService securityService;

    public synchronized CustomUserDetail getUserDetails(String token) {
        try {
            return webClientBuilder
                    .build()
                    .get()
                    .uri(String.format(GET_USER_DETAILS, authServiceBaseUrl))
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(CustomUserDetail.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public synchronized List<RoleDTO> getRoles() {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .get()
                    .uri(String.format(GET_ROLES, authServiceBaseUrl))
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<RoleDTO>>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public synchronized EmployeeResponseDTO getEmployeeById(Long id, String token) {
        try {
            return webClientBuilder
                    .build()
                    .get()
                    .uri(String.format(GET_EMPLOYEE_BY_ID, authServiceBaseUrl, id))
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(EmployeeResponseDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public synchronized List<Long> searchEmployee(EmployeeSearchDTO searchDTO) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(String.format(SEARCH_EMPLOYEE_IDS, authServiceBaseUrl))
                    .bodyValue(searchDTO)
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Long>>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public Long searchUserByRoleCode(UserSearchDTO searchDTO) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(String.format(SEARCH_USER_BY_ROLE_IDS, authServiceBaseUrl))
                    .bodyValue(searchDTO)
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public synchronized UserDetailResponseDTO getUserByEmployeeId(Long id, String token) {
        try {
            return webClientBuilder
                    .build()
                    .get()
                    .uri(String.format(GET_USER_BY_EMPLOYEE_ID, authServiceBaseUrl, id))
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(UserDetailResponseDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public synchronized List<UserDTO> listByRoles(List<Long> roleIds) {
        String token = securityService.getUserToken();
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(String.format(LIST_BY_ROLES, authServiceBaseUrl))
                    .bodyValue(roleIds)
                    .headers(httpHeaders -> httpHeaders.add(Constant.AUTH_HEADER, String.format(PREFIX_BEARER, token)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

}
