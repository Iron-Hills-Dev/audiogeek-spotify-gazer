package org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model;

import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Album;

/**
 * Command for adding album
 *
 * @param album Album to add
 */
public record AddAlbumCommand(Album album) {
}
