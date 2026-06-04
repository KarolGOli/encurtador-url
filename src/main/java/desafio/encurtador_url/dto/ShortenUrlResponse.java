package desafio.encurtador_url.dto;

public record ShortenUrlResponse(
        String originalUrl,
        String shortCode,
        String shortUrl
) {
}
