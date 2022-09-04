package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import lombok.Builder;

@Builder
public record SpotifyNewReleasesArtist(String name, String id) {
}
