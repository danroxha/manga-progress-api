package com.api.mangaprogress.controller;

import lombok.AllArgsConstructor;
import com.api.mangaprogress.dto.MangaDTO;
import com.api.mangaprogress.exception.MangaAlreadyRegisteredException;
import com.api.mangaprogress.exception.MangaDataInvalidException;
import com.api.mangaprogress.exception.MangaNotFoundException;
import com.api.mangaprogress.service.MangaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v1/mangas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MangaController implements MangaControllerDocs {

    private final MangaService mangaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MangaDTO createManga(@RequestBody @Valid MangaDTO mangaDTO) throws MangaAlreadyRegisteredException {
        return mangaService.createManga(mangaDTO);
    }

    @GetMapping("/{name}")
    public MangaDTO findByName(@PathVariable String name) throws MangaNotFoundException {
        return mangaService.findByName(name);
    }

    @GetMapping
    public List<MangaDTO> listMangas() {
        return mangaService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws MangaNotFoundException {
        mangaService.deleteById(id);
    }
    
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MangaDTO updateMangaById(@RequestBody @Valid MangaDTO mangaDTO, @PathVariable Long id) throws MangaNotFoundException, MangaDataInvalidException {
        return mangaService.updateManga(mangaDTO, id);
    }

}
