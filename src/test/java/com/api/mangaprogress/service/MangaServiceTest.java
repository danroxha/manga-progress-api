package com.api.mangaprogress.service;

import com.api.mangaprogress.builder.MangaDTOBuilder;
import com.api.mangaprogress.dto.MangaDTO;
import com.api.mangaprogress.entity.Manga;
import com.api.mangaprogress.exception.MangaAlreadyRegisteredException;
import com.api.mangaprogress.exception.MangaNotFoundException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.api.mangaprogress.mapper.MangaMapper;
import com.api.mangaprogress.repository.MangaRepository;

@ExtendWith(MockitoExtension.class)
public class MangaServiceTest {

    private static final long INVALID_MANGA_ID = 1L;

    @Mock
    private MangaRepository mangaRepository;

    private MangaMapper mangaMapper = MangaMapper.INSTANCE;

    @InjectMocks
    private MangaService mangaService;

    @Test
    void whenBeerInformedThenItShouldBeCreated() throws MangaAlreadyRegisteredException {
        // given
        MangaDTO expectedMangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();
        Manga expectedSavedManga = mangaMapper.toModel(expectedMangaDTO);

        // when
        when(mangaRepository.findByName(expectedMangaDTO.getName())).thenReturn(Optional.empty());
        when(mangaRepository.save(expectedSavedManga)).thenReturn(expectedSavedManga);

        //then
        MangaDTO createdMangaDTO = mangaService.createManga(expectedMangaDTO);

        assertThat(createdMangaDTO.getId(), is(equalTo(expectedMangaDTO.getId())));
        assertThat(createdMangaDTO.getName(), is(equalTo(expectedMangaDTO.getName())));
        assertThat(createdMangaDTO.getAuthor(), is(equalTo(expectedMangaDTO.getAuthor())));
        assertThat(createdMangaDTO.getChapters(), is(equalTo(expectedMangaDTO.getChapters())));
        
    }

    @Test
    void whenAlreadyRegisteredMangaInformedThenAnExceptionShouldBeThrown() {
        // given
        MangaDTO expectedMangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();
        Manga duplicatedManga = mangaMapper.toModel(expectedMangaDTO);

        // when
        when(mangaRepository.findByName(expectedMangaDTO.getName())).thenReturn(Optional.of(duplicatedManga));

        // then
        assertThrows(MangaAlreadyRegisteredException.class, () -> mangaService.createManga(expectedMangaDTO));
    }

    @Test
    void whenValidMangaNameIsGivenThenReturnAManga() throws MangaNotFoundException {
        // given
        MangaDTO expectedFoundMangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();
        Manga expectedFoundManga = mangaMapper.toModel(expectedFoundMangaDTO);

        // when
        when(mangaRepository.findByName(expectedFoundManga.getName())).thenReturn(Optional.of(expectedFoundManga));

        // then
        MangaDTO foundMangaDTO = mangaService.findByName(expectedFoundMangaDTO.getName());

        assertThat(foundMangaDTO, is(equalTo(expectedFoundMangaDTO)));
    }

    @Test
    void whenNotRegisteredMangaNameIsGivenThenThrowAnException() {
        // given
        MangaDTO expectedFoundMangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();

        // when
        when(mangaRepository.findByName(expectedFoundMangaDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(MangaNotFoundException.class, () -> mangaService.findByName(expectedFoundMangaDTO.getName()));
    }

    @Test
    void whenListMangaIsCalledThenReturnAListOfMangas() {
        // given
        MangaDTO expectedFoundMangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();
        Manga expectedFoundManga = mangaMapper.toModel(expectedFoundMangaDTO);

        //when
        when(mangaRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundManga));

        //then
        List<MangaDTO> foundListMangasDTO = mangaService.listAll();

        assertThat(foundListMangasDTO, is(not(empty())));
        assertThat(foundListMangasDTO.get(0), is(equalTo(expectedFoundMangaDTO)));
    }

    @Test
    void whenListMangaIsCalledThenReturnAnEmptyListOfMangas() {
        //when
        when(mangaRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<MangaDTO> foundListMangasDTO = mangaService.listAll();

        assertThat(foundListMangasDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAMangaShouldBeDeleted() throws MangaNotFoundException{
        // given
        MangaDTO expectedDeletedMangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();
        Manga expectedDeletedManga = mangaMapper.toModel(expectedDeletedMangaDTO);

        // when
        when(mangaRepository.findById(expectedDeletedMangaDTO.getId())).thenReturn(Optional.of(expectedDeletedManga));
        doNothing().when(mangaRepository).deleteById(expectedDeletedMangaDTO.getId());

        // then
        mangaService.deleteById(expectedDeletedMangaDTO.getId());

        verify(mangaRepository, times(1)).findById(expectedDeletedMangaDTO.getId());
        verify(mangaRepository, times(1)).deleteById(expectedDeletedMangaDTO.getId());
    }

  
//
//    @Test
//    void whenDecrementIsCalledThenDecrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toMangaDTO();
//        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);
//
//        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//        when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);
//
//        int quantityToDecrement = 5;
//        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
//        BeerDTO incrementedBeerDTO = beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
//        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
//    }
//
//    @Test
//    void whenDecrementIsCalledToEmptyStockThenEmptyBeerStock() throws BeerNotFoundException, BeerStockExceededException {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toMangaDTO();
//        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);
//
//        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//        when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);
//
//        int quantityToDecrement = 10;
//        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
//        BeerDTO incrementedBeerDTO = beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(0));
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
//    }
//
//    @Test
//    void whenDecrementIsLowerThanZeroThenThrowException() {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toMangaDTO();
//        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);
//
//        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//
//        int quantityToDecrement = 80;
//        assertThrows(BeerStockExceededException.class, () -> beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement));
//    }
//
//    @Test
//    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
//        int quantityToDecrement = 10;
//
//        when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());
//
//        assertThrows(BeerNotFoundException.class, () -> beerService.decrement(INVALID_BEER_ID, quantityToDecrement));
//    }
}
