package org.codebusters.audiogeek.spotifygazer.domain.dataexchange;

import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.AddAlbumsCommand;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.RemoveAlbumsCommand;

public interface DataModifyPort {
    void addAlbums(AddAlbumsCommand cmd);
    void removeAlbums(RemoveAlbumsCommand cmd);
}
