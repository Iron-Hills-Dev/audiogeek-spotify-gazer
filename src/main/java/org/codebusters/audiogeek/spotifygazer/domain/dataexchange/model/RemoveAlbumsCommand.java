package org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model;

import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Album;

import java.util.Set;

public record RemoveAlbumsCommand(Set<Album> albums) {
}
