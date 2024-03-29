package org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo;

import org.codebusters.audiogeek.spotifygazer.infrastructure.db.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {
    Optional<ArtistEntity> findByNameAndProviderId(String name, String providerId);
}
