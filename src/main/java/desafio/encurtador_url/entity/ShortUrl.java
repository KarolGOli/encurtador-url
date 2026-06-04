package desafio.encurtador_url.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("short-url")
public class ShortUrl {

	@Id
	private String shortCode;

	private String originalUrl;

	private Instant createdAt;

	private Instant expiresAt;
}
