package desafio.encurtador_url.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("short-url")
public class ShortUrl {

	@Id
	private String shortCode;

	private String originalUrl;

	private Instant createdAt;

	private Instant expiresAt;

	public ShortUrl() {
	}

	public ShortUrl(String shortCode, String originalUrl, Instant createdAt, Instant expiresAt) {
		this.shortCode = shortCode;
		this.originalUrl = originalUrl;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Instant expiresAt) {
		this.expiresAt = expiresAt;
	}
}
