package it.almaviva.difesa.cessazione.procedure.service.rest;

import it.almaviva.difesa.cessazione.procedure.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class BaseRestClient {

    @Autowired
    @Qualifier("webClient")
    WebClient.Builder webClientBuilder;

    public <T> T getCall(String url, Class<T> targetType) {
        try {
            return webClientBuilder
                    .build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(targetType)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("\n>>>>>>>>>> ERROR in RestClient -> getCall", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public <T> T getCall(String url, ParameterizedTypeReference<T> targetType) {
        try {
            return webClientBuilder
                    .build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(targetType)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("\n>>>>>>>>>> ERROR in RestClient -> getCall", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public <T> T postCall(String url, Object body, Class<T> targetType) {
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(url)
                    .bodyValue(body)
                    .headers(httpHeaders ->
                            httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                    .retrieve()
                    .bodyToMono(targetType)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("\n>>>>>>>>>> ERROR in RestClient -> postCall", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public <T> T postCall(String url, Object body, ParameterizedTypeReference<T> targetType) {
        try {
            return webClientBuilder
                    .build()
                    .post()
                    .uri(url)
                    .bodyValue(body)
                    .headers(httpHeaders -> httpHeaders.add("Content-Type", "application/json"))
                    .retrieve()
                    .bodyToMono(targetType)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("\n>>>>>>>>>> ERROR in RestClient -> postCall", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    public <T> T deleteCall(String url, Class<T> targetType) {
        try {
            return webClientBuilder
                    .build()
                    .delete()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(targetType)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("\n>>>>>>>>>> ERROR in RestClient -> deleteCall", e);
            throw new ServiceException(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

}
