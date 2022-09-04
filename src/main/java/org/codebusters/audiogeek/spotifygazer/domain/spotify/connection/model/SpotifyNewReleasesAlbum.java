package org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model;

import lombok.Builder;

import java.util.List;

@Builder
public record SpotifyNewReleasesAlbum(List<SpotifyNewReleasesArtist> artists,
                                      String id,
                                      String name,
                                      String releaseDate,
                                      String link) {
}
