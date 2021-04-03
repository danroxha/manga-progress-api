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

    private final MangaRepository beerRepository;
    private final MangaMapper beerMapper = MangaMapper.INSTANCE;

    public MangaDTO createBeer(MangaDTO beerDTO) throws MangaAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        Manga beer = beerMapper.toModel(beerDTO);
        Manga savedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(savedBeer);
    }

    public MangaDTO findByName(String name) throws MangaNotFoundException {
        Manga foundBeer = beerRepository.findByName(name)
                .orElseThrow(() -> new MangaNotFoundException(name));
        return beerMapper.toDTO(foundBeer);
    }

    public List<MangaDTO> listAll() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws MangaNotFoundException {
        verifyIfExists(id);
        beerRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws MangaAlreadyRegisteredException {
        Optional<Manga> optSavedBeer = beerRepository.findByName(name);
        if (optSavedBeer.isPresent()) {
            throw new MangaAlreadyRegisteredException(name);
        }
    }

    private Manga verifyIfExists(Long id) throws MangaNotFoundException {
        return beerRepository.findById(id)
                .orElseThrow(() -> new MangaNotFoundException(id));
    }

    public MangaDTO increment(Long id, int quantityToIncrement) throws MangaNotFoundException, MangaExceededException {
        Manga beerToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + beerToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= beerToIncrementStock.getMax()) {
            beerToIncrementStock.setQuantity(beerToIncrementStock.getQuantity() + quantityToIncrement);
            Manga incrementedBeerStock = beerRepository.save(beerToIncrementStock);
            return beerMapper.toDTO(incrementedBeerStock);
        }
        throw new MangaExceededException(id, quantityToIncrement);
    }
}
