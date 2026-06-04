package desafio.encurtador_url.dto;

import java.time.Instant;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

	private final Instant timestamp;

	private final int status;

	private final String error;

	private final String message;

	private final String path;

	private final Map<String, String> details;

	public static ErrorResponse of(int status, String error, String message, String path) {
		return ErrorResponse.builder()
				.timestamp(Instant.now())
				.status(status)
				.error(error)
				.message(message)
				.path(path)
				.details(Map.of())
				.build();
	}

	public static ErrorResponse withDetails(
			int status,
			String error,
			String message,
			String path,
			Map<String, String> details
	) {
		return ErrorResponse.builder()
				.timestamp(Instant.now())
				.status(status)
				.error(error)
				.message(message)
				.path(path)
				.details(details)
				.build();
	}
}
