package it.almaviva.difesa.cessazione.procedure.service;

import it.almaviva.difesa.cessazione.procedure.ProcedureApplication;
import it.almaviva.difesa.cessazione.procedure.service.rest.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {AuthServiceClient.class, ProcedureApplication.class})
@ActiveProfiles("test")
public class AuthServiceClientRestIntegrationTest {

    @Autowired
    AuthServiceClient authServiceClient;

}
