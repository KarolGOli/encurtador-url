package desafio.encurtador_url.service;

import static org.assertj.core.api.Assertions.assertThat;

import desafio.encurtador_url.config.UrlShortenerProperties;
import desafio.encurtador_url.dto.ShortenUrlResponse;
import desafio.encurtador_url.entity.ShortUrl;
import desafio.encurtador_url.support.InMemoryShortUrlRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;

class UrlShorteningServiceTest {

    @Test
    void shouldShortenAndPersistOriginalUrl() {
        InMemoryShortUrlRepository repository = new InMemoryShortUrlRepository();
        ShortUrlPersistenceService persistenceService = new ShortUrlPersistenceService(repository);
        UrlShortenerProperties properties = properties();
        UrlShorteningService service = new UrlShorteningService(generator(properties), persistenceService, properties);

        ShortenUrlResponse response = service.shorten("https://example.com");


        assertThat(response.originalUrl()).isEqualTo("https://example.com");
        assertThat(response.shortCode()).hasSize(8);
        assertThat(response.shortUrl()).isEqualTo("http://localhost:8080/" + response.shortCode());
        assertThat(repository.findById(response.shortCode()))
                .get()
                .extracting(ShortUrl::getOriginalUrl)
                .isEqualTo("https://example.com");
    }

    @Test
    void shouldFindOriginalUrlByShortCode() {
        InMemoryShortUrlRepository repository = new InMemoryShortUrlRepository();
        ShortUrlPersistenceService persistenceService = new ShortUrlPersistenceService(repository);
        UrlShortenerProperties properties = properties();
        UrlShorteningService service = new UrlShorteningService(generator(properties), persistenceService, properties);
        repository.save(ShortUrl.builder()
                .shortCode("abc123")
                .originalUrl("https://example.com")
                .build());

        Optional<String> originalUrl = service.findOriginalUrl("abc123");

        assertThat(originalUrl).contains("https://example.com");
    }

    @Test
    void shouldPersistExpirationWhenConfigured() {
        InMemoryShortUrlRepository repository = new InMemoryShortUrlRepository();
        ShortUrlPersistenceService persistenceService = new ShortUrlPersistenceService(repository);
        UrlShortenerProperties properties = properties();
        properties.setUrlExpiration(Duration.ofHours(1));
        UrlShorteningService service = new UrlShorteningService(generator(properties), persistenceService, properties);

        Instant beforeShortening = Instant.now();
        ShortenUrlResponse response = service.shorten("https://example.com");
        ShortUrl savedShortUrl = repository.findById(response.shortCode()).orElseThrow();

        assertThat(savedShortUrl.getExpiresAt()).isAfter(beforeShortening);
    }

    private ShortUrlGenerator generator(UrlShortenerProperties properties) {
        return new ShortUrlGenerator(properties, new Random(1));
    }

    @Test
    void shouldReturnEmptyWhenShortCodeDoesNotExist() {
        UrlShortenerProperties properties = properties();
        UrlShorteningService service = new UrlShorteningService(
                generator(properties),
                new ShortUrlPersistenceService(new InMemoryShortUrlRepository()),
                properties
        );

        Optional<String> originalUrl = service.findOriginalUrl("missing");

        assertThat(originalUrl).isEmpty();
    }

    private UrlShortenerProperties properties() {
        UrlShortenerProperties properties = new UrlShortenerProperties();
        properties.setBaseUrl("http://localhost:8080");
        properties.setShortCodeLength(8);
        properties.setMaxGenerationAttempts(10);
        return properties;
    }
}
