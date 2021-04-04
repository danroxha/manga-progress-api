package com.api.mangaprogress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MangaDataInvalidException extends Exception {
    public MangaDataInvalidException() {
        super(String.format("Manga data should has data valid"));
    }
}
