package desafio.encurtador_url.repository;

import desafio.encurtador_url.entity.ShortUrl;
import org.springframework.data.repository.CrudRepository;

public interface ShortUrlRepository extends CrudRepository<ShortUrl, String> {
}
