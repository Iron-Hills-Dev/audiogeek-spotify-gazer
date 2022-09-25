package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import lombok.Builder;

import java.util.List;

@Builder
public record SpotifyArtistResponse(String id, List<String> genres) {
}

