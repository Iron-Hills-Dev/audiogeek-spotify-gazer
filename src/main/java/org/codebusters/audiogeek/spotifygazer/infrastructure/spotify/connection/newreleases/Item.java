package org.codebusters.audiogeek.spotifygazer.infrastructure.spotify.connection.newreleases;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyNewReleasesArtist;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public record Item(List<Artist> artists, @JsonProperty("external_urls") Map<String, String> urls, String id,
                   String name, @JsonProperty("release_date") String releaseDate) {

    public List<SpotifyNewReleasesArtist> toArtistList() {
        return artists().stream()
                .map(a -> SpotifyNewReleasesArtist.builder()
                        .name(a.name())
                        .id(a.id())
                        .build())
                .collect(toList());
    }
}
