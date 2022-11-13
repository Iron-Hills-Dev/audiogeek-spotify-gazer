package org.codebusters.audiogeek.spotifygazer.domain.newreleases.model;

import lombok.Builder;
import lombok.NonNull;

/**
 * Artist, entity responsible for creating album.
 *
 * @param id   artist id.
 * @param name arist's stage name.
 */
@Builder
public record Artist(@NonNull String id, @NonNull String name) {
}
