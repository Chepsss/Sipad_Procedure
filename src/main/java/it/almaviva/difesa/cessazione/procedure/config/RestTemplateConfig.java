package it.almaviva.difesa.cessazione.procedure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestTemplateConfig {

    @Bean
    @Qualifier("restTemplate")
    RestTemplate getRestTemplate() {
        // config logs (request and response) HERE in case of needed
        return new RestTemplate();
    }

}
