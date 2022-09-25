package org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model;

import lombok.Builder;
import lombok.NonNull;

import java.net.URI;
import java.util.Set;

@Builder
public record Album(@NonNull String id,
                    @NonNull String title,
                    @NonNull Set<Artist> artists,
                    @NonNull String releaseDate,
                    Set<String> genre,
                    @NonNull URI link) {
}
