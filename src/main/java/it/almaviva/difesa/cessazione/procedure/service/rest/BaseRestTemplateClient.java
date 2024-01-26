package it.almaviva.difesa.cessazione.procedure.service.rest;

import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings(value = "all")
@Slf4j
@Component
public class BaseRestTemplateClient {

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    private <T> ResponseEntity<T> callRestService(String url,
                                                  Object payload,
                                                  HttpMethod method,
                                                  HttpHeaders headers,
                                                  Class<T> responseTypeClazz,
                                                  ParameterizedTypeReference<T> responseTypeParam) {
        ResponseEntity<T> out;
        if (responseTypeParam != null)
            out = restTemplate.exchange(url, method, new HttpEntity<>(payload, headers), responseTypeParam);
        else
            out = restTemplate.exchange(url, method, new HttpEntity<>(payload, headers), responseTypeClazz);

        return out;
    }

    protected <T> T callPostService(String url,
                                    Object payload,
                                    HttpHeaders headers,
                                    Class<T> responseTypeClazz,
                                    ParameterizedTypeReference<T> responseTypeParam) {
        try {
            ResponseEntity<T> t = callRestService(url, payload, HttpMethod.POST, headers, responseTypeClazz, responseTypeParam);
            return t.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw new ServiceException(e.getLocalizedMessage(), e.getStatusCode());
        }
    }


    protected <T> T callGetService(String url,
                                   HttpHeaders headers,
                                   Class<T> responseTypeClazz,
                                   ParameterizedTypeReference<T> responseTypeParam) {
        try {
            ResponseEntity<T> t = callRestService(url, null, HttpMethod.GET, headers, responseTypeClazz, responseTypeParam);
            return t.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw new ServiceException(e.getLocalizedMessage(), e.getStatusCode());
        }
    }

}
