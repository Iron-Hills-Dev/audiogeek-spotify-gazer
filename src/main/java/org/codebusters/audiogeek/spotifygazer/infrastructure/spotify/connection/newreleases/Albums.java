package org.codebusters.audiogeek.spotifygazer.infrastructure.spotify.connection.newreleases;

import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyNewReleasesAlbum;

import java.util.List;

import static java.util.stream.Collectors.toList;

public record Albums(List<Item> items, int total) {
    public List<SpotifyNewReleasesAlbum> toAlbumList() {
        return items().stream()
                .map(i -> SpotifyNewReleasesAlbum.builder()
                        .id(i.id())
                        .name(i.name())
                        .link(i.urls().get("spotify"))
                        .releaseDate(i.releaseDate())
                        .artists(i.toArtistList())
                        .build())
                .collect(toList());
    }
}
