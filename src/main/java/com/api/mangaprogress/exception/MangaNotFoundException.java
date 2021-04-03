package com.api.mangaprogress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MangaNotFoundException extends Exception {

    public MangaNotFoundException(String beerName) {
        super(String.format("Beer with name %s not found in the system.", beerName));
    }

    public MangaNotFoundException(Long id) {
        super(String.format("Beer with id %s not found in the system.", id));
    }
}
