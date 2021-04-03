package com.api.mangaprogress.mapper;

import com.api.mangaprogress.dto.MangaDTO;
import com.api.mangaprogress.entity.Manga;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MangaMapper {

    MangaMapper INSTANCE = Mappers.getMapper(MangaMapper.class);

    Manga toModel(MangaDTO mangaDTO);

    MangaDTO toDTO(Manga manga);
}
