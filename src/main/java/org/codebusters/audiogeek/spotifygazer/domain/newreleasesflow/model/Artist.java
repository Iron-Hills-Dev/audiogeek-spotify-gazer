package org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model;

import lombok.Builder;
import lombok.NonNull;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.ArtistEntity;

/**
 * Artist, entity responsible for creating album.
 * @param id artist id.
 * @param name arist's stage name.
 */
@Builder
public record Artist(@NonNull String id, @NonNull String name) {


    public ArtistEntity convertToArtistEntity() {
        return new ArtistEntity(this.id, this.name);
    }
}
