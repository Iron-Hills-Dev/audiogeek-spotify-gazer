package org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo;

import org.codebusters.audiogeek.spotifygazer.infrastructure.db.AlbumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AlbumRepository extends JpaRepository<AlbumEntity, UUID> {
    Optional<AlbumEntity> findByTitleAndReleaseDate(String title, LocalDate releaseDate);

    @Query("SELECT a FROM AlbumEntity a ORDER BY a.releaseDate DESC")
    Page<AlbumEntity> findAllOrderByReleaseDate(Pageable pageable);

    @Query("SELECT DISTINCT a FROM AlbumEntity a INNER JOIN a.genres g WHERE g.name IN :genres ORDER BY a.releaseDate DESC")
    Page<AlbumEntity> findByGenres(@Param("genres") Set<String> genres, Pageable pageable);
}
