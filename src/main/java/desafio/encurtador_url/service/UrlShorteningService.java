package desafio.encurtador_url.service;

import desafio.encurtador_url.dto.ShortenUrlResponse;
import desafio.encurtador_url.entity.ShortUrl;
import java.time.Instant;
import java.util.Optional;

import desafio.encurtador_url.exception.ShortUrlNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShorteningService {

    private final ShortUrlGenerator shortUrlGenerator;
    private final ShortUrlPersistenceService persistenceService;

    public ShortenUrlResponse shorten(String originalUrl) {
        String shortCode = shortUrlGenerator.generateUniqueShortCode(persistenceService::existsByShortCode);
        ShortUrl shortUrl = ShortUrl.builder()
                .shortCode(shortCode)
                .originalUrl(originalUrl)
                .createdAt(Instant.now())
                .build();

        ShortUrl savedShortUrl = persistenceService.save(shortUrl);

        return new ShortenUrlResponse(
                savedShortUrl.getOriginalUrl(),
                savedShortUrl.getShortCode(),
                shortUrlGenerator.buildShortUrl(savedShortUrl.getShortCode())
        );
    }

    public Optional<String> findOriginalUrl(String shortCode) {
        return persistenceService.findByShortCode(shortCode)
                .map(ShortUrl::getOriginalUrl);
    }

    public String getOriginalUrl(String shortCode) {
        return findOriginalUrl(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException(shortCode));
    }
}
