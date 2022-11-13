package org.codebusters.audiogeek.spotifygazer.domain.dataexchange;

import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.AddAlbumCommand;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.RemoveAlbumCommand;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface with methods for modifying data in storage
 */
public interface DataModifyPort {
    /**
     * Adds album to data storage
     *
     * @param cmd Command for adding album
     * @return Optional UUID of newly added album
     */
    Optional<UUID> addAlbum(AddAlbumCommand cmd);

    /**
     * Removes album from data storage
     *
     * @param cmd Command for removing album
     */
    void removeAlbum(RemoveAlbumCommand cmd);
}
