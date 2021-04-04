package com.api.mangaprogress.service;

import lombok.AllArgsConstructor;
import com.api.mangaprogress.dto.MangaDTO;
import com.api.mangaprogress.entity.Manga;
import com.api.mangaprogress.exception.MangaAlreadyRegisteredException;
import com.api.mangaprogress.exception.MangaDataInvalidException;
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
    
    public MangaDTO updateManga(MangaDTO mangaDTO, Long id) throws MangaNotFoundException, MangaDataInvalidException {
        
        Manga manga = mangaMapper.toModel(mangaDTO);

        verifyIfIsValidDataManga(manga);
        
        Manga foundManga = mangaRepository.findById(id)
                .orElseThrow(() -> new MangaNotFoundException(id));
        
  
      	foundManga.setName(manga.getName());
      	foundManga.setChapters(manga.getChapters());
      	foundManga.setAuthor(manga.getAuthor());
      	foundManga.setGenre(manga.getGenre());
        
        Manga updatedManga = mangaRepository.save(foundManga);
        
        return mangaMapper.toDTO(updatedManga);
    }
    
    private void verifyIfIsValidDataManga(Manga manga) throws MangaDataInvalidException {
    
        final Integer MIN_VALUE_CHAPTER = 0;
        final Integer MAX_VALUE_CHAPTER = 5000;
        final Integer MIN_LENGTH_STRING = 1;
        final Integer MAX_LENGTH_STRING = 200;
        
        boolean mangaChapterIsValid = manga.getChapters() >= MIN_VALUE_CHAPTER 
                && manga.getChapters() <= MAX_VALUE_CHAPTER;
        
        boolean mangaNameIsValid = manga.getName() != null 
                && manga.getName().length() >= MIN_LENGTH_STRING
                && manga.getName().length() <= MAX_LENGTH_STRING;

	if(!mangaNameIsValid || !mangaChapterIsValid)
            throw new MangaDataInvalidException();
       
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
