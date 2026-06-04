package desafio.encurtador_url.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ShortUrlResponse {

	private final String originalUrl;

	private final String shortCode;

	private final String shortUrl;
}
