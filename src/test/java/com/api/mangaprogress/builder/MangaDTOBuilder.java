package com.api.mangaprogress.builder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import com.api.mangaprogress.dto.MangaDTO;
import com.api.mangaprogress.enums.MangaType;

@Builder
public class MangaDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "One Piece";

    @Builder.Default
    private String author = "Oda";

    @Builder.Default
    private int chapters = 1001;

    @Builder.Default
    private MangaType type = MangaType.OTHER;

    public MangaDTO toBeerDTO() {
        return new MangaDTO(id,
                name,
                author,
                chapters,
                type);
    }
}
