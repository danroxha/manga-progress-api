package com.api.mangaprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.api.mangaprogress.enums.MangaGenre;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MangaDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String name;

    @NotNull
    @Size(min = 1, max = 200)
    private String author;

    @NotNull
    @Max(5000)
    private Integer chapters;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private MangaGenre genre;
}
