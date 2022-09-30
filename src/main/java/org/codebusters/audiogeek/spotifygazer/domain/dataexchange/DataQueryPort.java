package org.codebusters.audiogeek.spotifygazer.domain.dataexchange;

import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.GetAlbumsCommand;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.NewReleases;

import java.util.Set;

/**
 * Interface with methods for querying data in storage
 */
public interface DataQueryPort {
    /**
     * Gets all albums in data storage
     * @return NewReleases object with all albums
     */
    NewReleases getAlbums();

    /**
     * Gets albums chosen in cmd
     * @param cmd Command for filtering albums
     * @return All albums consistent with cmd
     */
    Set<Album> getAlbums(GetAlbumsCommand cmd);
}
