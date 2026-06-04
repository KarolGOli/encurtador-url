package desafio.encurtador_url.service;

import static org.assertj.core.api.Assertions.assertThat;

import desafio.encurtador_url.support.InMemoryShortUrlRepository;
import desafio.encurtador_url.entity.ShortUrl;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ShortUrlPersistenceServiceTest {

    @Test
    void shouldSaveShortUrl() {
        InMemoryShortUrlRepository repository = new InMemoryShortUrlRepository();
        ShortUrlPersistenceService service = new ShortUrlPersistenceService(repository);
        ShortUrl shortUrl = shortUrl();

        ShortUrl savedShortUrl = service.save(shortUrl);

        assertThat(savedShortUrl).isSameAs(shortUrl);
        assertThat(repository.findById("abc123")).contains(shortUrl);
    }

    @Test
    void shouldFindShortUrlByShortCode() {
        InMemoryShortUrlRepository repository = new InMemoryShortUrlRepository();
        ShortUrlPersistenceService service = new ShortUrlPersistenceService(repository);
        ShortUrl shortUrl = shortUrl();
        repository.save(shortUrl);

        Optional<ShortUrl> foundShortUrl = service.findByShortCode("abc123");

        assertThat(foundShortUrl).contains(shortUrl);
    }

    @Test
    void shouldReturnEmptyWhenShortCodeDoesNotExist() {
        ShortUrlPersistenceService service = new ShortUrlPersistenceService(new InMemoryShortUrlRepository());

        Optional<ShortUrl> foundShortUrl = service.findByShortCode("missing");

        assertThat(foundShortUrl).isEmpty();
    }

    @Test
    void shouldCheckIfShortCodeExists() {
        InMemoryShortUrlRepository repository = new InMemoryShortUrlRepository();
        ShortUrlPersistenceService service = new ShortUrlPersistenceService(repository);
        repository.save(shortUrl());

        boolean exists = service.existsByShortCode("abc123");

        assertThat(exists).isTrue();
    }

    private ShortUrl shortUrl() {
        return ShortUrl.builder()
                .shortCode("abc123")
                .originalUrl("https://example.com")
                .createdAt(Instant.parse("2026-06-04T12:00:00Z"))
                .build();
    }
}
