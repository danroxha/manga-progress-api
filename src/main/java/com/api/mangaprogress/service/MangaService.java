package com.api.mangaprogress.service;

import lombok.AllArgsConstructor;
import com.api.mangaprogress.dto.MangaDTO;
import com.api.mangaprogress.entity.Manga;
import com.api.mangaprogress.exception.MangaAlreadyRegisteredException;
import com.api.mangaprogress.exception.MangaNotFoundException;
import com.api.mangaprogress.exception.MangaExceededException;
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

    public MangaDTO createManga(MangaDTO beerDTO) throws MangaAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        Manga beer = mangaMapper.toModel(beerDTO);
        Manga savedBeer = mangaRepository.save(beer);
        return mangaMapper.toDTO(savedBeer);
    }

    public MangaDTO findByName(String name) throws MangaNotFoundException {
        Manga foundBeer = mangaRepository.findByName(name)
                .orElseThrow(() -> new MangaNotFoundException(name));
        return mangaMapper.toDTO(foundBeer);
    }

    public List<MangaDTO> listAll() {
        return mangaRepository.findAll()
                .stream()
                .map(mangaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws MangaNotFoundException {
        verifyIfExists(id);
        mangaRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws MangaAlreadyRegisteredException {
        Optional<Manga> optSavedManga = mangaRepository.findByName(name);
        if (optSavedManga.isPresent()) {
            throw new MangaAlreadyRegisteredException(name);
        }
    }

    private Manga verifyIfExists(Long id) throws MangaNotFoundException {
        return mangaRepository.findById(id)
                .orElseThrow(() -> new MangaNotFoundException(id));
    }

    public MangaDTO increment(Long id, int quantityToIncrement) throws MangaNotFoundException, MangaExceededException {
        Manga mangaToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + mangaToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= mangaToIncrementStock.getMax()) {
            mangaToIncrementStock.setQuantity(mangaToIncrementStock.getQuantity() + quantityToIncrement);
            Manga incrementedManga = mangaRepository.save(mangaToIncrementStock);
            return mangaMapper.toDTO(incrementedManga);
        }
        throw new MangaExceededException(id, quantityToIncrement);
    }
}
