package com.api.mangaprogress.repository;

import com.api.mangaprogress.entity.Manga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MangaRepository extends JpaRepository<Manga, Long> {

    Optional<Manga> findByName(String name);
}
