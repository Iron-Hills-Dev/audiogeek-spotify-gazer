package org.codebusters.audiogeek.spotifygazer.infrastructure.spotify.connection;

import com.fasterxml.jackson.annotation.JsonProperty;

record GetTokenResponse(@JsonProperty("access_token") String accessToken) {
}
