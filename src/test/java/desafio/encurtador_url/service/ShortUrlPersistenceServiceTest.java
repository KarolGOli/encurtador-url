package desafio.encurtador_url.service;

import static org.assertj.core.api.Assertions.assertThat;

import desafio.encurtador_url.entity.ShortUrl;
import desafio.encurtador_url.repository.ShortUrlRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
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

    private static class InMemoryShortUrlRepository implements ShortUrlRepository {

        private final Map<String, ShortUrl> shortUrls = new HashMap<>();

        @Override
        public <S extends ShortUrl> S save(S entity) {
            shortUrls.put(entity.getShortCode(), entity);
            return entity;
        }

        @Override
        public <S extends ShortUrl> Iterable<S> saveAll(Iterable<S> entities) {
            entities.forEach(this::save);
            return entities;
        }

        @Override
        public Optional<ShortUrl> findById(String shortCode) {
            return Optional.ofNullable(shortUrls.get(shortCode));
        }

        @Override
        public boolean existsById(String shortCode) {
            return shortUrls.containsKey(shortCode);
        }

        @Override
        public Iterable<ShortUrl> findAll() {
            return shortUrls.values();
        }

        @Override
        public Iterable<ShortUrl> findAllById(Iterable<String> shortCodes) {
            return () -> shortUrls.entrySet().stream()
                    .filter(entry -> contains(shortCodes, entry.getKey()))
                    .map(Map.Entry::getValue)
                    .iterator();
        }

        @Override
        public long count() {
            return shortUrls.size();
        }

        @Override
        public void deleteById(String shortCode) {
            shortUrls.remove(shortCode);
        }

        @Override
        public void delete(ShortUrl entity) {
            shortUrls.remove(entity.getShortCode());
        }

        @Override
        public void deleteAllById(Iterable<? extends String> shortCodes) {
            shortCodes.forEach(shortUrls::remove);
        }

        @Override
        public void deleteAll(Iterable<? extends ShortUrl> entities) {
            entities.forEach(this::delete);
        }

        @Override
        public void deleteAll() {
            shortUrls.clear();
        }

        private boolean contains(Iterable<String> shortCodes, String shortCode) {
            for (String currentShortCode : shortCodes) {
                if (currentShortCode.equals(shortCode)) {
                    return true;
                }
            }

            return false;
        }
    }
}
