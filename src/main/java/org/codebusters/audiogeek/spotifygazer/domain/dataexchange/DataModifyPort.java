package org.codebusters.audiogeek.spotifygazer.domain.dataexchange;

import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.AddAlbumsCommand;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.RemoveAlbumsCommand;

public interface DataModifyPort {
    /**
     * Adds one or more albums to data storage
     * @param cmd Command for adding albums
     */
    void addAlbums(AddAlbumsCommand cmd);

    /**
     * Removes one or more albums from data storage
     * @param cmd Command for removing albums
     */
    void removeAlbums(RemoveAlbumsCommand cmd);
}
