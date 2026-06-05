package desafio.encurtador_url.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import desafio.encurtador_url.config.UrlShortenerProperties;
import desafio.encurtador_url.service.ShortUrlGenerator;
import desafio.encurtador_url.service.ShortUrlPersistenceService;
import desafio.encurtador_url.service.UrlShorteningService;
import desafio.encurtador_url.support.InMemoryShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class GlobalExceptionHandlerTest {

    @Test
    void shouldMapValidationErrorsToBadRequest() throws Exception {
        MockMvc mockMvc = mockMvc(properties());

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"ftp://example.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Requisicao invalida"))
                .andExpect(jsonPath("$.message").value("Existem campos invalidos na requisicao"))
                .andExpect(jsonPath("$.path").value("/api/v1/urls"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("url"))
                .andExpect(jsonPath("$.fieldErrors[0].message")
                        .value("A URL original deve ser valida e usar http ou https"));
    }

    @Test
    void shouldMapMissingShortCodeToNotFound() throws Exception {
        MockMvc mockMvc = mockMvc(properties());

        mockMvc.perform(get("/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("URL nao encontrada"))
                .andExpect(jsonPath("$.message").value("URL encurtada nao encontrada para o codigo: missing"))
                .andExpect(jsonPath("$.path").value("/missing"))
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    @Test
    void shouldMapShortCodeGenerationFailureToInternalServerError() throws Exception {
        UrlShortenerProperties properties = properties();
        properties.setMaxGenerationAttempts(0);
        MockMvc mockMvc = mockMvc(properties);

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://example.com\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Erro ao gerar codigo curto"))
                .andExpect(jsonPath("$.message").value("Nao foi possivel gerar um codigo curto unico"))
                .andExpect(jsonPath("$.path").value("/api/v1/urls"))
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    private MockMvc mockMvc(UrlShortenerProperties properties) {
        ShortUrlGenerator generator = new ShortUrlGenerator(properties);
        ShortUrlPersistenceService persistenceService = new ShortUrlPersistenceService(new InMemoryShortUrlRepository());
        ShortUrlController controller = new ShortUrlController(
                new UrlShorteningService(generator, persistenceService, properties)
        );

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        return MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    private UrlShortenerProperties properties() {
        UrlShortenerProperties properties = new UrlShortenerProperties();
        properties.setBaseUrl("http://localhost:8080");
        properties.setShortCodeLength(8);
        properties.setMaxGenerationAttempts(10);
        return properties;
    }
}
