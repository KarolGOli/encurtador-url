package desafio.encurtador_url.controller;

import desafio.encurtador_url.dto.ApiErrorResponse;
import desafio.encurtador_url.dto.FieldErrorResponse;
import desafio.encurtador_url.exception.ShortCodeGenerationException;
import desafio.encurtador_url.exception.ShortUrlNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.InvalidUrlException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<FieldErrorResponse> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldErrorResponse(
                        fieldError.getField(),
                        Objects.toString(fieldError.getDefaultMessage(), "Campo invalido")
                ))
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Requisicao invalida",
                "Existem campos invalidos na requisicao",
                request.getRequestURI(),
                fieldErrors
        );
    }

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidUrlException(
            InvalidUrlException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, "URL invalida", exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ShortUrlNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleShortUrlNotFoundException(
            ShortUrlNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "URL nao encontrada",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ShortCodeGenerationException.class)
    public ResponseEntity<ApiErrorResponse> handleShortCodeGenerationException(
            ShortCodeGenerationException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro ao gerar codigo curto",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String message,
            String path
    ) {
        return buildResponse(status, error, message, path, List.of());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String message,
            String path,
            List<FieldErrorResponse> fieldErrors
    ) {
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(Instant.now(), status.value(), error, message, path, fieldErrors));
    }
}
