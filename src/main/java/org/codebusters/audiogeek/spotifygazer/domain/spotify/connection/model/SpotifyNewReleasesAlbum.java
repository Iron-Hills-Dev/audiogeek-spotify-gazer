package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Artist;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toSet;

/**
 * Spotify final album object.
 *
 * @param artists     album artists.
 * @param id          album id.
 * @param name        album title.
 * @param releaseDate album release date.
 * @param link        link to album on Spotify App.
 */
@Slf4j
@Builder
public record SpotifyNewReleasesAlbum(List<SpotifyNewReleasesArtist> artists,
                                      String id,
                                      String name,
                                      String releaseDate,
                                      String link) {

    private static final String RELEASE_DATE_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER = ofPattern(RELEASE_DATE_FORMAT);

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
                    .releaseDate(toLocalDate(this.releaseDate))
                    .link(URI.create(this.link()))
                    .artists(this.toArtistSet())
                    .build());
        } catch (Exception e) {
            log.error("Exception while converting album, skipping: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private LocalDate toLocalDate(String releaseDate) {
        return LocalDate.parse(releaseDate, FORMATTER);
    }


}
