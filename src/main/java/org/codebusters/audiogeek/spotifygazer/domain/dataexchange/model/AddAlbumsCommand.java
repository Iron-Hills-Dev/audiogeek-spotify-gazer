package org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model;

import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Album;
import java.util.Set;

/**
 * Command for adding albums
 * @param albums Album set to add
 */
public record AddAlbumsCommand(Set<Album> albums) {
}
