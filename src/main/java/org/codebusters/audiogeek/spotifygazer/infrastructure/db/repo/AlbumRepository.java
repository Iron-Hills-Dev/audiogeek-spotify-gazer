package org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo;

import org.codebusters.audiogeek.spotifygazer.infrastructure.db.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface AlbumRepository extends JpaRepository<AlbumEntity, UUID> {

    Optional<AlbumEntity> findByTitleAndReleaseDate(String title, LocalDate releaseDate);
}
