package com.api.mangaprogress.service;

import lombok.AllArgsConstructor;
import com.api.mangaprogress.dto.MangaDTO;
import com.api.mangaprogress.entity.Manga;
import com.api.mangaprogress.exception.MangaAlreadyRegisteredException;
import com.api.mangaprogress.exception.MangaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.api.mangaprogress.mapper.MangaMapper;
import com.api.mangaprogress.repository.MangaRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MangaService {

    private final MangaRepository mangaRepository;
    private final MangaMapper mangaMapper = MangaMapper.INSTANCE;

    public MangaDTO createManga(MangaDTO mangaDTO) throws MangaAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(mangaDTO.getName());
        Manga manga = mangaMapper.toModel(mangaDTO);
        Manga savedManga = mangaRepository.save(manga);
        return mangaMapper.toDTO(savedManga);
    }

    public MangaDTO findByName(String name) throws MangaNotFoundException {
        Manga foundManga = mangaRepository.findByName(name)
                .orElseThrow(() -> new MangaNotFoundException(name));
        return mangaMapper.toDTO(foundManga);
    }

    public List<MangaDTO> listAll() {
        return mangaRepository
                .findAll()
                .stream()
                .map(mangaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws MangaNotFoundException {
        verifyIfExists(id);
        mangaRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws MangaAlreadyRegisteredException {
        Optional<Manga> optionalSavedManga = mangaRepository.findByName(name);
        if (optionalSavedManga.isPresent()) {
            throw new MangaAlreadyRegisteredException(name);
        }
    }

    private Manga verifyIfExists(Long id) throws MangaNotFoundException {
        return mangaRepository
                .findById(id)
                .orElseThrow(() -> new MangaNotFoundException(id));
    }

}
