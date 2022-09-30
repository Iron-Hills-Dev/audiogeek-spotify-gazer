package org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model;

import java.util.Set;

public record RemoveAlbumsCommand(Set<String> albumsIds) {
}
