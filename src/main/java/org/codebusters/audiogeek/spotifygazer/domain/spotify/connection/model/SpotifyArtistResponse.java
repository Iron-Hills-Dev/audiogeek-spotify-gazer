package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import java.util.List;

public record SpotifyArtistResponse(String id, List<String> genres) {
}

