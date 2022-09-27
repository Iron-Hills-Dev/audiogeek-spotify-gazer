package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Artist;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Builder
public record SpotifyNewReleasesAlbum(List<SpotifyNewReleasesArtist> artists,
                                      String id,
                                      String name,
                                      String releaseDate,
                                      String link) {
    public Set<Artist> toArtistSet() {
        return artists().stream()
                .map(ar -> Artist.builder()
                        .id(ar.id())
                        .name(ar.name())
                        .build())
                .collect(toSet());
    }

    public Optional<Album> convertToAlbum() {
        try {
            return Optional.of(Album.builder()
                    .id(this.id())
                    .title(this.name())
                    .releaseDate(this.releaseDate())
                    .link(URI.create(this.link()))
                    .artists(this.toArtistSet())
                    .build());
        } catch (Exception e) {
            log.error("Exception while converting album, skipping: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
