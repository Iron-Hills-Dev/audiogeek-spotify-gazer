package org.codebusters.audiogeek.spotifygazer.domain.dataexchange;

import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.GetAlbumsCommand;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.NewReleases;

import java.util.Set;

public interface DataQueryPort {
    NewReleases getAlbums();
    Set<Album> getAlbums(GetAlbumsCommand cmd);
}
