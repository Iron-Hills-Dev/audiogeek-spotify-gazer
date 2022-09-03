package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyTokenResponse(@JsonProperty("access_token") String token) {
}
