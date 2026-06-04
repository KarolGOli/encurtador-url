package desafio.encurtador_url.dto;

public record ShortUrlResponse(
		String originalUrl,
		String shortCode,
		String shortUrl
) {
}
