package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import java.util.List;

/**
 * Spotify final new releases.
 * @param albums list of albums (new releases).
 * @param total total accessible albums (new releases).
 */
public record SpotifyNewReleasesResponse(List<SpotifyNewReleasesAlbum> albums, int total) {
}
