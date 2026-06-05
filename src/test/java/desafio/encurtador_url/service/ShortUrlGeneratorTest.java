package desafio.encurtador_url.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import desafio.encurtador_url.config.UrlShortenerProperties;
import desafio.encurtador_url.exception.ShortCodeGenerationException;
import org.junit.jupiter.api.Test;

class ShortUrlGeneratorTest {

    @Test
    void shouldGenerateShortCodeWithConfiguredLength() {
        UrlShortenerProperties properties = properties();
        properties.setShortCodeLength(10);
        ShortUrlGenerator generator = new ShortUrlGenerator(properties, new Random(1));

        String shortCode = generator.generateUniqueShortCode(code -> false);

        assertThat(shortCode)
                .hasSize(10)
                .matches("[0-9A-Za-z]+");
    }

    @Test
    void shouldRetryWhenGeneratedShortCodeAlreadyExists() {
        UrlShortenerProperties properties = properties();
        ShortUrlGenerator generator = new ShortUrlGenerator(properties, new Random(1));
        AtomicInteger attempts = new AtomicInteger();

        String shortCode = generator.generateUniqueShortCode(code -> attempts.getAndIncrement() == 0);

        assertThat(shortCode).hasSize(properties.getShortCodeLength());
        assertThat(attempts).hasValue(2);
    }

    @Test
    void shouldFailWhenMaxGenerationAttemptsIsReached() {
        UrlShortenerProperties properties = properties();
        properties.setMaxGenerationAttempts(3);
        ShortUrlGenerator generator = new ShortUrlGenerator(properties, new Random(1));
        AtomicInteger attempts = new AtomicInteger();

        assertThatThrownBy(() -> generator.generateUniqueShortCode(code -> {
            attempts.incrementAndGet();
            return true;
        }))
                .isInstanceOf(ShortCodeGenerationException.class)
                .hasMessage("Nao foi possivel gerar um codigo curto unico");

        assertThat(attempts).hasValue(3);
    }

    @Test
    void shouldBuildShortUrlUsingConfiguredBaseUrl() {
        UrlShortenerProperties properties = properties();
        properties.setBaseUrl("https://sho.rt/");
        ShortUrlGenerator generator = new ShortUrlGenerator(properties, new Random(1));

        assertThat(generator.buildShortUrl("abc123")).isEqualTo("https://sho.rt/abc123");
    }

    private UrlShortenerProperties properties() {
        UrlShortenerProperties properties = new UrlShortenerProperties();
        properties.setBaseUrl("http://localhost:8080");
        properties.setShortCodeLength(8);
        properties.setMaxGenerationAttempts(10);
        return properties;
    }
}
