package org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.With;

import java.net.URI;
import java.util.Set;

/**
 * Single album (new release).
 * @param id album id.
 * @param title album title.
 * @param artists album author(s).
 * @param releaseDate album release date.
 * @param genres album genre(s).
 * @param link link to album on streaming service (e.g. Spotify).
 */
@Builder
public record Album(@NonNull String id,
                    @NonNull String title,
                    @NonNull Set<Artist> artists,
                    @NonNull String releaseDate, //TODO change to LocalDate object\
                    @With Set<String> genres,
                    @NonNull URI link) {
}
