package desafio.encurtador_url.exception;

public class ShortUrlNotFoundException extends RuntimeException {
    public ShortUrlNotFoundException(String shortCode) {
        super("URL encurtada nao encontrada para o codigo: " + shortCode);
    }
}
