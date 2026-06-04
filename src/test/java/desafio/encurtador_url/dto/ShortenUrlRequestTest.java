package desafio.encurtador_url.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ShortenUrlRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldAcceptHttpUrl() {
        ShortenUrlRequest request = new ShortenUrlRequest("http://example.com/page");

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void shouldAcceptHttpsUrl() {
        ShortenUrlRequest request = new ShortenUrlRequest("https://example.com/page?source=test");

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void shouldRejectBlankUrl() {
        ShortenUrlRequest request = new ShortenUrlRequest(" ");

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("url");
    }

    @Test
    void shouldRejectUrlWithoutHost() {
        ShortenUrlRequest request = new ShortenUrlRequest("https:///missing-host");

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getMessage())
                .contains("A URL original deve ser valida e usar http ou https");
    }

    @Test
    void shouldRejectUnsupportedProtocol() {
        ShortenUrlRequest request = new ShortenUrlRequest("ftp://example.com/file");

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getMessage())
                .contains("A URL original deve ser valida e usar http ou https");
    }

    @Test
    void shouldRejectInvalidUrlFormat() {
        ShortenUrlRequest request = new ShortenUrlRequest("not a valid url");

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getMessage())
                .contains("A URL original deve ser valida e usar http ou https");
    }
}
