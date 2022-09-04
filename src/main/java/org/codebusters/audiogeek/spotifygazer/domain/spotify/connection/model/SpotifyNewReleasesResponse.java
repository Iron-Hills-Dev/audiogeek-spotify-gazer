package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import java.util.List;

public record SpotifyNewReleasesResponse(List<SpotifyNewReleasesAlbum> albums, int total) {
}
