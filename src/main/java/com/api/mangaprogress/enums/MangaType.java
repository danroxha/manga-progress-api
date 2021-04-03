package com.api.mangaprogress.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MangaType {
    DOUJINSHI("Doujinshi"),
    GEKIGA("Gekiga"),
    HENTAI("Hentai"),
    JOSEI("Josei"),
    SEINEN("Seinen"),
    SHOUJO("Shoujo"),
    SHONEN("Shoune"),
    KODOMO("Kodomo"),
    YURI("Yuri"),
    YAOI("Yaoi"),
    OTHER("Other");

    private final String description;
}
