package it.almaviva.difesa.cessazione.procedure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI () {
        return new OpenAPI()
                .info(new Info().title("Procedure-service API doc").version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("auth-token", new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(Constant.AUTH_HEADER)
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("auth-token"));
    }
}
