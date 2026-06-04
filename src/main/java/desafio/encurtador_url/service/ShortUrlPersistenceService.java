package desafio.encurtador_url.service;

import desafio.encurtador_url.entity.ShortUrl;
import desafio.encurtador_url.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShortUrlPersistenceService {

    private final ShortUrlRepository repository;

    public ShortUrl save(ShortUrl shortUrl) {
        return repository.save(Objects.requireNonNull(shortUrl, "shortUrl must not be null"));
    }

    public Optional<ShortUrl> findByShortCode(String shortCode) {
        return repository.findById(Objects.requireNonNull(shortCode, "shortCode must not be null"));
    }

    public boolean existsByShortCode(String shortCode) {
        return repository.existsById(Objects.requireNonNull(shortCode, "shortCode must not be null"));
    }
}
