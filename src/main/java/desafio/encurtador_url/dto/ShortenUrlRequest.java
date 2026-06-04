package desafio.encurtador_url.dto;

import desafio.encurtador_url.validation.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortenUrlRequest {

    @NotBlank(message = "A URL original e obrigatoria")
    @ValidUrl
    private String url;
}
