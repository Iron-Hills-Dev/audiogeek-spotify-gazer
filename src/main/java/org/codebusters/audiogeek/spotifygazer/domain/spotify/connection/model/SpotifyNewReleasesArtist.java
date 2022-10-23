package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import lombok.Builder;

/**
 * Spotify final artist object.
 * @param name artist name.
 * @param id artist id.
 */
@Builder
public record SpotifyNewReleasesArtist(String name, String id) {

}
