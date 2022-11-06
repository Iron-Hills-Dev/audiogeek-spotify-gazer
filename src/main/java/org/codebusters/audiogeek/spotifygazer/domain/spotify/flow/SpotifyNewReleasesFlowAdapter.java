package org.codebusters.audiogeek.spotifygazer.domain.spotify.flow;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.NewReleasesFlowPort;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.NewReleases;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.SpotifyConnectionPort;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.exception.SpotifyConnectionException;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyNewReleasesAlbum;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toSet;

@Builder
@Slf4j
class SpotifyNewReleasesFlowAdapter implements NewReleasesFlowPort {

    private final SpotifyConnectionPort connectionPort;
    private final int releasesPageLength;

    @Override
    public Optional<NewReleases> getNewReleases() {
        try {
            return getNewReleasesInternal();
        } catch (SpotifyConnectionException e) {
            return empty();
        } catch (Exception e) {
            log.error("Program encountered an unexpected error while trying to get new releases", e);
            return empty();
        }
    }

    private Optional<NewReleases> getNewReleasesInternal() {
        log.info("Getting new releases");
        var tokenResponse = connectionPort.getToken();

        var rawNewReleases = getRawNewReleases(tokenResponse.token());
        var albums = convertToAlbumSet(rawNewReleases);

        albums = addGenresToAlbums(albums, tokenResponse.token());
        return convertToNewReleases(albums);
    }

    private Set<SpotifyNewReleasesAlbum> getRawNewReleases(String token) {
        log.debug("Getting raw new releases via Spotify API: page-length={}", releasesPageLength);
        var totalNewReleases = new LinkedHashSet<SpotifyNewReleasesAlbum>();
        int offset = 0;
        int total = 100;
        do {
            try {
                log.trace("Getting new releases in loop: total={}, offset={}", total, offset);
                var rawNewReleases = connectionPort.getNewReleases(token, offset, releasesPageLength);
                totalNewReleases.addAll(rawNewReleases.albums());
                total = rawNewReleases.total();
                offset += releasesPageLength;
            } catch (Exception e) {
                log.error("Failed getting new releases, skipping to next offset: total={} offset={} page-length={}",
                        total, offset, releasesPageLength);
                offset += releasesPageLength;
            }
        } while (offset < total);
        log.trace("Retrieved raw new releases: {}", totalNewReleases);
        return totalNewReleases;
    }

    private static Set<Album> convertToAlbumSet(Set<SpotifyNewReleasesAlbum> rawNewReleases) {
        log.trace("Converting raw new releases to model");
        return rawNewReleases.stream()
                .map(SpotifyNewReleasesAlbum::convertToAlbum)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toSet());
    }

    private Set<Album> addGenresToAlbums(Set<Album> albums, String token) {
        log.trace("Adding genre info to albums");
        return albums.stream()
                .map(a -> getAlbumWithGenres(token, a))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toSet());
    }

    private static Optional<NewReleases> convertToNewReleases(Set<Album> albums) {
        if (albums.isEmpty()) {
            log.error("Album set is empty, skipping new releases creation.");
            return empty();
        }
        var finalNewReleases = new NewReleases(albums);
        log.debug("Got new releases: {}", finalNewReleases);
        return Optional.of(finalNewReleases);
    }


    private Optional<Album> getAlbumWithGenres(String token, Album album) {
        log.trace("Adding genres to album: {}", album);
        var genres = getAlbumGenres(token, album);
        if (genres.isEmpty()) {
            log.error("Genres are empty, skipping album");
            return empty();
        }
        var albumWithGenres = album.withGenres(genres);
        log.trace("Added genre to album: {}", albumWithGenres);
        return Optional.of(albumWithGenres);
    }

    private Set<String> getAlbumGenres(String token, Album album) {
        return SpotifyAlbumGenreAnalyzer.builder()
                .artists(album.artists())
                .token(token)
                .artistGenreSupplier((t, a) -> connectionPort.getArtist(token, a.id()))
                .build()
                .getGenre();
    }
}