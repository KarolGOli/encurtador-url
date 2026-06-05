package desafio.encurtador_url.service;

import desafio.encurtador_url.config.UrlShortenerProperties;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;

import desafio.encurtador_url.exception.ShortCodeGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShortUrlGenerator {

    private static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private final UrlShortenerProperties properties;
    private final RandomGenerator randomGenerator;

    @Autowired
    public ShortUrlGenerator(UrlShortenerProperties properties) {
        this(properties, new SecureRandom());
    }

    ShortUrlGenerator(UrlShortenerProperties properties, RandomGenerator randomGenerator) {
        this.properties = properties;
        this.randomGenerator = randomGenerator;
    }

    public String generateUniqueShortCode(Predicate<String> shortCodeExists) {
        Objects.requireNonNull(shortCodeExists, "shortCodeExists must not be null");

        for (int attempt = 0; attempt < properties.getMaxGenerationAttempts(); attempt++) {
            String shortCode = generateShortCode();

            if (!shortCodeExists.test(shortCode)) {
                return shortCode;
            }
        }

        throw new ShortCodeGenerationException("Nao foi possivel gerar um codigo curto unico");
    }

    public String buildShortUrl(String shortCode) {
        return normalizeBaseUrl() + "/" + shortCode;
    }

    private String generateShortCode() {
        StringBuilder shortCode = new StringBuilder(properties.getShortCodeLength());

        for (int index = 0; index < properties.getShortCodeLength(); index++) {
            shortCode.append(ALPHABET[randomGenerator.nextInt(ALPHABET.length)]);
        }

        return shortCode.toString();
    }

    private String normalizeBaseUrl() {
        return properties.getBaseUrl().replaceAll("/+$", "");
    }
}
