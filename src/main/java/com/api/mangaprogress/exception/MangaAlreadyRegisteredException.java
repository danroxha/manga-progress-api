package com.api.mangaprogress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MangaAlreadyRegisteredException extends Exception{

    public MangaAlreadyRegisteredException(String mangaName) {
        super(String.format("Manga with name %s already registered in the system.", mangaName));
    }
}
