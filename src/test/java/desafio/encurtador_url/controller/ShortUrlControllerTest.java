package desafio.encurtador_url.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import desafio.encurtador_url.config.UrlShortenerProperties;
import desafio.encurtador_url.dto.ShortenUrlRequest;
import desafio.encurtador_url.dto.ShortenUrlResponse;
import desafio.encurtador_url.entity.ShortUrl;
import desafio.encurtador_url.exception.ShortUrlNotFoundException;
import desafio.encurtador_url.service.ShortUrlGenerator;
import desafio.encurtador_url.service.ShortUrlPersistenceService;
import desafio.encurtador_url.service.UrlShorteningService;
import desafio.encurtador_url.support.InMemoryShortUrlRepository;
import java.net.URI;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ShortUrlControllerTest {

    @Test
    void shouldCreateShortUrl() {
        ShortUrlController controller = new ShortUrlController(service());

        ResponseEntity<ShortenUrlResponse> response = controller.shorten(new ShortenUrlRequest("https://example.com"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ShortenUrlResponse responseBody = Objects.requireNonNull(response.getBody());
        assertThat(responseBody.originalUrl()).isEqualTo("https://example.com");
        assertThat(responseBody.shortCode()).hasSize(8);
        assertThat(responseBody.shortUrl()).isEqualTo("http://localhost:8080/" + responseBody.shortCode());
    }

    @Test
    void shouldRedirectToOriginalUrl() {
        ShortUrlController controller = new ShortUrlController(serviceWithPersistedShortUrl());

        ResponseEntity<Void> response = controller.redirect("abc123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("https://example.com"));
    }

    @Test
    void shouldReturnNotFoundWhenShortCodeDoesNotExist() {
        ShortUrlController controller = new ShortUrlController(serviceWithPersistedShortUrl());

        assertThatThrownBy(() -> controller.redirect("missing"))
                .isInstanceOf(ShortUrlNotFoundException.class)
                .hasMessage("URL encurtada nao encontrada para o codigo: missing");
    }

    private UrlShorteningService serviceWithPersistedShortUrl() {
        InMemoryShortUrlRepository repository = new InMemoryShortUrlRepository();
        UrlShortenerProperties properties = properties();
        repository.save(ShortUrl.builder()
                .shortCode("abc123")
                .originalUrl("https://example.com")
                .build());

        return new UrlShorteningService(generator(properties), new ShortUrlPersistenceService(repository), properties);
    }

    private UrlShorteningService service() {
        UrlShortenerProperties properties = properties();

        return new UrlShorteningService(
                generator(properties),
                new ShortUrlPersistenceService(new InMemoryShortUrlRepository()),
                properties
        );
    }

    private ShortUrlGenerator generator(UrlShortenerProperties properties) {
        return new ShortUrlGenerator(properties);
    }

    private UrlShortenerProperties properties() {
        UrlShortenerProperties properties = new UrlShortenerProperties();
        properties.setBaseUrl("http://localhost:8080");
        properties.setShortCodeLength(8);
        properties.setMaxGenerationAttempts(10);
        return properties;
    }
}
