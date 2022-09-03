package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection;

/**
 * Port for connection with Spotify API.
 */
public interface SpotifyConnectionPort {

    /**
     * Retrieves token.
     *
     * @return token
     */
    SpotifyTokenResponse getToken();

    /**
     * Retrieves new releases.
     *
     * @param token  access token
     * @param offset page offset
     * @param limit  page size
     * @return DTO with releases data
     */
    SpotifyNewReleasesResponse getNewReleases(String token, int offset, int limit);

    /**
     * Gets artist data.
     *
     * @param token    access token
     * @param artistId artist ID on Spotify
     * @return DTO with artist data
     */
    SpotifyArtistResponse getArtist(String token, String artistId);
}
