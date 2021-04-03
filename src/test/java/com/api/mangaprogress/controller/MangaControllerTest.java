package com.api.mangaprogress.controller;

import com.api.mangaprogress.builder.MangaDTOBuilder;
import com.api.mangaprogress.dto.MangaDTO;
import com.api.mangaprogress.exception.MangaNotFoundException;
import com.api.mangaprogress.service.MangaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.api.mangaprogress.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MangaControllerTest {

    private static final String MANGA_API_URL_PATH = "/api/v1/mangas";
    private static final long VALID_MANGA_ID = 1L;
    private static final long INVALID_MANGA_ID = 2l;
    private static final String MANGA_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String MANGA_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private MangaService mangaService;

    @InjectMocks
    private MangaController mangaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mangaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }
    
    @Test
    void whenPOSTIsCalledThenAMangaIsCreated() throws Exception {
        // given
        MangaDTO mangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();

        // when
        when(mangaService.createManga(mangaDTO)).thenReturn(mangaDTO);

        // then
        mockMvc.perform(post(MANGA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mangaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(mangaDTO.getName())))
                .andExpect(jsonPath("$.author", is(mangaDTO.getAuthor())))
                .andExpect(jsonPath("$.chapters", is(mangaDTO.getChapters())))
                .andExpect(jsonPath("$.genre", is(mangaDTO.getGenre().toString())));
    }
    
    
     
    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        MangaDTO mangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();
        mangaDTO.setAuthor(null);
        mangaDTO.setChapters(null);
        mangaDTO.setGenre(null);

        // then
        mockMvc.perform(post(MANGA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mangaDTO)))
                .andExpect(status().isBadRequest());
    }
    
    
    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        MangaDTO mangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();

        //when
        when(mangaService.findByName(mangaDTO.getName())).thenReturn(mangaDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(MANGA_API_URL_PATH + "/" + mangaDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(mangaDTO.getName())))
                .andExpect(jsonPath("$.chapters", is(mangaDTO.getChapters())))
                .andExpect(jsonPath("$.author", is(mangaDTO.getAuthor())))
                .andExpect(jsonPath("$.genre", is(mangaDTO.getGenre().toString())));
    }
    
    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        MangaDTO mangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();

        //when
        when(mangaService.findByName(mangaDTO.getName())).thenThrow(MangaNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(MANGA_API_URL_PATH + "/" + mangaDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
   
    @Test
    void whenGETListWithMangasIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        MangaDTO mangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();

        //when
        when(mangaService.listAll()).thenReturn(Collections.singletonList(mangaDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(MANGA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(mangaDTO.getName())))
                .andExpect(jsonPath("$[0].author", is(mangaDTO.getAuthor())))
                .andExpect(jsonPath("$[0].chapters", is(mangaDTO.getChapters())))
                .andExpect(jsonPath("$[0].genre", is(mangaDTO.getGenre().toString())));
    }
    
   
    @Test
    void whenGETListWithoutMangasIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        MangaDTO mangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();

        //when
        when(mangaService.listAll()).thenReturn(Collections.singletonList(mangaDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(MANGA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        MangaDTO mangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();

        //when
        doNothing().when(mangaService).deleteById(mangaDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(MANGA_API_URL_PATH + "/" + mangaDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
     
    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //when
        doThrow(MangaNotFoundException.class).when(mangaService).deleteById(INVALID_MANGA_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(MANGA_API_URL_PATH + "/" + INVALID_MANGA_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    
    }
    
// @Test
// void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
//     QuantityDTO quantityDTO = QuantityDTO.builder();
//             .quantity(10)
//             .build();
// 
//     MangaDTO mangaDTO = MangaDTOBuilder.builder().build().toMangaDTO();
//     // mangaDTO.setQuantity(mangaDTO.getQuantity() + quantityDTO.getQuantity());
//     mangaDTO.setChapters(20);

//     when(mangaService.increment(VALID_MANGA_ID, 20)).thenReturn(mangaDTO);

//     mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(asJsonString(quantityDTO))).andExpect(status().isOk())
//             .andExpect(jsonPath("$.name", is(mangaDTO.getName())))
//             .andExpect(jsonPath("$.brand", is(mangaDTO.getBrand())))
//             .andExpect(jsonPath("$.type", is(mangaDTO.getType().toString())))
//             .andExpect(jsonPath("$.quantity", is(mangaDTO.getQuantity())));
// }
    
//    @Test
//    void whenPATCHIsCalledToIncrementGreatherThanMaxThenBadRequestStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(30)
//                .build();
//
//        BeerDTO mangaDTO = BeerDTOBuilder.builder().build().toMangaDTO();
//        mangaDTO.setQuantity(mangaDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(beerService.increment(VALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerStockExceededException.class);
//
//        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .con(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
//    }

//    @Test
//    void whenPATCHIsCalledWithInvalidBeerIdToIncrementThenNotFoundStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(30)
//                .build();
//
//        when(beerService.increment(INVALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerNotFoundException.class);
//        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + INVALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void whenPATCHIsCalledToDecrementDiscountThenOKstatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(5)
//                .build();
//
//        BeerDTO mangaDTO = BeerDTOBuilder.builder().build().toMangaDTO();
//        mangaDTO.setQuantity(mangaDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(beerService.decrement(VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(mangaDTO);
//
//        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is(mangaDTO.getName())))
//                .andExpect(jsonPath("$.brand", is(mangaDTO.getBrand())))
//                .andExpect(jsonPath("$.type", is(mangaDTO.getType().toString())))
//                .andExpect(jsonPath("$.quantity", is(mangaDTO.getQuantity())));
//    }
//
//    @Test
//    void whenPATCHIsCalledToDEcrementLowerThanZeroThenBadRequestStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(60)
//                .build();
//
//        BeerDTO mangaDTO = BeerDTOBuilder.builder().build().toMangaDTO();
//        mangaDTO.setQuantity(mangaDTO.getQuantity() + quantityDTO.getQuantity());
//
//        when(beerService.decrement(VALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerStockExceededException.class);
//
//        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void whenPATCHIsCalledWithInvalidBeerIdToDecrementThenNotFoundStatusIsReturned() throws Exception {
//        QuantityDTO quantityDTO = QuantityDTO.builder()
//                .quantity(5)
//                .build();
//
//        when(beerService.decrement(INVALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerNotFoundException.class);
//        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + INVALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(quantityDTO)))
//                .andExpect(status().isNotFound());
//    }
}
