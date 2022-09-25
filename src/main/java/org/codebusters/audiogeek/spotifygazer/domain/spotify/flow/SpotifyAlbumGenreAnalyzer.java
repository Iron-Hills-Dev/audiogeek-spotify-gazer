package org.codebusters.audiogeek.spotifygazer.domain.spotify.flow;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Artist;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyArtistResponse;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

@Slf4j
@Builder
class SpotifyAlbumGenreAnalyzer {

    private final Set<Artist> artists;
    private final String token;
    private final BiFunction<String, Artist, SpotifyArtistResponse> artistGenreSupplier;

    Optional<Set<String>> getGenre() {
        log.trace("Getting album genres");
        return getArtistsGenres(artists, token);
    }

    private Optional<Set<String>> getArtistsGenres(Set<Artist> artists, String token) {
        log.trace("Getting genres from artists");
        var genres = new LinkedHashSet<String>();
        for (Artist artist : artists) {
            try {
                var artistData = artistGenreSupplier.apply(token, artist);
                genres.addAll(artistData.genres());
                log.trace("Got genres from artist: artist={} genres={}", artist, artistData.genres());
            } catch (Exception e) {
                log.error("An error occurred while trying to get artist genres: artist={}", artist);
                return Optional.empty();
            }
        }
        log.trace("Got genres: {}", genres);
        return Optional.of(genres);
    }
}
