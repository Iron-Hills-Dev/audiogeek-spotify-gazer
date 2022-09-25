package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection;

import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.exception.SpotifyConnectionException;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyArtistResponse;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyNewReleasesResponse;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyTokenResponse;

/**
 * Port for connection with Spotify API.
 */
public interface SpotifyConnectionPort {

    /**
     * Retrieves token.
     *
     * @return token
     * @throws SpotifyConnectionException when token could not be retrieved
     */
    SpotifyTokenResponse getToken();

    /**
     * Retrieves new releases.
     *
     * @param token  access token
     * @param offset page offset
     * @param limit  page size
     * @return DTO with releases data
     * @throws SpotifyConnectionException when new releases could not be retrieved
     */
    SpotifyNewReleasesResponse getNewReleases(String token, int offset, int limit);

    /**
     * Gets artist data.
     *
     * @param token    access token
     * @param artistId artist ID on Spotify
     * @return DTO with artist data
     * @throws SpotifyConnectionException when artist could not be retrieved
     */
    SpotifyArtistResponse getArtist(String token, String artistId);
}
