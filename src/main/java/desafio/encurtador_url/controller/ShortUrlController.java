package desafio.encurtador_url.controller;

import desafio.encurtador_url.dto.ShortenUrlRequest;
import desafio.encurtador_url.dto.ShortenUrlResponse;
import desafio.encurtador_url.service.UrlShorteningService;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShortUrlController {

    private final UrlShorteningService urlShorteningService;

    @PostMapping("/api/v1/urls")
    public ResponseEntity<ShortenUrlResponse> shorten(@RequestBody ShortenUrlRequest request) {
        ShortenUrlResponse response = urlShorteningService.shorten(request.getUrl());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlShorteningService.getOriginalUrl(shortCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
