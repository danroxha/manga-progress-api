package com.api.mangaprogress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MangaExceededException extends Exception {

    public MangaExceededException(Long id, int quantityToIncrement) {
        super(String.format("Manga with %s ID to increment informed exceeds the max: %s", id, quantityToIncrement));
    }
}
