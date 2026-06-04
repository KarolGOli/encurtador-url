package desafio.encurtador_url.support;

import desafio.encurtador_url.entity.ShortUrl;
import desafio.encurtador_url.repository.ShortUrlRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryShortUrlRepository implements ShortUrlRepository {

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
