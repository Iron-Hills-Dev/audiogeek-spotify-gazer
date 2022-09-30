package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Spotify final token response.
 * @param token token for accessing Spotify API (SECRET!).
 */
public record SpotifyTokenResponse(@JsonProperty("access_token") String token) {
}
