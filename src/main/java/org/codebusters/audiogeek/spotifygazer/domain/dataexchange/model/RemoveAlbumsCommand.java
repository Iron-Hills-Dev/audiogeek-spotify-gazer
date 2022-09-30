package org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model;

import java.util.Set;

/**
 * Command for removing albums
 * @param albumsIds Set of albums identifiers chosen to delete
 */
public record RemoveAlbumsCommand(Set<String> albumsIds) {
}
