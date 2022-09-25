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

import static org.codebusters.audiogeek.spotifygazer.domain.util.DtoUtils.convertToString;

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
            log.error("Program encountered an error while trying to get Spotify token", e);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Program encountered an unexpected error while trying to get new releases", e);
            return Optional.empty();
        }
    }

    private Optional<NewReleases> getNewReleasesInternal() {
        log.debug("Getting new releases");
        var tokenResponse = connectionPort.getToken();
        var rawNewReleases = getRawNewReleases(tokenResponse.token());

        var albums = convertToAlbumSet(rawNewReleases);

        albums = addGenresToAlbums(albums, tokenResponse.token());
        var finalNewReleases = NewReleases.builder()
                .albums(albums)
                .build();
        log.debug("Got new releases: {}", convertToString(finalNewReleases));
        if (finalNewReleases.albums().isEmpty()) {
            log.error("New releases list is empty");
            return Optional.empty();
        }
        return Optional.of(finalNewReleases);
    }

    private static Set<Album> convertToAlbumSet(Set<SpotifyNewReleasesAlbum> rawNewReleases) {
        log.trace("Converting raw new releases");
        var result = new LinkedHashSet<Album>();
        for (SpotifyNewReleasesAlbum album : rawNewReleases) {
            var convertResult = album.convertToAlbum();
            if (convertResult.isEmpty()) {
                continue;
            }
            result.add(convertResult.get());
        }
        return result;
    }


    private Set<Album> addGenresToAlbums(Set<Album> albums,
                                         String token) {
        log.trace("Adding genre info to albums");
        var newReleasesWithGenres = new LinkedHashSet<Album>();

        for (Album album : albums) {
            log.trace("Adding genres to album: {}", convertToString(album));
            var genres = SpotifyAlbumGenreAnalyzer.builder()
                    .artists(album.artists())
                    .token(token)
                    .artistGenreSupplier((t, a) -> connectionPort.getArtist(token, a.id()))
                    .build()
                    .getGenre();

            if (genres.isEmpty()) {
                log.error("Genres is empty, skipping album");
                continue;
            }

            var albumWithGenres = Album.builder()
                    .id(album.id())
                    .releaseDate(album.releaseDate())
                    .title(album.title())
                    .artists(album.artists())
                    .link(album.link())
                    .genre(genres.get())
                    .build();
            newReleasesWithGenres.add(albumWithGenres);
        }
        return newReleasesWithGenres;
    }


    private Set<SpotifyNewReleasesAlbum> getRawNewReleases(String token) {
        log.trace("Getting raw new releases via Spotify API: page-length={}", releasesPageLength);
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
}
