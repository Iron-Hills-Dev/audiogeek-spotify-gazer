package org.codebusters.audiogeek.spotifygazer.domain.dataexchange;

import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.GetAlbumsByGenreFilter;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface with methods for querying data in storage
 */
public interface DataQueryPort {
    /**
     * Gets all albums in data storage
     *
     * @param pageable pageable object
     * @return NewReleases object with all albums
     */
    Page<Album> getAlbums(Pageable pageable);

    /**
     * Gets albums by their genre
     *
     * @param filter Command for filtering albums
     * @return All albums consistent with filter
     */
    Page<Album> getAlbums(GetAlbumsByGenreFilter filter);
}
