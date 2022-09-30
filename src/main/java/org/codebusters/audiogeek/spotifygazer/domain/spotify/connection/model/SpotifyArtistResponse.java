package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import lombok.Builder;

import java.util.List;

/**
 * Spotify artist response.
 * @param id artist id.
 * @param genres artist genres.
 */
@Builder
public record SpotifyArtistResponse(String id, List<String> genres) {
}

