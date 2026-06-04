package desafio.encurtador_url.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateShortUrlRequest {

	@NotBlank(message = "A URL original e obrigatoria.")
	@URL(regexp = "^(http|https)://.*", message = "A URL deve ser valida e usar http ou https.")
	private String url;
}
