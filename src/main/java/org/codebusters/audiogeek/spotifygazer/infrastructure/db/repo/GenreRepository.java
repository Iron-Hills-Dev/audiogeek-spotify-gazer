package org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo;

import org.codebusters.audiogeek.spotifygazer.infrastructure.db.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<GenreEntity, UUID> {
    Optional<GenreEntity> findByName(String name);
}
