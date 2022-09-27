package org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record Artist(@NonNull String id, @NonNull String name) {
}
