package desafio.encurtador_url.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "app.url-shortener")
public class UrlShortenerProperties {

    @NotBlank
    private String baseUrl = "http://localhost:8080";

    @Min(4)
    private int shortCodeLength = 8;

    @Min(1)
    private int maxGenerationAttempts = 10;

    @NotNull
    private Duration urlExpiration = Duration.ZERO;
}