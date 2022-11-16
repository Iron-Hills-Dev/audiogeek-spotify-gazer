package org.codebusters.audiogeek.spotifygazer.domain.newreleases.flow;

import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;

import java.util.Set;

public record OutputModel(Set<Album> releases) {
}
