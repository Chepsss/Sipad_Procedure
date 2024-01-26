package it.almaviva.difesa.cessazione.procedure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@TestConfiguration
@ActiveProfiles("test")
class ProcedureApplicationTests {

	@Bean
	@Qualifier("webClient")
	WebClient.Builder getWebClient() {
		return WebClient.builder();
	}

	@Test
	void contextLoads() {
	}

}
