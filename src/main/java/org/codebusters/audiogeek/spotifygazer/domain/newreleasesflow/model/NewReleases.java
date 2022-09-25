package org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model;

import lombok.Builder;

import java.util.Set;

@Builder
public record NewReleases(Set<Album> albums) {
}
