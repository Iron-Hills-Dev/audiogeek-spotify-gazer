package org.codebusters.audiogeek.spotifygazer.domain.spotify.retrieve;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Artist;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyArtistResponse;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

@Slf4j
@Builder
class SpotifyAlbumGenreAnalyzer {

    private final Set<Artist> artists;
    private final String token;
    private final BiFunction<String, Artist, SpotifyArtistResponse> artistGenreSupplier;

    Set<String> getGenre() {
        log.trace("Getting album genres");
        return getArtistsGenres(artists, token);
    }

    private Set<String> getArtistsGenres(Set<Artist> artists, String token) {
        log.trace("Getting genres from artists");
        var genres = new LinkedHashSet<String>();
        for (Artist artist : artists) {
            try {
                genres.addAll(getArtistGenres(token, artist));
            } catch (Exception e) {
                log.error("An error occurred while trying to get artist genres: artist={}", artist);
                return Set.of();
            }
        }
        log.trace("Got genres: {}", genres);
        return genres;
    }

    private List<String> getArtistGenres(String token, Artist artist) {
        var artistData = artistGenreSupplier.apply(token, artist);
        log.trace("Got genres from artist: artist={} genres={}", artist, artistData.genres());
        return artistData.genres();
    }
}